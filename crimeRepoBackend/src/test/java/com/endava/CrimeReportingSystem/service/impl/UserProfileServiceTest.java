package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

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

@ExtendWith(MockitoExtension.class)
 class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private KycApplicationRepository kycApplicationRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UserProfile userProfile;
    private UserProfileDTO userProfileDTO;
    private Users user;
    private KycApplication kycApplication;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(1);
        user.setUserName("Shamanth");

        userProfile = new UserProfile();
        userProfile.setProfileId(1);
        userProfile.setUsers(user);
        userProfile.setUserProfileImage(new byte[]{1, 2, 3, 4});
        userProfile.setIsActive(true);

         userProfileDTO = UserProfileDTO.builder()
        	    .profileId(1)
        	    .userId(1)
        	    .userName("Shamanth")
        	    .build();

        

        kycApplication = new KycApplication();
        kycApplication.setKycVerificationStatus(KycVerificationStatus.underVerification);
    }

    @Test
    void testGetUserProfileById_Success() {
        when(userProfileRepository.findById(1)).thenReturn(Optional.of(userProfile));
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.getUserProfileById(1);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());
        
    }

  

    @Test
    void testSaveUserProfile_Success() {
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileDTOtoUserProfile(any(UserProfileDTO.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.saveUserProfile(userProfileDTO);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());
        assertNull(response.getMessage());
    }

    @Test
    void testSaveUserProfile_Failure_InvalidUserId() {
        when(usersRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<UserProfileDTO> response = userProfileService.saveUserProfile(userProfileDTO);

        assertEquals(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testSaveUserProfile_UpdateUserName() {
      
        user.setUserName("Old Name");

        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(usersRepository.save(user)).thenReturn(user); 
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileDTOtoUserProfile(any(UserProfileDTO.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.saveUserProfile(userProfileDTO);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());
        assertEquals("Shamanth", user.getUserName()); 
        assertNotNull(user.getUpdatedAt()); 

       
    }

    @Test
    void testUpdateUserProfile_Success() {
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileDTOtoUserProfile(any(UserProfileDTO.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.updateUserProfile(userProfileDTO);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());
    }

    @Test
    void testUpdateUserProfile_Failure_UserNotFound() {
        when(usersRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<UserProfileDTO> response = userProfileService.updateUserProfile(userProfileDTO);

        assertEquals(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage(), response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testUpdateUserProfile_UpdateUserName() {
        // Set up the initial user with a different name
        user.setUserName("Old Name");

        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(usersRepository.save(any(Users.class))).thenReturn(user); // Mock the save method to return the original user
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileDTOtoUserProfile(any(UserProfileDTO.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.updateUserProfile(userProfileDTO);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());
        assertEquals("Shamanth", user.getUserName()); // Ensure user name is updated
        assertNotNull(user.getUpdatedAt()); // Ensure updatedAt is set

       
    }

    @Test
    void testUpdateUserProfile_WithImage() {
        // Set up the userProfileDTO with a user profile image
        userProfileDTO.setUserProfileImage("someImageData");

        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileDTOtoUserProfile(any(UserProfileDTO.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);
        when(usersMapper.usersDTOtoUsers(any())).thenReturn(user);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.updateUserProfile(userProfileDTO);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());
        assertEquals("someImageData", response.getData().getUserProfileImage()); 
    }

    @Test
    void testUpdateUserProfile_SaveFailure() {
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(null); 
        when(userProfileMapper.userProfileDTOtoUserProfile(any(UserProfileDTO.class))).thenReturn(userProfile);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.updateUserProfile(userProfileDTO);

        assertEquals(UserProfileConstants.ERROR_PROFILE_UPDATE_FAILED.getMessage(), response.getMessage());
        assertNull(response.getData());
    }
    
    @Test
    void testDeleteUserProfile_Success() {
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);

        Boolean result = userProfileService.deleteUserProfile(1);

        assertTrue(result);
        assertFalse(userProfile.getIsActive()); 

      
    }

    @Test
    void testDeleteUserProfile_Failure_UserProfileNotFound() {
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.empty());

        Boolean result = userProfileService.deleteUserProfile(1);

        assertFalse(result);

    }
    
    
    @Test
    void testGetUserProfileKycStatusByUserId_Success() {
        when(userProfileRepository.getByUsersUserId(anyInt())).thenReturn(userProfile);
        when(kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(anyInt())).thenReturn(Optional.of(kycApplication));

        ApiGenericResponse<KycVerificationStatus> response = userProfileService.getUserProfileKycStatusByUserId(1);

        assertNotNull(response.getData());
        assertEquals(KycVerificationStatus.underVerification, response.getData());
    }

    @Test
    void testGetUserProfileKycStatusByUserId_UserProfileNotFound() {
        when(userProfileRepository.getByUsersUserId(anyInt())).thenReturn(null);

        ApiGenericResponse<KycVerificationStatus> response = userProfileService.getUserProfileKycStatusByUserId(1);

        assertNull(response.getData());
        assertEquals(UserProfileConstants.ERROR_NO_KYC_APPLICATION.getMessage(), response.getMessage());
    }

    @Test
    void testGetUserProfileKycStatusByUserId_KycApplicationNotFound() {
        when(userProfileRepository.getByUsersUserId(anyInt())).thenReturn(userProfile);
        when(kycApplicationRepository.getByUserProfileProfileIdAndIsActiveTrue(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<KycVerificationStatus> response = userProfileService.getUserProfileKycStatusByUserId(1);

        assertNull(response.getData());
        assertEquals(UserProfileConstants.ERROR_NO_KYC_APPLICATION.getMessage(), response.getMessage());
    }

    @Test
    void testGetUserProfileKycStatusByUserId_RepositoryThrowsException() {
        when(userProfileRepository.getByUsersUserId(anyInt())).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, ()->userProfileService.getUserProfileKycStatusByUserId(1));
      
    }
    
    @Test
    void testGetUserProfileByUserId_Success() {
        when(userProfileRepository.getByUsersUserId(anyInt())).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);

        ApiGenericResponse<UserProfileDTO> response = userProfileService.getUserProfileByUserId(1);

        assertNotNull(response.getData());
        assertEquals("Shamanth", response.getData().getUserName());

        String base64Image = Base64.getEncoder().encodeToString(new byte[]{1, 2, 3, 4});
        assertEquals(CommonConstants.BASE64_IMAGE_JPEG_PREFIX + base64Image, response.getData().getUserProfileImage());
       
    }
    
    @Test
    void testGetUserProfileByUserId_Failure_UserProfileNotFound() {
        
        when(userProfileRepository.getByUsersUserId(anyInt())).thenReturn(null);

       
        ApiGenericResponse<UserProfileDTO> response = userProfileService.getUserProfileByUserId(999);

        assertNull(response.getData());
        assertEquals(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage(), response.getMessage());
    }
    
    @Test
    void testSaveUserProfileImage_Success() throws Exception {
        
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        when(userProfileMapper.userProfileToUserProfileDTO(any(UserProfile.class))).thenReturn(userProfileDTO);

        MockMultipartFile mockFile = new MockMultipartFile("userProfileImage", "test.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

     
        ApiGenericResponse<UserProfileDTO> response = userProfileService.saveUserProfileImage(1, mockFile);

       
        assertNotNull(response.getData());
        assertEquals(UserProfileConstants.SUCCESS_USER_PROFILE_UPDATED.getMessage(), response.getMessage());
    }
    
    @Test
    void testSaveUserProfileImage_UserProfileNotFound() throws Exception {
     
        when(userProfileRepository.findById(anyInt())).thenReturn(Optional.empty());

       
        MockMultipartFile mockFile = new MockMultipartFile("userProfileImage", "test.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        ApiGenericResponse<UserProfileDTO> response = userProfileService.saveUserProfileImage(999, mockFile);

       
        assertNull(response.getData());
        assertEquals(UserProfileConstants.ERROR_USER_PROFILE_NOT_FOUND.getMessage(), response.getMessage());
    }


    
}