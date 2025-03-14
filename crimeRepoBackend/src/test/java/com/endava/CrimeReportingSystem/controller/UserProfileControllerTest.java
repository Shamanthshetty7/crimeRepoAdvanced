package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.constants.UserProfileConstants;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.UserProfileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserProfileServiceImpl userProfileServiceImpl;

    @InjectMocks
    private UserProfileController userProfileController;
    
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
    	objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }

    @Test
    void testGetUserProfileById_Success() throws Exception {
        // Mocking service behavior
    	UserProfileDTO userProfile = UserProfileDTO.builder()
    		    .userId(1)
    		    .userName("Shamanth")
    		    .build();
        ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(userProfile);
        when(userProfileServiceImpl.getUserProfileById(anyInt()))
                .thenReturn(response);

       
        mockMvc.perform(get("/crime-reporting-system/user-profile/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testGetUserProfileById_NotFound() throws Exception {

    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("UserProfile not found");
        when(userProfileServiceImpl.getUserProfileById(anyInt()))
                .thenReturn(response);

      
        mockMvc.perform(get("/crime-reporting-system/user-profile/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("UserProfile not found"));
    }

    @Test
    void testGetUserProfileById_InternalServerError() throws Exception {
        
        when(userProfileServiceImpl.getUserProfileById(anyInt()))
                .thenThrow(new RuntimeException("Database error"));

     
        mockMvc.perform(get("/crime-reporting-system/user-profile/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching user profile."));
    }

    @Test
    void testGetUserProfileKycStatusByUserId_Success() throws Exception {
    	ApiGenericResponse<KycVerificationStatus> response = new ApiGenericResponse<>(null, null);
    	response.setData(KycVerificationStatus.verified);
        when(userProfileServiceImpl.getUserProfileKycStatusByUserId(anyInt()))
                .thenReturn(response);

       
        mockMvc.perform(get("/crime-reporting-system/user-profile/KycStatus/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("verified"));
    }

    @Test
    void testGetUserProfileKycStatusByUserId_NotFound() throws Exception {
    	ApiGenericResponse<KycVerificationStatus> response = new ApiGenericResponse<>(null, null);
    	
    	response.setMessage( "KYC status not found");
        when(userProfileServiceImpl.getUserProfileKycStatusByUserId(anyInt()))
                .thenReturn(response);

      
        mockMvc.perform(get("/crime-reporting-system/user-profile/KycStatus/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("KYC status not found"));
    }

    @Test
    void testGetUserProfileKycStatusByUserId_InternalServerError() throws Exception {
        
        when(userProfileServiceImpl.getUserProfileKycStatusByUserId(anyInt()))
                .thenThrow(new RuntimeException("Service error"));

      
        mockMvc.perform(get("/crime-reporting-system/user-profile/KycStatus/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching KYC status."));
    }
    
    @Test
    void testGetUserProfileByUserId_Success() throws Exception {
      
    	UserProfileDTO userProfileDTO = UserProfileDTO.builder()
    		    .userId(1)
    		    .userName("Shamanth")
    		    .build();
    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	response.setData(userProfileDTO);
        when(userProfileServiceImpl.getUserProfileByUserId(1))
                .thenReturn(response);

       
        mockMvc.perform(get("/crime-reporting-system/user-profile/userProfile/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testGetUserProfileByUserId_NotFound() throws Exception {
       
    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	response.setMessage("UserProfile not found");
        when(userProfileServiceImpl.getUserProfileByUserId(anyInt()))
                .thenReturn(response);

        
        mockMvc.perform(get("/crime-reporting-system/user-profile/userProfile/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("UserProfile not found"));
    }

    @Test
    void testGetUserProfileByUserId_InternalServerError() throws Exception {
       
        when(userProfileServiceImpl.getUserProfileByUserId(1))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/user-profile/userProfile/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching user profile."));
    }

 
    @Test
    void testSaveUserProfile_Success() throws Exception {
      
    	UserProfileDTO userProfileDTO = UserProfileDTO.builder()
    		    .userId(1)
    		    .profileId(1)
    		    .userName("Shamanth")
    		    .build();

    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	
    	response.setData(userProfileDTO);
    	
        when(userProfileServiceImpl.saveUserProfile(any(UserProfileDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testSaveUserProfile_Failure() throws Exception {
    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	response.setMessage("Failed to save user profile");
        when(userProfileServiceImpl.saveUserProfile(any(UserProfileDTO.class)))
                .thenReturn(response);

       
        mockMvc.perform(post("/crime-reporting-system/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserProfileDTO.builder().build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to save user profile"));
    }

    @Test
    void testSaveUserProfile_InternalServerError() throws Exception {
      
        when(userProfileServiceImpl.saveUserProfile(any(UserProfileDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

       
        mockMvc.perform(post("/crime-reporting-system/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserProfileDTO.builder().build())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while saving user profile."));
    }
    
    @Test
    void testSaveUserProfileImage_Success() throws Exception {
     
    	UserProfileDTO userProfileDTO = UserProfileDTO.builder()
    		    .userId(1)
    		    .userName("Shamanth")
    		    .build();


        MockMultipartFile imageFile = new MockMultipartFile("userProfileImage", "image.jpg", "image/jpeg", new byte[] {1, 2, 3, 4});
    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	response.setData(userProfileDTO);
        when(userProfileServiceImpl.saveUserProfileImage(1, imageFile))
                .thenReturn(response);

     
        
        
              
        
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .multipart("/crime-reporting-system/user-profile/saveUserProfileImage/1") 
                        .file(imageFile) 
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testSaveUserProfileImage_NotFound() throws Exception {
       
        MockMultipartFile imageFile = new MockMultipartFile("userProfileImage", "image.jpg", "image/jpeg", new byte[] {1, 2, 3, 4});

        ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	response.setMessage("UserProfile not found");
        when(userProfileServiceImpl.saveUserProfileImage(anyInt(), any()))
                .thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/user-profile/saveUserProfileImage/999") 
                .file(imageFile) 
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("UserProfile not found"));
    }

    @Test
    void testSaveUserProfileImage_InternalServerError() throws Exception {
        
        MockMultipartFile imageFile = new MockMultipartFile("userProfileImage", "image.jpg", "image/jpeg", new byte[] {1, 2, 3, 4});

        when(userProfileServiceImpl.saveUserProfileImage(1, imageFile))
                .thenThrow(new RuntimeException("Unexpected error"));

     
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/user-profile/saveUserProfileImage/1") 
                .file(imageFile) 
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while saving user profile image."));
    }

    
    @Test
    void testUpdateUserProfile_Success() throws Exception {
      
    	UserProfileDTO userProfileDTO = UserProfileDTO.builder()
    		    .userId(1)
    		    .userName("Shamanth")
    		    .build();


        ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
    	response.setData(userProfileDTO);
        when(userProfileServiceImpl.updateUserProfile(any(UserProfileDTO.class)))
                .thenReturn(response);

       
        mockMvc.perform(put("/crime-reporting-system/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testUpdateUserProfile_NotFound() throws Exception {
       
    	ApiGenericResponse<UserProfileDTO> response = new ApiGenericResponse<>(null, null);
     	response.setMessage("UserProfile not found");
        when(userProfileServiceImpl.updateUserProfile(any(UserProfileDTO.class)))
                .thenReturn(response);

        
        mockMvc.perform(put("/crime-reporting-system/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserProfileDTO.builder().build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("UserProfile not found"));
    }

    @Test
    void testUpdateUserProfile_InternalServerError() throws Exception {
        
        when(userProfileServiceImpl.updateUserProfile(any(UserProfileDTO.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

      
        mockMvc.perform(put("/crime-reporting-system/user-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserProfileDTO.builder().build())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while updating user profile."));
    }
    
    @Test
    void testDeleteUser_Success() throws Exception {
        when(userProfileServiceImpl.deleteUserProfile(anyInt())).thenReturn(true);

        
        mockMvc.perform(delete("/crime-reporting-system/user-profile/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User profile deleted successfully."));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(userProfileServiceImpl.deleteUserProfile(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/crime-reporting-system/user-profile/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(UserProfileConstants.ERROR_PROFILE_DELETE_FAILED.getMessage()));
    }

    @Test
    void testDeleteUser_InternalServerError() throws Exception {
        when(userProfileServiceImpl.deleteUserProfile(anyInt()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(delete("/crime-reporting-system/user-profile/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while deleting user profile."));
    }
    
    
}
