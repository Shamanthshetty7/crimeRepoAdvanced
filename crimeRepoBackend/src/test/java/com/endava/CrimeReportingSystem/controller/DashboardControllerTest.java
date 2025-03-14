package com.endava.CrimeReportingSystem.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.DashboardServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DashboardServiceImpl dashboardServiceImpl;

    @InjectMocks
    private DashboardController dashboardController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    void testGetAllDataCount_Success() throws Exception {
        Map<String, Integer> dataCounts = new HashMap<>();
        dataCounts.put("activeUsers", 10);
        dataCounts.put("blockedUser", 2);
        ApiGenericResponse<Map<String, Integer>> response = new ApiGenericResponse<>(null, null);
        response.setData(dataCounts);
        when(dashboardServiceImpl.getAllDatCount()).thenReturn((ApiGenericResponse) response);

        mockMvc.perform(get("/crime-reporting-system/dashboard/allDataCounts")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testGetAllDataCount_NotFound() throws Exception {
        ApiGenericResponse<Map<String, Integer>> response = new ApiGenericResponse<>(null,null);
        response.setMessage("No data found");
        when(dashboardServiceImpl.getAllDatCount()).thenReturn((ApiGenericResponse) response);

        mockMvc.perform(get("/crime-reporting-system/dashboard/allDataCounts")
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No data found for dashboard"));
    }

    @Test
    void testGetAllDataCount_Exception() throws Exception {
        when(dashboardServiceImpl.getAllDatCount()).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(get("/crime-reporting-system/dashboard/allDataCounts")
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching dashboard data."));
    }

    @Test
    void testGetAllReportByMonth_Success() throws Exception {
        int year = 2023;
        Map<String, Long> monthlyReportCounts = new HashMap<>();
        monthlyReportCounts.put("JANUARY", 5L);
        monthlyReportCounts.put("FEBRUARY", 10L);
        ApiGenericResponse<Map<String, Long>> response = new ApiGenericResponse<>(null, null);
        response.setData(monthlyReportCounts);
        when(dashboardServiceImpl.getAllReportByMonth(year)).thenReturn((ApiGenericResponse) response);

        mockMvc.perform(get("/crime-reporting-system/dashboard/getReportsByMonth/{year}", year)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response.getData())));
    }

    @Test
    void testGetAllReportByMonth_NotFound() throws Exception {
        int year = 2023;
        ApiGenericResponse<Map<String, Long>> response = new ApiGenericResponse<>(null, null);
        response.setMessage("No report data found");
        when(dashboardServiceImpl.getAllReportByMonth(year)).thenReturn((ApiGenericResponse) response);

        mockMvc.perform(get("/crime-reporting-system/dashboard/getReportsByMonth/{year}", year)
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No report data found"));
    }

    @Test
    void testGetAllReportByMonth_Exception() throws Exception {
        int year = 2023;
        when(dashboardServiceImpl.getAllReportByMonth(year)).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(get("/crime-reporting-system/dashboard/getReportsByMonth/{year}", year)
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching report data."));
    }
}
