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

import java.util.ArrayList;
import java.util.List;

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

import com.endava.CrimeReportingSystem.constants.ReportsConstants;
import com.endava.CrimeReportingSystem.entity.dto.ReportsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.ReportsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ReportsServiceImpl reportsServiceImpl;

    @InjectMocks
    private ReportsController reportsController;

    private ObjectMapper objectMapper;
    private ReportsDTO reportsDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(reportsController).build();

         reportsDTO = ReportsDTO.builder()
        	    .reportId(1)
        	    .reportTitle("Test Report")
        	    .reportLocation("New York")
        	    .build();

    }

    // Test for getAllReports
    @Test
    void testGetAllReports_Success() throws Exception {
        List<ReportsDTO> reportsList = new ArrayList<>();
        reportsList.add(reportsDTO);

        when(reportsServiceImpl.getAllReports()).thenReturn(reportsList);

        mockMvc.perform(get("/crime-reporting-system/reports/getAllReports")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].reportId").value(1))
                .andExpect(jsonPath("$[0].reportTitle").value("Test Report"))
                .andExpect(jsonPath("$[0].reportLocation").value("New York"));
    }

    @Test
    void testGetAllReports_NotFound() throws Exception {
        when(reportsServiceImpl.getAllReports()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/crime-reporting-system/reports/getAllReports")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Reports not available"));
    }

    @Test
    void testGetAllReports_InternalServerError() throws Exception {
        when(reportsServiceImpl.getAllReports()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/reports/getAllReports")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching reports."));
    }

    // Test for getReportById
    @Test
    void testGetReportById_Success() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(reportsDTO);
        when(reportsServiceImpl.getReportById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/reports/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1))
                .andExpect(jsonPath("$.reportTitle").value("Test Report"))
                .andExpect(jsonPath("$.reportLocation").value("New York"));
    }

    @Test
    void testGetReportById_NotFound() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage( "Report not found");
        when(reportsServiceImpl.getReportById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/reports/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Report not found"));
    }

    @Test
    void testGetReportById_InternalServerError() throws Exception {
        when(reportsServiceImpl.getReportById(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/reports/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(ReportsConstants.ERROR_FETCHING_REPORTS.getMessage()));
    }

    // Test for saveReport
    @Test
    void testSaveReport_Success() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(reportsDTO);
        when(reportsServiceImpl.saveReport(any(ReportsDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1))
                .andExpect(jsonPath("$.reportTitle").value("Test Report"))
                .andExpect(jsonPath("$.reportLocation").value("New York"));
    }

    @Test
    void testSaveReport_Failure() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to save report");
        when(reportsServiceImpl.saveReport(any(ReportsDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to save report"));
    }

    @Test
    void testSaveReport_InternalServerError() throws Exception {
        when(reportsServiceImpl.saveReport(any(ReportsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/crime-reporting-system/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while saving the report."));
    }
    
 // Test for saveReportImage
    @Test
    void testSaveReportImage_Success() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("reportImage", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(reportsDTO);
        when(reportsServiceImpl.saveReportImage(anyInt(), any())).thenReturn(response);
        

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/reports/saveReportImage/1") 
                .file(imageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1))
                .andExpect(jsonPath("$.reportTitle").value("Test Report"))
                .andExpect(jsonPath("$.reportLocation").value("New York"));
    }

    @Test
    void testSaveReportImage_Failure() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("reportImage", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to save image");
        when(reportsServiceImpl.saveReportImage(anyInt(), any())).thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/reports/saveReportImage/1") 
                .file(imageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to save image"));
    }

    @Test
    void testSaveReportImage_InternalServerError() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile("reportImage", "image.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        when(reportsServiceImpl.saveReportImage(anyInt(), any())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/reports/saveReportImage/1") 
                .file(imageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(ReportsConstants.ERROR_SAVING_REPORT_IMAGE.getMessage()));
    }

    // Test for updateReport
    @Test
    void testUpdateReport_Success() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
       response.setData(reportsDTO);
        when(reportsServiceImpl.updateReport(any(ReportsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1))
                .andExpect(jsonPath("$.reportTitle").value("Test Report"))
                .andExpect(jsonPath("$.reportLocation").value("New York"));
    }

    @Test
    void testUpdateReport_Failure() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to update report");
        when(reportsServiceImpl.updateReport(any(ReportsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to update report"));
    }

    @Test
    void testUpdateReport_InternalServerError() throws Exception {
        when(reportsServiceImpl.updateReport(any(ReportsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(put("/crime-reporting-system/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while updating the report."));
    }

    // Test for updateReportVote
    @Test
    void testUpdateReportVote_Success() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(reportsDTO);
        when(reportsServiceImpl.updateReportVote(anyInt(), any(ReportsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/reports/vote/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1))
                .andExpect(jsonPath("$.reportTitle").value("Test Report"))
                .andExpect(jsonPath("$.reportLocation").value("New York"));
    }

    @Test
    void testUpdateReportVote_Failure() throws Exception {
        ApiGenericResponse<ReportsDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to update vote");
        when(reportsServiceImpl.updateReportVote(anyInt(), any(ReportsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/reports/vote/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to update vote"));
    }

    @Test
    void testUpdateReportVote_InternalServerError() throws Exception {
        when(reportsServiceImpl.updateReportVote(anyInt(), any(ReportsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(put("/crime-reporting-system/reports/vote/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportsDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value(ReportsConstants.ERROR_UPDATING_REPORT_VOTE.getMessage()));
    }
    
 // Test for deleteReport (Success Case)
    @Test
    void testDeleteReport_Success() throws Exception {
        when(reportsServiceImpl.deleteReport(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/crime-reporting-system/reports/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    // Test for deleteReport (Report Not Found)
    @Test
    void testDeleteReport_ReportNotFound() throws Exception {
        when(reportsServiceImpl.deleteReport(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/crime-reporting-system/reports/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(false));
    }

    // Test for deleteReport (Internal Server Error)
    @Test
    void testDeleteReport_InternalServerError() throws Exception {
        when(reportsServiceImpl.deleteReport(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(delete("/crime-reporting-system/reports/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while deleting the report."));
    }
}
