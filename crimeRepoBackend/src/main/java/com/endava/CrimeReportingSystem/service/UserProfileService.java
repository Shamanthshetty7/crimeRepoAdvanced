package com.endava.CrimeReportingSystem.service;

import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface UserProfileService {

	public ApiGenericResponse<UserProfileDTO> getUserProfileById(int userProfileId);
	public ApiGenericResponse<UserProfileDTO> saveUserProfile(UserProfileDTO userProfileDTO);
	public ApiGenericResponse<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO);
	public Boolean deleteUserProfile(int userProfileId);
	public ApiGenericResponse<UserProfileDTO> getUserProfileByUserId(int userId);
	ApiGenericResponse<UserProfileDTO> saveUserProfileImage(int userProfileId, MultipartFile userProfileImage)
			throws Exception;
	
	public ApiGenericResponse<KycVerificationStatus>  getUserProfileKycStatusByUserId(int userId);

}
