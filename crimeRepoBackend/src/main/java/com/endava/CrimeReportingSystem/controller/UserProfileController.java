package com.endava.CrimeReportingSystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.UserProfileConstants;
import com.endava.CrimeReportingSystem.entity.dto.UserProfileDTO;
import com.endava.CrimeReportingSystem.enums.KycVerificationStatus;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.UserProfileServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(UserProfileConstants.BASE_USER_PROFILE_URL)
@Slf4j  // Create logger automatically
public class UserProfileController {
    
	 private final UserProfileServiceImpl userProfileServiceImpl;

	    // Constructor injection
	    public UserProfileController(UserProfileServiceImpl userProfileServiceImpl) {
	        this.userProfileServiceImpl = userProfileServiceImpl;
	    }
    
    
    /**
     * End point to fetch a user profile by its ID.
     * @param userProfileId The ID of the user profile to retrieve
     * @return The user profile data or an error message if not found
     */
    @GetMapping(path= UserProfileConstants.GET_DELETE_USER_PROFILE_BY_ID_URL, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getUserProfileById(@PathVariable int userProfileId) {
        log.debug("UserProfileController::getUserProfileById() - Entering method with userProfileId: {}", userProfileId);
        
        try {
            ApiGenericResponse<UserProfileDTO> userProfile = userProfileServiceImpl.getUserProfileById(userProfileId);
            if(userProfile.getData() != null) {
                log.debug("UserProfileController::getUserProfileById() - Successfully retrieved user profile: {}", userProfile.getData());
                return new ResponseEntity<>(userProfile.getData(), HttpStatus.OK);
            } else {
                log.debug("UserProfileController::getUserProfileById() - No user profile found for id: {}", userProfileId);
                return new ResponseEntity<>(userProfile.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UserProfileController::getUserProfileById() - Error occurred while fetching user profile with id: {}. Error: {}", userProfileId, e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_FETCHING_USER_PROFILE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to fetch the KYC (Know Your Customer) verification status of a user.
     * @param userId The ID of the user whose KYC status is being fetched
     * @return The KYC status or an error message if not found
     */
    @GetMapping(path=UserProfileConstants.GET_KYC_STATUS_BY_USER_ID_URL, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getUserProfileKycStatusByUserId(@PathVariable int userId) {
        log.debug("UserProfileController::getUserProfileKycStatusByUserId() - Entering method with userId: {}", userId);
        
        try {
            ApiGenericResponse<KycVerificationStatus> kycStatus = userProfileServiceImpl.getUserProfileKycStatusByUserId(userId);
            if(kycStatus.getData() != null) {
                log.debug("UserProfileController::getUserProfileKycStatusByUserId() - Successfully retrieved KYC status for userId: {}", userId);
                return new ResponseEntity<>(kycStatus.getData(), HttpStatus.OK);
            } else {
                log.debug("UserProfileController::getUserProfileKycStatusByUserId() - No KYC status found for userId: {}", userId);
                return new ResponseEntity<>(kycStatus.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UserProfileController::getUserProfileKycStatusByUserId() - Error occurred while fetching KYC status for userId: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_FETCHING_KYC_STATUS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to fetch a user profile by user ID.
     * @param userId The ID of the user whose profile is being fetched
     * @return The user profile data or an error message if not found
     */
    @GetMapping(path=UserProfileConstants.GET_USER_PROFILE_BY_USER_ID_URL, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getUserProfileByUserId(@PathVariable int userId) {
        log.debug("UserProfileController::getUserProfileByUserId() - Entering method with userId: {}", userId);
        
        try {
            ApiGenericResponse<UserProfileDTO> userProfile = userProfileServiceImpl.getUserProfileByUserId(userId);
            if(userProfile.getData() != null) {
                log.debug("UserProfileController::getUserProfileByUserId() - Successfully retrieved user profile: {}", userProfile.getData());
                return new ResponseEntity<>(userProfile.getData(), HttpStatus.OK);
            } else {
                log.debug("UserProfileController::getUserProfileByUserId() - No user profile found for userId: {}", userId);
                return new ResponseEntity<>(userProfile.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
        	
            log.error("UserProfileController::getUserProfileByUserId() - Error occurred while fetching user profile for userId: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_FETCHING_USER_PROFILE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    /**
     * End point to save a new user profile.
     * @param userProfileDTO The user profile data to save
     * @return The saved user profile data or an error message if saving failed
     */
    @PostMapping(consumes= {CommonConstants.APPLICATION_JSON}, produces= {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        log.debug("UserProfileController::saveUserProfile() - Entering method with userProfileDTO: {}", userProfileDTO);
        
        try {
            ApiGenericResponse<UserProfileDTO> addedUserProfile = userProfileServiceImpl.saveUserProfile(userProfileDTO);
            if(addedUserProfile.getData() != null) {
                log.debug("UserProfileController::saveUserProfile() - Successfully saved user profile: {}", addedUserProfile.getData());
                return new ResponseEntity<>(addedUserProfile.getData(), HttpStatus.OK);
            } else {
                log.error("UserProfileController::saveUserProfile() - Failed to save user profile. Error message: {}", addedUserProfile.getMessage());
                return new ResponseEntity<>(addedUserProfile.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UserProfileController::saveUserProfile() - Error occurred while saving user profile. Error: {}", e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_SAVING_USER_PROFILE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to save or update a user's profile image.
     * @param userProfileId The ID of the user whose image is being saved
     * @param userProfileImage The image file to save
     * @return The updated user profile data with the image or an error message if saving failed
     */
    @PostMapping(path=UserProfileConstants.SAVE_USER_PROFILE_IMAGE_URL, consumes = {CommonConstants.MULTIPART_FILE}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveUserProfileImage(@PathVariable int userProfileId, @RequestPart("userProfileImage") MultipartFile userProfileImage) throws Exception {
        log.debug("UserProfileController::saveUserProfileImage() - Entering method with userProfileId: {}", userProfileId);
        
        try {
            ApiGenericResponse<UserProfileDTO> updatedProfiledata = userProfileServiceImpl.saveUserProfileImage(userProfileId, userProfileImage);
            if(updatedProfiledata.getData() != null) {
                log.debug("UserProfileController::saveUserProfileImage() - Successfully saved user profile image for userProfileId: {}", userProfileId);
                return new ResponseEntity<>(updatedProfiledata.getData(), HttpStatus.OK);
            } else {
                log.error("UserProfileController::saveUserProfileImage() - Failed to save user profile image. Error message: {}", updatedProfiledata.getMessage());
                return new ResponseEntity<>(updatedProfiledata.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UserProfileController::saveUserProfileImage() - Error occurred while saving user profile image for userProfileId: {}. Error: {}", userProfileId, e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_SAVING_USER_PROFILE_IMAGE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to update an existing user profile.
     * @param userProfileDTO The user profile data to update
     * @return The updated user profile or an error message if updating failed
     */
    @PutMapping(consumes= {CommonConstants.APPLICATION_JSON}, produces= {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        log.debug("UserProfileController::updateUserProfile() - Entering method with userProfileDTO: {}", userProfileDTO);
        
        try {
            ApiGenericResponse<UserProfileDTO> updatedUserProfile = userProfileServiceImpl.updateUserProfile(userProfileDTO);
            if(updatedUserProfile.getData() != null) {
                log.debug("UserProfileController::updateUserProfile() - Successfully updated user profile: {}", updatedUserProfile.getData());
                return new ResponseEntity<>(updatedUserProfile.getData(), HttpStatus.OK);
            } else {
                log.error("UserProfileController::updateUserProfile() - Failed to update user profile. Error message: {}", updatedUserProfile.getMessage());
                return new ResponseEntity<>(updatedUserProfile.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UserProfileController::updateUserProfile() - Error occurred while updating user profile. Error: {}", e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_UPDATING_USER_PROFILE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * End point to delete a user profile.
     * @param userProfileId The ID of the user profile to delete
     * @return A message indicating whether the deletion was successful or not
     */
    @DeleteMapping(UserProfileConstants.GET_DELETE_USER_PROFILE_BY_ID_URL)
    public ResponseEntity<?> deleteUser(@PathVariable int userProfileId) {
        log.debug("UserProfileController::deleteUser() - Entering method with userProfileId: {}", userProfileId);
        
        try {
            boolean isDeleted = userProfileServiceImpl.deleteUserProfile(userProfileId);
            if(isDeleted) {
                log.debug("UserProfileController::deleteUser() - Successfully deleted user profile with userProfileId: {}", userProfileId);
                return new ResponseEntity<>(UserProfileConstants.SUCCESS_USER_PROFILE_DELETED.getMessage(), HttpStatus.OK);
            } else {
                log.error("UserProfileController::deleteUser() - Failed to delete user profile with userProfileId: {}", userProfileId);
                return new ResponseEntity<>(UserProfileConstants.ERROR_PROFILE_DELETE_FAILED.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UserProfileController::deleteUser() - Error occurred while deleting user profile with userProfileId: {}. Error: {}", userProfileId, e.getMessage());
            return new ResponseEntity<>(UserProfileConstants.ERROR_DELETING_USER_PROFILE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
