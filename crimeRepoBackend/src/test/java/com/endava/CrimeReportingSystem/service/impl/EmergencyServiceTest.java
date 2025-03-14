package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.CrimeReportingSystem.constants.EmergencyConstants;
import com.endava.CrimeReportingSystem.entity.Emergency;
import com.endava.CrimeReportingSystem.entity.dto.EmergencyDTO;
import com.endava.CrimeReportingSystem.mapper.EmergencyMapper;
import com.endava.CrimeReportingSystem.repository.EmergencyRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class EmergencyServiceTest {

    @Mock
    private EmergencyRepository emergencyRepository;

    @Mock
    private EmergencyMapper emergencyMapper;

    @InjectMocks
    private EmergencyServiceImpl emergencyService;

    private Emergency emergency;
    private EmergencyDTO emergencyDTO;

    @BeforeEach
    void setUp() {
        emergency = new Emergency();
        emergency.setEmergencyId(1);
        emergency.setEmergencyContactNumber("123456");
        emergency.setCreatedAt(LocalDateTime.now());

        emergencyDTO = new EmergencyDTO(1,null,"123456",null,null,null);
      
    }

    @Test
    void testAddEmergencyNumber_Success() {
        when(emergencyMapper.emergencyDTOToEmergency(any(EmergencyDTO.class))).thenReturn(emergency);
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(emergency);
        when(emergencyMapper.emergencyToEmergencyDTO(any(Emergency.class))).thenReturn(emergencyDTO);

        ApiGenericResponse<EmergencyDTO> response = emergencyService.addEmergencyNumber(emergencyDTO);

        assertNotNull(response.getData());
        assertEquals("123456", response.getData().emergencyContactNumber());
        assertEquals("Emergency number added successfully", response.getMessage());
    }

   

    @Test
    void testFetchAllEmergencyNumbers_Success() {
        List<Emergency> emergencies = new ArrayList<>();
        emergencies.add(emergency);

        when(emergencyRepository.findAll()).thenReturn(emergencies);
        when(emergencyMapper.emergencyToEmergencyDTO(any(Emergency.class))).thenReturn(emergencyDTO);

        List<EmergencyDTO> response = emergencyService.fetchAllEmergencyNumbers();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("123456", response.get(0).emergencyContactNumber());
    }

    @Test
    void testUpdateEmergencyNumber_Success() {
        when(emergencyMapper.emergencyDTOToEmergency(any(EmergencyDTO.class))).thenReturn(emergency);
        when(emergencyRepository.save(any(Emergency.class))).thenReturn(emergency);
        when(emergencyMapper.emergencyToEmergencyDTO(any(Emergency.class))).thenReturn(emergencyDTO);

        ApiGenericResponse<EmergencyDTO> response = emergencyService.updateEmergencyNumber(emergencyDTO);

        assertNotNull(response.getData());
        assertEquals("123456", response.getData().emergencyContactNumber());
        assertEquals("Emergency number updated successfully", response.getMessage());
    }

    @Test
    void testUpdateEmergency_Failure() {
        when(emergencyMapper.emergencyDTOToEmergency(any(EmergencyDTO.class))).thenReturn(emergency);
        when(emergencyRepository.save(any(Emergency.class))).thenThrow(new RuntimeException());

        ApiGenericResponse<EmergencyDTO> response = emergencyService.updateEmergencyNumber(emergencyDTO);

        assertNotNull(response);
        assertNull(response.getData());
        assertEquals(EmergencyConstants.ERROR_FAILED_TO_UPDATE.getMessage(), response.getMessage());
    }

    @Test
    void testDeleteEmergencyNumber_Success() {
        Boolean response = emergencyService.deleteEmergencyNumber(1);
        assertEquals(true, response);
    }

    @Test
    void testDeleteEmergencyNumber_Failure() {
        doThrow(new RuntimeException()).when(emergencyRepository).deleteById(anyInt());

        Boolean response = emergencyService.deleteEmergencyNumber(1);
        assertEquals(false, response);
    }
}
