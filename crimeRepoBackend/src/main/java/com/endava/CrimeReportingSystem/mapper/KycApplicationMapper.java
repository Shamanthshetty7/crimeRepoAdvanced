package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.KycApplication;
import com.endava.CrimeReportingSystem.entity.dto.KycApplicationDTO;

@Component
public class KycApplicationMapper {

    // Mapping from KycApplication entity to KycApplicationDTO
    public KycApplicationDTO kycApplicationToKycApplicationDTO(KycApplication kycApplication) {
    	return  KycApplicationDTO.builder()
    		    .KycId(kycApplication.getKycId())
    		    .userFullName(kycApplication.getUserFullName())
    		    .userDOB(kycApplication.getUserDOB())
    		    .createdAt(kycApplication.getCreatedAt())
    		    .updatedAt(kycApplication.getUpdatedAt())
    		    .kycVerificationStatus(kycApplication.getKycVerificationStatus())
    		    .currentCity(kycApplication.getCurrentCity())
    		    .fecthedUserLocation(kycApplication.getFecthedUserLocation())
    		    .build();

    }

    // Mapping from KycApplicationDTO to KycApplication entity
    public KycApplication kycApplicationDTOToKycApplication(KycApplicationDTO kycApplicationDTO) {
        KycApplication kycApplication = new KycApplication();
        
        kycApplication.setKycId(kycApplicationDTO.getKycId());
        kycApplication.setUserFullName(kycApplicationDTO.getUserFullName());
        kycApplication.setUserDOB(kycApplicationDTO.getUserDOB());
        kycApplication.setCreatedAt(kycApplicationDTO.getCreatedAt());
        kycApplication.setUpdatedAt(kycApplicationDTO.getUpdatedAt());
        kycApplication.setKycVerificationStatus(kycApplicationDTO.getKycVerificationStatus());
        kycApplication.setCurrentCity(kycApplicationDTO.getCurrentCity());
        kycApplication.setFecthedUserLocation(kycApplicationDTO.getFecthedUserLocation());
        
        return kycApplication;
    }
}
