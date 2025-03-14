package com.endava.CrimeReportingSystem.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.entity.dto.InvestigationCentreDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.InvestigationCentreServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class InvestigationCentreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvestigationCentreServiceImpl investigationCentreServiceImpl;

    @InjectMocks
    private InvestigationCentreController investigationCentreController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(investigationCentreController).build();
    }

    @Test
    void testGetInvestigationCentreById_Success() throws Exception {
        String investigationCentreCode = "INV123";
        InvestigationCentreDTO investigationCentreDTO = new InvestigationCentreDTO(0,"Crime Investigation Center",investigationCentreCode,null);
        
        
        ApiGenericResponse<InvestigationCentreDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(investigationCentreDTO);

       
        when(investigationCentreServiceImpl.getInvestigationCentreByInvCode(investigationCentreCode)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/investigation-centre/{investigationCentreCode}", investigationCentreCode))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(investigationCentreDTO)));
    }

    @Test
    void testGetInvestigationCentreById_NotFound() throws Exception {
        String investigationCentreCode = "INV999";
        
        ApiGenericResponse<InvestigationCentreDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("Investigation Centre not found");

        // Mock service method
        when(investigationCentreServiceImpl.getInvestigationCentreByInvCode(investigationCentreCode)).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/investigation-centre/{investigationCentreCode}", investigationCentreCode))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Investigation Centre not found"));
    }

    @Test
    void testGetInvestigationCentreById_Exception() throws Exception {
        String investigationCentreCode = "INV123";

       
        when(investigationCentreServiceImpl.getInvestigationCentreByInvCode(investigationCentreCode))
                .thenThrow(new RuntimeException("Service exception"));

        mockMvc.perform(get("/crime-reporting-system/investigation-centre/{investigationCentreCode}", investigationCentreCode))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching data."));
    }
}
