package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.utility.ExternalAPIs;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ExternalApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExternalAPIs externalAPIs;

    @InjectMocks
    private ExternalApiController externalApiController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(externalApiController).build();
    }

    @Test
    void testGetInvestigationCentreById_Success() throws Exception {
        String cityName = "New York";
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setData("Investigation Centre Data");

        // Mock service method
        when(externalAPIs.getMatchedCitiesByCity(cityName)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/APIcall/{cityName}", cityName))
                .andExpect(status().isOk())
                .andExpect(content().string("Investigation Centre Data"));
    }

    @Test
    void testGetInvestigationCentreById_NotFound() throws Exception {
        String cityName = "Unknown City";
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setMessage("No matching cities found");

        // Mock service method
        when(externalAPIs.getMatchedCitiesByCity(cityName)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/APIcall/{cityName}", cityName))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No matching cities found"));
    }

   

    @Test
    void testGetCoordinatesByAddress_Success() throws Exception {
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setData("Coordinates data");

        // Mock service method
        when(externalAPIs.getCoordinatesByAddress(anyList())).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/APIcall/coordinates")
                .content(objectMapper.writeValueAsString(List.of("Address1", "Address2")))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Coordinates data"));
    }

    @Test
    void testGetCoordinatesByAddress_NotFound() throws Exception {
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setMessage("No coordinates found");

        // Mock service method
        when(externalAPIs.getCoordinatesByAddress(anyList())).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/APIcall/coordinates")
                .content(objectMapper.writeValueAsString(List.of("Address1", "Address2")))
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No coordinates found"));
    }

   

    @Test
    void testGetNearbyEmergencyServices_Success() throws Exception {
        double latitude = 40.7128;
        double longitude = -74.0060;
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setData("Nearby emergency services");

        // Mock service method
        when(externalAPIs.getNearbyEmergencyServices(latitude, longitude)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/APIcall/emergency-services/{lat}/{lon}", latitude, longitude))
                .andExpect(status().isOk())
                .andExpect(content().string("Nearby emergency services"));
    }

    @Test
    void testGetNearbyEmergencyServices_NotFound() throws Exception {
        double latitude = 40.7128;
        double longitude = -74.0060;
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setMessage("No nearby services found");

        // Mock service method
        when(externalAPIs.getNearbyEmergencyServices(latitude, longitude)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/APIcall/emergency-services/{lat}/{lon}", latitude, longitude))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No nearby services found"));
    }

   

    @Test
    void testCheckVerifiedEmail_Success() throws Exception {
        String email = "test@example.com";
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null, null);
       
        response.setData("Email is verified");

        when(externalAPIs.checkVerifiedEmail(email)).thenReturn((ApiGenericResponse) response);

        mockMvc.perform(get("/crime-reporting-system/APIcall/verifyEmail/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string("Email is verified"));
    }

    @Test
    void testCheckVerifiedEmail_Failure() throws Exception {
        String email = "test@example.com";
        ApiGenericResponse<Object> response = new ApiGenericResponse<>(null,null);
        response.setMessage("An unexpected error occurred while verifying email.");

        
        when(externalAPIs.checkVerifiedEmail(email)).thenReturn((ApiGenericResponse) response);

        mockMvc.perform(get("/crime-reporting-system/APIcall/verifyEmail/{email}", email))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while verifying email."));
    }

   
}
