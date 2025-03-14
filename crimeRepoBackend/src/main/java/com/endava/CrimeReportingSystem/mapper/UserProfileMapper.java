package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.UserProfile;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;

@Component
public class UserProfileMapper {

 
    public UserProfileDTO userProfileToUserProfileDTO(UserProfile userProfile) {
    	return UserProfileDTO.builder()
    		    .profileId(userProfile.getProfileId())
    		    .userAddress(userProfile.getUserAddress())
    		    .userAge(userProfile.getUserAge())
    		    .userAlternativeNumber(userProfile.getUserAlternativeNumber())
    		    .createdAt(userProfile.getCreatedAt())
    		    .userId(userProfile.getUsers().getUserId())
    		    .userName(userProfile.getUsers().getUserName())
    		    .userGender(userProfile.getUserGender())
    		    .build();

       
    }


    public UserProfile userProfileDTOtoUserProfile(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = new UserProfile();
        userProfile.setProfileId(userProfileDTO.getProfileId());  
        userProfile.setUserAddress(userProfileDTO.getUserAddress()); 
        userProfile.setUserAge(userProfileDTO.getUserAge());  
        userProfile.setUserAlternativeNumber(userProfileDTO.getUserAlternativeNumber());
        userProfile.setCreatedAt(userProfileDTO.getCreatedAt());
        userProfile.setUserGender(userProfileDTO.getUserGender());  
        return userProfile;
    }
}
