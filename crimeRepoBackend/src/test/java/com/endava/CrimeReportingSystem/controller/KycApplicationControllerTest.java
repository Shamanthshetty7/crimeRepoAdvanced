package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.entity.UserProfile;
import com.endava.CrimeReportingSystem.entity.dto.KycApplicationDTO;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.KycApplicationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class KycApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private KycApplicationServiceImpl kycApplicationServiceImpl;

    @InjectMocks
    private KycApplicationController kycApplicationController;

    private ObjectMapper objectMapper;
    private KycApplicationDTO kycApplicationDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(kycApplicationController).build();

        UserProfile userProfile=new UserProfile();
        userProfile.setProfileId(1001);
        
        UserProfileDTO userProfileDTO= UserProfileDTO.builder().profileId(1001).build();
       
        // Sample KYC application
        kycApplicationDTO = KycApplicationDTO.builder()
        	    .KycId(1)
        	    .userProfile(userProfileDTO)
        	    .kycVerificationStatus(KycVerificationStatus.underVerification)
        	    .build();
    }

    // Test for getKycApplicationByProfileId
    @Test
    void testGetKycApplicationByProfileId_Success() throws Exception {
        ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(kycApplicationDTO);

        when(kycApplicationServiceImpl.getKycApplicationByProfileId(1001)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/kyc-application/getKycApplicationByProfileId/1001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kycId").value(1))
                .andExpect(jsonPath("$.userProfile.profileId").value(1001))
                .andExpect(jsonPath("$.kycVerificationStatus").value("underVerification"));
    }

    @Test
    void testGetKycApplicationByProfileId_NotFound() throws Exception {
        ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(null);

        when(kycApplicationServiceImpl.getKycApplicationByProfileId(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/kyc-application/getKycApplicationByProfileId/1001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("KYC application not found"));
    }

    @Test
    void testGetKycApplicationByProfileId_Exception() throws Exception {
        when(kycApplicationServiceImpl.getKycApplicationByProfileId(anyInt()))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/crime-reporting-system/kyc-application/getKycApplicationByProfileId/1001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching KYC application."));
    }

    // Test for getAllKycApplications
    @Test
    void testGetAllKycApplications_Success() throws Exception {
        List<KycApplicationDTO> applications = List.of(kycApplicationDTO);
        when(kycApplicationServiceImpl.getAllKycApplications()).thenReturn(applications);

        mockMvc.perform(get("/crime-reporting-system/kyc-application/getAllKycApplications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].kycId").value(1))
                .andExpect(jsonPath("$[0].userProfile.profileId").value(1001))
                .andExpect(jsonPath("$[0].kycVerificationStatus").value("underVerification"));
    }

    @Test
    void testGetAllKycApplications_NotFound() throws Exception {
        when(kycApplicationServiceImpl.getAllKycApplications()).thenReturn(List.of());

        mockMvc.perform(get("/crime-reporting-system/kyc-application/getAllKycApplications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("KYC application not found"));
    }

    @Test
    void testGetAllKycApplications_Exception() throws Exception {
        when(kycApplicationServiceImpl.getAllKycApplications()).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/crime-reporting-system/kyc-application/getAllKycApplications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching all KYC applications."));
    }

    // Test for saveKycApplication
    @Test
    void testSaveKycApplication_Success() throws Exception {
        ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(kycApplicationDTO);

        when(kycApplicationServiceImpl.saveKycApplicationData(any())).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/kyc-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kycApplicationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kycId").value(1))
                .andExpect(jsonPath("$.userProfile.profileId").value(1001))
                .andExpect(jsonPath("$.kycVerificationStatus").value("underVerification"));
    }

    @Test
    void testSaveKycApplication_Failure() throws Exception {
        ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("Failed");

        when(kycApplicationServiceImpl.saveKycApplicationData(any(KycApplicationDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/kyc-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kycApplicationDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed"));
    }

    @Test
    void testSaveKycApplication_Exception() throws Exception {
        when(kycApplicationServiceImpl.saveKycApplicationData(any(KycApplicationDTO.class)))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/crime-reporting-system/kyc-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kycApplicationDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while saving KYC application."));
    }

    // Test for saveKycApplicationImages
    @Test
    void testSaveKycApplicationImages_Success() throws Exception {
    	MockMultipartFile verificationImage = new MockMultipartFile("userVerificationImage", "file.jpg", "image/jpeg", "test".getBytes());
    	MockMultipartFile aadhaarFile = new MockMultipartFile("userAdhaarFile", "aadhaar.pdf", "application/pdf", "test".getBytes());

        ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(kycApplicationDTO);

        when(kycApplicationServiceImpl.saveKycApplicationImages(anyInt(), any(MultipartFile.class), any(MultipartFile.class)))
                .thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/kyc-application/saveKycApplicationImages/1") 
                .file(verificationImage)
                .file(aadhaarFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kycId").value(1))
                .andExpect(jsonPath("$.userProfile.profileId").value(1001))
                .andExpect(jsonPath("$.kycVerificationStatus").value("underVerification"));
    }

    @Test
    void testSaveKycApplicationImages_Failure() throws Exception {
    	MockMultipartFile verificationImage = new MockMultipartFile("userVerificationImage", "file.jpg", "image/jpeg", "test".getBytes());
    	MockMultipartFile aadhaarFile = new MockMultipartFile("userAdhaarFile", "aadhaar.pdf", "application/pdf", "test".getBytes());

        ApiGenericResponse<KycApplicationDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("Failed");

        when(kycApplicationServiceImpl.saveKycApplicationImages(anyInt(), any(MultipartFile.class), any(MultipartFile.class)))
                .thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/kyc-application/saveKycApplicationImages/1") 
                .file(verificationImage)
                .file(aadhaarFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Failed"));
    }

    @Test
    void testSaveKycApplicationImages_Exception() throws Exception {
        MockMultipartFile verificationImage = new MockMultipartFile("userVerificationImage", "file.jpg", "image/jpeg", "test".getBytes());
        MockMultipartFile aadhaarFile = new MockMultipartFile("userAdhaarFile", "aadhaar.pdf", "application/pdf", "test".getBytes());

        when(kycApplicationServiceImpl.saveKycApplicationImages(anyInt(), any(MultipartFile.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Error"));

        
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/kyc-application/saveKycApplicationImages/1") 
                .file(verificationImage)
                .file(aadhaarFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while saving KYC images."));
    }

    // Test for updateKycStatus
    @Test
    void testUpdateKycStatus_Success() throws Exception {
    	KycApplicationDTO updateDTO = KycApplicationDTO.builder()
    		    .KycId(1)
    		    .kycVerificationStatus(KycVerificationStatus.verified)
    		    .build();


        when(kycApplicationServiceImpl.updateKycStatus(1, KycVerificationStatus.verified)).thenReturn(true);

        mockMvc.perform(put("/crime-reporting-system/kyc-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("KYC status updated successfully"));
    }

    @Test
    void testUpdateKycStatus_Failure() throws Exception {
    	KycApplicationDTO updateDTO = KycApplicationDTO.builder()
    		    .KycId(1)
    		    .kycVerificationStatus(KycVerificationStatus.verified)
    		    .build();


        when(kycApplicationServiceImpl.updateKycStatus(1, KycVerificationStatus.verified)).thenReturn(false);

        mockMvc.perform(put("/crime-reporting-system/kyc-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Status updation failed"));
    }

    @Test
    void testUpdateKycStatus_Exception() throws Exception {
    	KycApplicationDTO updateDTO = KycApplicationDTO.builder()
    		    .KycId(1)
    		    .kycVerificationStatus(KycVerificationStatus.verified)
    		    .build();


        when(kycApplicationServiceImpl.updateKycStatus(1,KycVerificationStatus.verified))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/crime-reporting-system/kyc-application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while updating KYC status."));
    }
}
