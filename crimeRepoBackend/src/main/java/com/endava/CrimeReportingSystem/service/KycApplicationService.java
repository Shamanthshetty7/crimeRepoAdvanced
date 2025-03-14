package com.endava.CrimeReportingSystem.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.entity.dto.KycApplicationDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;



public interface KycApplicationService {
	public List<KycApplicationDTO> getAllKycApplications();
	public ApiGenericResponse<KycApplicationDTO> getKycApplicationByProfileId(int profileId);
	public ApiGenericResponse<KycApplicationDTO> saveKycApplicationData(KycApplicationDTO kycApplicationDTO);
	public ApiGenericResponse<KycApplicationDTO> saveKycApplicationImages(int kycApplicationId,MultipartFile kycImage, MultipartFile adhaarFile) throws IOException;

	
	public Boolean updateKycStatus(int kycId,KycVerificationStatus kycStatus);
}
