package com.endava.CrimeReportingSystem.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.EmergencyDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.EmergencyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class EmergencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmergencyServiceImpl emergencyServiceImpl;

    @InjectMocks
    private EmergencyController emergencyController;

    private ObjectMapper objectMapper;
    

    private  EmergencyDTO emergencyDTO;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(emergencyController).build();
        emergencyDTO= new EmergencyDTO(0, "", "", null, null, null);
        
       
 
    }

    @Test
    void testAddEmergencyNumber_Success() throws Exception {
        
       

        ApiGenericResponse<EmergencyDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(emergencyDTO);
        response.setMessage("Successfully added");
        when(emergencyServiceImpl.addEmergencyNumber(any(EmergencyDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/emergency")
                .content(objectMapper.writeValueAsString(emergencyDTO))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testAddEmergencyNumber_Failure() throws Exception {
       
        

        ApiGenericResponse<EmergencyDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to add");
        when(emergencyServiceImpl.addEmergencyNumber(any(EmergencyDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/emergency")
                .content(objectMapper.writeValueAsString(emergencyDTO))
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to add"));
    }

    @Test
    void testFetchAllEmergencyNumbers_Success() throws Exception {
        List<EmergencyDTO> emergencyList = Arrays.asList( new EmergencyDTO(0, "", "", null, null, null), 
                new EmergencyDTO(0, "", "", null, null, null));
        when(emergencyServiceImpl.fetchAllEmergencyNumbers()).thenReturn(emergencyList);

        mockMvc.perform(get("/crime-reporting-system/emergency")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(emergencyList)));
    }

    @Test
    void testFetchAllEmergencyNumbers_NotFound() throws Exception {
        when(emergencyServiceImpl.fetchAllEmergencyNumbers()).thenReturn(List.of());

        mockMvc.perform(get("/crime-reporting-system/emergency")
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No emergency numbers found"));
    }

    @Test
    void testUpdateEmergencyNumber_Success() throws Exception {
        

        ApiGenericResponse<EmergencyDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(emergencyDTO);
        response.setMessage("Successfully updated");
        when(emergencyServiceImpl.updateEmergencyNumber(any(EmergencyDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/emergency")
                .content(objectMapper.writeValueAsString(emergencyDTO))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testUpdateEmergencyNumber_Failure() throws Exception {
    

        ApiGenericResponse<EmergencyDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to update");
        when(emergencyServiceImpl.updateEmergencyNumber(any(EmergencyDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/emergency")
                .content(objectMapper.writeValueAsString(emergencyDTO))
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to update"));
    }

    @Test
    void testDeleteEmergencyNumber_Success() throws Exception {
        int id = 1;
        when(emergencyServiceImpl.deleteEmergencyNumber(id)).thenReturn(true);

        mockMvc.perform(delete("/crime-reporting-system/emergency/delete/{id}", id)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Emergency number deleted successfully"));
    }

    @Test
    void testDeleteEmergencyNumber_Failure() throws Exception {
        int id = 1;
        when(emergencyServiceImpl.deleteEmergencyNumber(id)).thenReturn(false);

        mockMvc.perform(delete("/crime-reporting-system/emergency/delete/{id}", id)
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to delete emergency number"));
    }
    
    @Test
    void testAddEmergencyNumber_Exception() throws Exception {
        when(emergencyServiceImpl.addEmergencyNumber(any(EmergencyDTO.class))).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(post("/crime-reporting-system/emergency")
                .content(objectMapper.writeValueAsString(emergencyDTO))
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while adding emergency number."));
    }
    
    @Test
    void testFetchAllEmergencyNumbers_Exception() throws Exception {
        when(emergencyServiceImpl.fetchAllEmergencyNumbers()).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(get("/crime-reporting-system/emergency")
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching emergency numbers."));
    }
    
    @Test
    void testUpdateEmergencyNumber_Exception() throws Exception {
        when(emergencyServiceImpl.updateEmergencyNumber(any(EmergencyDTO.class))).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(put("/crime-reporting-system/emergency")
                .content(objectMapper.writeValueAsString(emergencyDTO))
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while updating emergency number."));
    }
    
    @Test
    void testDeleteEmergencyNumber_Exception() throws Exception {
        int id = 1;
        when(emergencyServiceImpl.deleteEmergencyNumber(id)).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(delete("/crime-reporting-system/emergency/delete/{id}", id)
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while deleting emergency number."));
    }
}
