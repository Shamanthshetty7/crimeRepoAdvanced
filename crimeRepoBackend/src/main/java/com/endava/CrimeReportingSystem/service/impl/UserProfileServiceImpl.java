package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.UserProfileConstants;
import com.endava.CrimeReportingSystem.entity.KycApplication;
import com.endava.CrimeReportingSystem.entity.UserProfile;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.mapper.UserProfileMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.KycApplicationRepository;
import com.endava.CrimeReportingSystem.repository.UserProfileRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	
	private final UserProfileRepository userProfileRepository;
    private final KycApplicationRepository kycApplicationRepository;
    private final UserProfileMapper userProfileMapper;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    // Constructor injection
    public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
                                  KycApplicationRepository kycApplicationRepository,
                                  UserProfileMapper userProfileMapper,
                                  UsersRepository usersRepository,
                                  UsersMapper usersMapper) {
        this.userProfileRepository = userProfileRepository;
        this.kycApplicationRepository = kycApplicationRepository;
        this.userProfileMapper = userProfileMapper;
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }
	
	
	/**
	 * Method to fetch user profile by profile id
	 */
	
	@Override
	public ApiGenericResponse<UserProfileDTO> getUserProfileById(int userProfileId) {
		Optional<UserProfile> userProfile= userProfileRepository.findById(userProfileId);
		ApiGenericResponse<UserProfileDTO> response=new ApiGenericResponse<>(null,null);
		if(userProfile.isEmpty()) {
			response.setMessage(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage());
		}
		UserProfileDTO userProfileDTO=userProfileMapper.userProfileToUserProfileDTO(userProfile.get());
		response.setData(userProfileDTO);
		return response;
	}

	/**
	 * Saving profile data
	 */
	@Override
	public ApiGenericResponse<UserProfileDTO> saveUserProfile(UserProfileDTO userProfileDTO) {
		ApiGenericResponse<UserProfileDTO> response=new ApiGenericResponse<>(null,null);
	    Optional<Users> user=usersRepository.findById(userProfileDTO.getUserId());
		
	    //updating userName if it is updated
		if(user.isEmpty()) {
			response.setMessage(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage());
			return response;
		}else if(!user.get().getUserName().equals(userProfileDTO.getUserName())) {
			user.get().setUserName(userProfileDTO.getUserName());
			user.get().setUpdatedAt(LocalDateTime.now());
			usersRepository.save(user.get());
		}
		
		userProfileDTO.setCreatedAt(LocalDateTime.now());
		
		UserProfile savingUserProfile=userProfileMapper.userProfileDTOtoUserProfile(userProfileDTO);
		savingUserProfile.setUsers(usersMapper.usersDTOtoUsers(userProfileDTO.getUser()));
		savingUserProfile.setIsActive(true);
		UserProfile userProfile= userProfileRepository.save(savingUserProfile);
		
		
		if(userProfile==null) {
			response.setMessage(UserProfileConstants.ERROR_PROFILE_UPDATE_FAILED.getMessage());
			return response;
		}
		UserProfileDTO saveduserProfileDTO=userProfileMapper.userProfileToUserProfileDTO(userProfile);
		response.setData(saveduserProfileDTO);
		return response;
		
	}

	/**
	 * Updating profile
	 */
	@Override
	public ApiGenericResponse<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO) {
		
		ApiGenericResponse<UserProfileDTO> response=new ApiGenericResponse<>(null,null);
	    Optional<Users> user=usersRepository.findById(userProfileDTO.getUserId());
		
	  //updating userName if it is updated
		if(user.isEmpty()) {
			response.setMessage(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage());
			return response;
		}else if(!user.get().getUserName().equals(userProfileDTO.getUserName())) {
			user.get().setUserName(userProfileDTO.getUserName());
			user.get().setUpdatedAt(LocalDateTime.now());
			usersRepository.save(user.get());
		}
		
		UserProfile updatingUserProfile=userProfileMapper.userProfileDTOtoUserProfile(userProfileDTO);
		updatingUserProfile.setUsers(usersMapper.usersDTOtoUsers(userProfileDTO.getUser()));
		updatingUserProfile.setUpdatedAt(LocalDateTime.now());
		
		
		
		if(userProfileDTO.getUserProfileImage()!=null) {
			 Optional<UserProfile> updatingSameImage=userProfileRepository.findById(userProfileDTO.getProfileId());
		     if(!updatingSameImage.isEmpty()) {
		    	 updatingUserProfile.setUserProfileImage(updatingSameImage.get().getUserProfileImage());
		     }
			
			 
		}
		
		UserProfile userProfile= userProfileRepository.save(updatingUserProfile);
		
		if(userProfile==null) {
			response.setMessage(UserProfileConstants.ERROR_PROFILE_UPDATE_FAILED.getMessage());
			return response;
		}
		
		UserProfileDTO saveduserProfileDTO=userProfileMapper.userProfileToUserProfileDTO(userProfile);
		response.setData(saveduserProfileDTO);
		return response;
	}

	/**
	 * deleting profile by profile ID
	 */
	@Override
	public Boolean deleteUserProfile(int userProfileId) {
		Optional<UserProfile> deletingUserProfile=userProfileRepository.findById(userProfileId);
		if(!deletingUserProfile.isEmpty()) {
			deletingUserProfile.get().setIsActive(false);
			userProfileRepository.save(deletingUserProfile.get());
			return true;
		}
		return false;
	}
	
	
	/**
	 * method to fetch kyc status from userprofile using user id
	 */
	
	@Override
	public ApiGenericResponse<KycVerificationStatus>  getUserProfileKycStatusByUserId(int userId) {
		ApiGenericResponse<KycVerificationStatus> response=new ApiGenericResponse<>(null,null);

		UserProfile userProfile=userProfileRepository.getByUsersUserId(userId);
		if(userProfile!=null) {
			Optional<KycApplication> kycApplication=kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(userProfile.getProfileId());
			if(kycApplication.isEmpty()) {
				response.setMessage(UserProfileConstants.ERROR_NO_KYC_APPLICATION.getMessage());
			}else {
				response.setData(kycApplication.get().getKycVerificationStatus());
			}
			 
		}else {
			response.setMessage(UserProfileConstants.ERROR_NO_KYC_APPLICATION.getMessage());
		}
		return response;
		
	}

	/**
	 * fetch user profile by user id
	 */
	@Override
	public ApiGenericResponse<UserProfileDTO> getUserProfileByUserId(int userId) {
		
		UserProfile userProfile= userProfileRepository.getByUsersUserId(userId);
		
		ApiGenericResponse<UserProfileDTO> response=new ApiGenericResponse<>(null,null);
		if(userProfile==null) {
			response.setMessage(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage());
			return response;
		}
		UserProfileDTO userProfileDTO=userProfileMapper.userProfileToUserProfileDTO(userProfile);
		if (userProfile.getUserProfileImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(userProfile.getUserProfileImage());
            userProfileDTO.setUserProfileImage(CommonConstants.BASE64_IMAGE_JPEG_PREFIX + base64Image);
        }
		response.setData(userProfileDTO);
		return response;
	}
	
	/**
	 * saving user profile image
	 */
	@Override
	public ApiGenericResponse<UserProfileDTO> saveUserProfileImage(int userProfileId, MultipartFile userProfileImage) throws Exception {
		 
	    Optional<UserProfile> updatingUserProfile = userProfileRepository.findById(userProfileId);
	    ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);

	    if (updatingUserProfile.isEmpty()) {
	        response.setMessage(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage());
	        return response;
	    }

	    updatingUserProfile.get().setUserProfileImage(userProfileImage.getBytes());

	    UserProfile savedUserProfile = userProfileRepository.save(updatingUserProfile.get());

	    response.setData(userProfileMapper.userProfileToUserProfileDTO(savedUserProfile));
	    response.setMessage(UserProfileConstants.SUCCESS_USER_PROFILE_UPDATED.getMessage());

	    return response;
	}
	

}
