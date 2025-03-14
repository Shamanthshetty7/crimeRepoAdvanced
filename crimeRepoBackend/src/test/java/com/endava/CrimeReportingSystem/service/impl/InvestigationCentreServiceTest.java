package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.CrimeReportingSystem.entity.InvestigationCentre;
import com.endava.CrimeReportingSystem.entity.dto.InvestigationCentreDTO;
import com.endava.CrimeReportingSystem.mapper.InvestigationCentreMapper;
import com.endava.CrimeReportingSystem.repository.InvestigationCentreRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class InvestigationCentreServiceTest {

    @Mock
    private InvestigationCentreMapper investigationCentreMapper;

    @Mock
    private InvestigationCentreRepository investigationCentreRepository;

    @InjectMocks
    private InvestigationCentreServiceImpl investigationCentreService;

    private InvestigationCentre investigationCentre;
    private InvestigationCentreDTO investigationCentreDTO;

    @BeforeEach
    void setUp() {
        investigationCentre = new InvestigationCentre();
        investigationCentre.setInvestigationCentreId(1);
        investigationCentre.setInvestigationCentreCode("INV001");
        investigationCentre.setCreatedAt(LocalDateTime.now());

        investigationCentreDTO = new InvestigationCentreDTO(1,null,"INV001",null);
       
    }

    @Test
    void testGetInvestigationCentreByInvCode_Success() {
        when(investigationCentreRepository.getByInvestigationCentreCode(anyString())).thenReturn(investigationCentre);
        when(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(any(InvestigationCentre.class)))
            .thenReturn(investigationCentreDTO);

        ApiGenericResponse<InvestigationCentreDTO> response = investigationCentreService.getInvestigationCentreByInvCode("INV001");

        assertNotNull(response.getData());
        assertEquals("INV001", response.getData().InvestigationCentreCode());
    }

    @Test
    void testGetInvestigationCentreByInvCode_Failure() {
        when(investigationCentreRepository.getByInvestigationCentreCode(anyString())).thenReturn(null);

        ApiGenericResponse<InvestigationCentreDTO> response = investigationCentreService.getInvestigationCentreByInvCode("INV001");

        assertNull(response.getData());
        assertEquals("Investigation Centre doesn't exist", response.getMessage());
    }

    @Test
    void testGetAllInvestigationCentre_Success() {
        List<InvestigationCentre> investigationCentreList = new ArrayList<>();
        investigationCentreList.add(investigationCentre);

        when(investigationCentreRepository.findAll()).thenReturn(investigationCentreList);
        when(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(any(InvestigationCentre.class)))
            .thenReturn(investigationCentreDTO);

        List<InvestigationCentreDTO> response = investigationCentreService.getAllInvestigationCentre();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("INV001", response.get(0).InvestigationCentreCode());
    }

    @Test
    void testSaveInvestigationCentre_Success() {
        when(investigationCentreMapper.InvestigationCentreDTOtoInvestigationCentre(any(InvestigationCentreDTO.class)))
            .thenReturn(investigationCentre);
        when(investigationCentreRepository.save(any(InvestigationCentre.class))).thenReturn(investigationCentre);
        when(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(any(InvestigationCentre.class)))
            .thenReturn(investigationCentreDTO);

        ApiGenericResponse<InvestigationCentreDTO> response = investigationCentreService.saveInvestigationCentre(investigationCentreDTO);

        assertNotNull(response.getData());
        assertEquals("INV001", response.getData().InvestigationCentreCode());
        assertEquals("Investigation Centre Added Successfully", response.getMessage());
    }

    @Test
    void testUpdateInvestigationCentre_Success() {
        when(investigationCentreMapper.InvestigationCentreDTOtoInvestigationCentre(any(InvestigationCentreDTO.class)))
            .thenReturn(investigationCentre);
        when(investigationCentreRepository.save(any(InvestigationCentre.class))).thenReturn(investigationCentre);
        when(investigationCentreMapper.investigationCentreToInvestigationCentreDTO(any(InvestigationCentre.class)))
            .thenReturn(investigationCentreDTO);

        ApiGenericResponse<InvestigationCentreDTO> response = investigationCentreService.updateInvestigationCentre(investigationCentreDTO);

        assertNotNull(response.getData());
        assertEquals("INV001", response.getData().InvestigationCentreCode());
        assertEquals("Investigation Centre Updated Successfully", response.getMessage());
    }

    @Test
    void testDeleteInvestigationCentre_Success() {
        when(investigationCentreRepository.findById(anyInt())).thenReturn(Optional.of(investigationCentre));

        Boolean response = investigationCentreService.deleteInvestigationCentre(1);

        assertEquals(true, response);
    }

    @Test
    void testDeleteInvestigationCentre_Failure() {
        when(investigationCentreRepository.findById(anyInt())).thenReturn(Optional.empty());

        Boolean response = investigationCentreService.deleteInvestigationCentre(1);

        assertEquals(false, response);
    }
}
