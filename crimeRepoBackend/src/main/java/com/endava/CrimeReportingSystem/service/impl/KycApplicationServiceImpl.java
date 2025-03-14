package com.endava.CrimeReportingSystem.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.KycApplicationConstants;
import com.endava.CrimeReportingSystem.entity.KycApplication;
import com.endava.CrimeReportingSystem.entity.UserProfile;
import com.endava.CrimeReportingSystem.entity.dto.KycApplicationDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.mapper.KycApplicationMapper;
import com.endava.CrimeReportingSystem.mapper.UserProfileMapper;
import com.endava.CrimeReportingSystem.repository.KycApplicationRepository;
import com.endava.CrimeReportingSystem.repository.UserProfileRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.KycApplicationService;

@Service
public class KycApplicationServiceImpl implements  KycApplicationService  {

	private final KycApplicationRepository kycApplicationRepository;
    private final KycApplicationMapper kycApplicationMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    // Constructor injection
    public KycApplicationServiceImpl(KycApplicationRepository kycApplicationRepository,
                                     KycApplicationMapper kycApplicationMapper,
                                     UserProfileMapper userProfileMapper,
                                     UserProfileRepository userProfileRepository) {
        this.kycApplicationRepository = kycApplicationRepository;
        this.kycApplicationMapper = kycApplicationMapper;
        this.userProfileMapper = userProfileMapper;
        this.userProfileRepository = userProfileRepository;
    }
	
	 /** 
     * Method to retrieve all KYC applications.
     */
	@Override
	public List<KycApplicationDTO> getAllKycApplications() {
        List<KycApplication> kycApplicationList = kycApplicationRepository.findAll();
		
	    return  kycApplicationList.stream().map((application) -> {
	    	
	    	KycApplicationDTO kycApplicationDTO = kycApplicationMapper.kycApplicationToKycApplicationDTO(application);
	    	
	        if (application.getUserAdhaarFile() != null&&application.getUserVerificationImage()!=null) {
	        	//converting byte to base64 format to send it to frontend
	            String base64ImageOfUserAdhaarFile = Base64.getEncoder().encodeToString(application.getUserAdhaarFile());
	            kycApplicationDTO.setUserAdhaarFile("data:"+application.getAdhaarFileType()+";base64,"  + base64ImageOfUserAdhaarFile);
	            
	            String base64ImageOfUserVerificationImage = Base64.getEncoder().encodeToString(application.getUserVerificationImage());
	            kycApplicationDTO.setUserVerificationImage(CommonConstants.BASE64_IMAGE_JPEG_PREFIX+ base64ImageOfUserVerificationImage);
	        }
	        kycApplicationDTO.setUserProfile(userProfileMapper.userProfileToUserProfileDTO(application.getUserProfile()));
	       
	        return kycApplicationDTO;
	    }).toList();
	   
		
	}



	
	 /** 
     * Method to save KYC application data.
     */
	@Override
	public ApiGenericResponse<KycApplicationDTO> saveKycApplicationData(KycApplicationDTO kycApplicationDTO) {
		ApiGenericResponse<KycApplicationDTO> response=new ApiGenericResponse<>(null,null);
		
		//setting rejected kyc application to inactive application by fetching it using profile id
        Optional<KycApplication> existingKycApplication=kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(kycApplicationDTO.getUserProfile().getProfileId());
        if(!existingKycApplication.isEmpty()) {
        	existingKycApplication.get().setIsActive(false);
        	Optional.ofNullable(kycApplicationRepository.save(existingKycApplication.get()))
        	.orElseThrow(()->new RuntimeException(KycApplicationConstants.ERROR_SAVING_KYC_APPLICATION.getMessage()));
        }
		KycApplication kycApplication=kycApplicationMapper.kycApplicationDTOToKycApplication(kycApplicationDTO);
		kycApplication.setCreatedAt(LocalDateTime.now());
		kycApplication.setUpdatedAt(LocalDateTime.now());
		kycApplication.setUserProfile(userProfileMapper.userProfileDTOtoUserProfile(kycApplicationDTO.getUserProfile()));
		kycApplication.setKycVerificationStatus(KycVerificationStatus.underVerification);
		kycApplication.setIsActive(true);
		
		KycApplicationDTO addedKycDataDto=kycApplicationMapper.kycApplicationToKycApplicationDTO(kycApplicationRepository.save(kycApplication));
		Optional<UserProfile> userProfile=userProfileRepository.findById(kycApplicationDTO.getUserProfile().getProfileId());
		if(userProfile.isEmpty()) {
			response.setMessage(KycApplicationConstants.ERROR_SAVING_KYC_APPLICATION.getMessage());
			return response;
		}
	    addedKycDataDto.setUserId(userProfile.get().getUsers().getUserId());
		response.setData(addedKycDataDto);
		
		return response;
	}
	
	
	/** 
     * Method to save KYC application images (Aadhaar and verification images).
     */
	@Override
	public ApiGenericResponse<KycApplicationDTO> saveKycApplicationImages(int kycApplicationId, MultipartFile kycImage, MultipartFile adhaarFile) throws IOException {
		ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null, null);
	    Optional<KycApplication> updatingKycApplication = kycApplicationRepository.findById(kycApplicationId);
	    

	    if (updatingKycApplication.isEmpty()) {
	        response.setMessage(KycApplicationConstants.ERROR_NO_KYC_APPLICATION_FOUND.getMessage());
	        return response;
	    }

	    updatingKycApplication.get().setUserVerificationImage(kycImage.getBytes());
	    updatingKycApplication.get().setUserAdhaarFile(adhaarFile.getBytes());
	    updatingKycApplication.get().setAdhaarFileType(adhaarFile.getContentType());
	    KycApplication savedKycApplication =Optional.of(kycApplicationRepository.save(updatingKycApplication.get()))
	    		.orElseThrow(()->new RuntimeException(KycApplicationConstants.ERROR_SAVING_KYC_APPLICATION.getMessage()));

	    response.setData(kycApplicationMapper.kycApplicationToKycApplicationDTO(savedKycApplication));
	    response.setMessage(KycApplicationConstants.SUCCESS_KYC_APPLICATION_UPDATED.getMessage());

	    return response;
	}


	/** 
     * Method to retrieve KYC application by Profile ID.
     */
	@Override
	public ApiGenericResponse<KycApplicationDTO> getKycApplicationByProfileId(int profileId) {
		ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null, null);
        Optional<KycApplication> kycApplication=kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(profileId);
        
        if(!kycApplication.isEmpty()) {
        	response.setData(kycApplicationMapper.kycApplicationToKycApplicationDTO(kycApplication.get()));
        }else{
        	response.setMessage(KycApplicationConstants.ERROR_INCORRECT_PROFILE_ID.getMessage());
        }
		return response;
	}


	/** 
     * Method to update the status of the KYC application.
     */
	@Override
	public Boolean updateKycStatus(int kycId, KycVerificationStatus kycStatus) {
		Optional<KycApplication> updatingKycApplication=kycApplicationRepository.findById(kycId);
		if(!updatingKycApplication.isEmpty()) {
			updatingKycApplication.get().setUpdatedAt(LocalDateTime.now());
			
			updatingKycApplication.get().setKycVerificationStatus(kycStatus);
		Optional.ofNullable(kycApplicationRepository.save(updatingKycApplication.get()))
		.orElseThrow(()->new RuntimeException(KycApplicationConstants.ERROR_SAVING_KYC_APPLICATION.getMessage()));
			return true;
			
		}else {
			return false;
		}
	
	}



	

	
}
