package com.endava.CrimeReportingSystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.endava.CrimeReportingSystem.entity.UserProfile;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;

class UserProfileMapperTest {

    private final UserProfileMapper userProfileMapper = new UserProfileMapper();

    @Test
    void testUserProfileToUserProfileDTO_Success() {
       
        Users user = new Users();
        user.setUserId(1);
        user.setUserName("John Doe");

        UserProfile userProfile = new UserProfile();
        userProfile.setProfileId(101);
        userProfile.setUserAddress("123 Main St");
        userProfile.setUserAge(30);
        userProfile.setUserAlternativeNumber("9876543210");
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUserGender("Male");
        userProfile.setUsers(user);

     
        UserProfileDTO userProfileDTO = userProfileMapper.userProfileToUserProfileDTO(userProfile);

       
        assertEquals(userProfile.getProfileId(), userProfileDTO.getProfileId());
        assertEquals(userProfile.getUserAddress(), userProfileDTO.getUserAddress());
        assertEquals(userProfile.getUserAge(), userProfileDTO.getUserAge());
        assertEquals(userProfile.getUserAlternativeNumber(), userProfileDTO.getUserAlternativeNumber());
        assertEquals(userProfile.getCreatedAt(), userProfileDTO.getCreatedAt());
        assertEquals(userProfile.getUsers().getUserId(), userProfileDTO.getUserId());
        assertEquals(userProfile.getUsers().getUserName(), userProfileDTO.getUserName());
        assertEquals(userProfile.getUserGender(), userProfileDTO.getUserGender());
    }

    @Test
    void testUserProfileDTOtoUserProfile_Success() {
      
    	UserProfileDTO userProfileDTO = UserProfileDTO.builder()
    		    .profileId(101)
    		    .userAddress("123 Main St")
    		    .userAge(30)
    		    .userAlternativeNumber("9876543210")
    		    .createdAt(LocalDateTime.now())
    		    .userGender("Male")
    		    .build();


        UserProfile userProfile = userProfileMapper.userProfileDTOtoUserProfile(userProfileDTO);

      
        assertEquals(userProfileDTO.getProfileId(), userProfile.getProfileId());
        assertEquals(userProfileDTO.getUserAddress(), userProfile.getUserAddress());
        assertEquals(userProfileDTO.getUserAge(), userProfile.getUserAge());
        assertEquals(userProfileDTO.getUserAlternativeNumber(), userProfile.getUserAlternativeNumber());
        assertEquals(userProfileDTO.getCreatedAt(), userProfile.getCreatedAt());
        assertEquals(userProfileDTO.getUserGender(), userProfile.getUserGender());
    }

   
}
