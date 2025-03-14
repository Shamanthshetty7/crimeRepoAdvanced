package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.entity.KycApplication;
import com.endava.CrimeReportingSystem.entity.UserProfile;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.KycApplicationDTO;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.mapper.KycApplicationMapper;
import com.endava.CrimeReportingSystem.mapper.UserProfileMapper;
import com.endava.CrimeReportingSystem.repository.KycApplicationRepository;
import com.endava.CrimeReportingSystem.repository.UserProfileRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class KycApplicationServiceTest {

    @Mock
    private KycApplicationRepository kycApplicationRepository;

    @Mock
    private KycApplicationMapper kycApplicationMapper;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private KycApplicationServiceImpl kycApplicationService;

    private Optional<KycApplication> kycApplication;
    private KycApplicationDTO kycApplicationDTO;
    private UserProfile userProfile;
    private UserProfileDTO userProfileDto;
    private Users users;

    @BeforeEach
    void setUp() {
    	 users=new Users();
         users.setUserId(1);
         
        userProfile = new UserProfile();
        userProfile.setProfileId(1);
        userProfile.setUsers(users);
        
        userProfileDto=UserProfileDTO.builder().profileId(1).build();
        
        
       
        
        kycApplication = Optional.ofNullable(new KycApplication());
        kycApplication.get().setKycId(1);
        kycApplication.get().setUserProfile(userProfile);
        kycApplication.get().setUserAdhaarFile(new byte[]{1, 2, 3}); // Mocked image data
        kycApplication.get().setUserVerificationImage(new byte[]{4, 5, 6}); // Mocked image data
        kycApplication.get().setCreatedAt(LocalDateTime.now());
        kycApplication.get().setUpdatedAt(LocalDateTime.now());

         kycApplicationDTO = KycApplicationDTO.builder()
        	    .KycId(1)
        	    .userProfile(userProfileDto)
        	    .build();

    }

    @Test
    void testGetAllKycApplications_Success() {
        List<KycApplication> kycApplicationList = new ArrayList<>();
        kycApplicationList.add(kycApplication.get());

        when(kycApplicationRepository.findAll()).thenReturn(kycApplicationList);
        when(kycApplicationMapper.kycApplicationToKycApplicationDTO(any(KycApplication.class))).thenAnswer(invocation -> {
            KycApplication application = invocation.getArgument(0);
            return KycApplicationDTO.builder()
            	    .KycId(application.getKycId())
            	    .userAdhaarFile(application.getUserAdhaarFile() != null ? 
            	        "data:" + application.getAdhaarFileType() + ";base64," + Base64.getEncoder().encodeToString(application.getUserAdhaarFile()) : null)
            	    .userVerificationImage(application.getUserVerificationImage() != null ? 
            	        "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(application.getUserVerificationImage()) : null)
            	    .userProfile(userProfileMapper.userProfileToUserProfileDTO(application.getUserProfile()))
            	    .build();

        });

        List<KycApplicationDTO> response = kycApplicationService.getAllKycApplications();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getKycId());
    }

    @Test
    void testSaveKycApplicationData_Success() {
        when(kycApplicationMapper.kycApplicationDTOToKycApplication(any(KycApplicationDTO.class))).thenReturn(kycApplication.get());
        when(kycApplicationRepository.save(any(KycApplication.class))).thenReturn(kycApplication.get());
        when(kycApplicationMapper.kycApplicationToKycApplicationDTO(any(KycApplication.class))).thenReturn(kycApplicationDTO);
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.of(userProfile));

        ApiGenericResponse<KycApplicationDTO> response = kycApplicationService.saveKycApplicationData(kycApplicationDTO);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getKycId());
    }

    @Test
    void testSaveKycApplicationData_RejectedApplicationExists() {
        Optional<KycApplication> existingKycApplication = Optional.ofNullable(new KycApplication());
        existingKycApplication.get().setIsActive(true);

        when(kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(anyInt())).thenReturn(existingKycApplication);
        when(kycApplicationMapper.kycApplicationDTOToKycApplication(any(KycApplicationDTO.class))).thenReturn(kycApplication.get());
        when(kycApplicationRepository.save(any(KycApplication.class))).thenReturn(kycApplication.get());
        when(kycApplicationMapper.kycApplicationToKycApplicationDTO(any(KycApplication.class))).thenReturn(kycApplicationDTO);
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.of(userProfile));

        ApiGenericResponse<KycApplicationDTO> response = kycApplicationService.saveKycApplicationData(kycApplicationDTO);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getKycId());
    }

    @Test
    void testSaveKycApplicationImages_Success() throws IOException {
        MultipartFile mockKycImage = org.mockito.Mockito.mock(MultipartFile.class);
        MultipartFile mockAdhaarFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(mockKycImage.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(mockAdhaarFile.getBytes()).thenReturn(new byte[]{4, 5, 6});
        when(mockAdhaarFile.getContentType()).thenReturn("image/jpeg");
        when(kycApplicationRepository.findById(anyInt())).thenReturn(kycApplication);
        when(kycApplicationMapper.kycApplicationToKycApplicationDTO(any())).thenReturn(kycApplicationDTO);
        when(kycApplicationRepository.save(any(KycApplication.class))).thenReturn(kycApplication.get());
        ApiGenericResponse<KycApplicationDTO> response = kycApplicationService.saveKycApplicationImages(1, mockKycImage, mockAdhaarFile);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getKycId());
        assertEquals("KYC Application updated successfully", response.getMessage());
    }

    @Test
    void testSaveKycApplicationImages_Failure() throws IOException {
        MultipartFile mockKycImage = org.mockito.Mockito.mock(MultipartFile.class);
        MultipartFile mockAdhaarFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(kycApplicationRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<KycApplicationDTO> response = kycApplicationService.saveKycApplicationImages(1, mockKycImage, mockAdhaarFile);

        assertNull(response.getData());
        assertEquals("KYC application not found", response.getMessage());
    }

    @Test
    void testGetKycApplicationByProfileId_Success() {
        when(kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(anyInt())).thenReturn(kycApplication);
        when(kycApplicationMapper.kycApplicationToKycApplicationDTO(any(KycApplication.class))).thenReturn(kycApplicationDTO);

        ApiGenericResponse<KycApplicationDTO> response = kycApplicationService.getKycApplicationByProfileId(1);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getKycId());
    }

    @Test
    void testGetKycApplicationByProfileId_Failure() {
        when(kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<KycApplicationDTO> response = kycApplicationService.getKycApplicationByProfileId(1);

        assertNull(response.getData());
        assertEquals("Incorrect Profile Id", response.getMessage());
    }

    @Test
    void testUpdateKycStatus_Success() {
        when(kycApplicationRepository.findById(anyInt())).thenReturn(kycApplication);
        when(kycApplicationRepository.save(any(KycApplication.class))).thenReturn(kycApplication.get());
        Boolean response = kycApplicationService.updateKycStatus(1, KycVerificationStatus.verified);

        assertEquals(true, response);
    }

    @Test
    void testUpdateKycStatus_Failure() {
        when(kycApplicationRepository.findById(anyInt())).thenReturn(Optional.empty());

        Boolean response = kycApplicationService.updateKycStatus(1, KycVerificationStatus.verified);

        assertEquals(false, response);
    }
}