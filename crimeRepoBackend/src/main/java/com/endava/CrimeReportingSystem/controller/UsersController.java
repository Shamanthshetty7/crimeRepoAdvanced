package com.endava.CrimeReportingSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.UsersConstants;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.UsersServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(UsersConstants.BASE_USER_URL)
@Slf4j  
public class UsersController {

	private final UsersServiceImpl usersServiceImpl;

    // Constructor injection
    public UsersController(UsersServiceImpl usersServiceImpl) {
        this.usersServiceImpl = usersServiceImpl;
    }
    
    /**
     * End point to fetch all users.
     * @return A list of all users or an error message if no users found
     */
    @GetMapping(path=UsersConstants.GET_ALL_USERS_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllUsers() {
        log.debug("UsersController::getAllUsers() - Entering method");
        
        try {
            List<UsersDTO> users = usersServiceImpl.getAllUsers();
            
            if (!users.isEmpty()) {
                log.debug("UsersController::getAllUsers() - Found {} users", users.size());
                return new ResponseEntity<>(users, HttpStatus.OK);
            } else {
                log.debug("UsersController::getAllUsers() - Users not available");
                return new ResponseEntity<>(UsersConstants.ERROR_NO_USERS_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UsersController::getAllUsers() - Error occurred while fetching users. Error: {}", e.getMessage());
            return new ResponseEntity<>(UsersConstants.ERROR_FETCHING_USERS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * End point to fetch a specific user by ID.
     * @param userId The ID of the user to fetch
     * @return The user data or an error message if the user is not found
     */
    @GetMapping(path=UsersConstants.GET_DELETE_USER_BY_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getUserById(@PathVariable int userId) {
        log.debug("UsersController::getUserById() - Entering method with userId: {}", userId);
        
        try {
            ApiGenericResponse<UsersDTO> user = usersServiceImpl.getUserById(userId);
            
            if (user.getData() != null) {
                log.debug("UsersController::getUserById() - Found user: {}", user.getData());
                return new ResponseEntity<>(user.getData(), HttpStatus.OK);
            } else {
                log.debug("UsersController::getUserById() - User not found with id: {}", userId);
                return new ResponseEntity<>(user.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UsersController::getUserById() - Error occurred while fetching user by ID: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(UsersConstants.ERROR_FETCHING_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * End point to check if user login credentials are valid.
     * @param usersDTO The user login data
     * @return User data if login is successful or an error message if invalid login
     */
    @PostMapping(path=UsersConstants.USER_LOGIN_PATH, consumes = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> checkUserLogin(@RequestBody UsersDTO usersDTO) {
        log.debug("UsersController::checkUserLogin() - Entering method with usersDTO: {}", usersDTO);
        
        try {
            ApiGenericResponse<UsersDTO> user = usersServiceImpl.checkUserLogin(usersDTO);
            
            if (user.getData() != null) {
                log.debug("UsersController::checkUserLogin() - User login successful for: {}", usersDTO);
                return new ResponseEntity<>(user.getData(), HttpStatus.OK);
            } else {
                log.debug("UsersController::checkUserLogin() - Invalid login for user: {}", usersDTO);
                return new ResponseEntity<>(user.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UsersController::checkUserLogin() - Error occurred while checking user login. Error: {}", e.getMessage());
            return new ResponseEntity<>(UsersConstants.ERROR_CHECKING_USER_LOGIN.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to register a new user.
     * @param usersDTO The new user data
     * @return The saved user data or an error message if registration fails
     */
    @PostMapping(path=UsersConstants.USER_REGISTER_PATH,consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveUser(@RequestBody UsersDTO usersDTO) {
        log.debug("UsersController::saveUser() - Entering method with usersDTO: {}", usersDTO);
        
        try {
            ApiGenericResponse<UsersDTO> addedUser = usersServiceImpl.saveUser(usersDTO);
            
            if (addedUser.getData() != null) {
                log.debug("UsersController::saveUser() - User added successfully: {}", addedUser.getData());
                return new ResponseEntity<>(addedUser.getData(), HttpStatus.OK);
            } else {
                log.error("UsersController::saveUser() - Failed to add user. Error message: {}", addedUser.getMessage());
                return new ResponseEntity<>(addedUser.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("UsersController::saveUser() - Error occurred while saving user. Error: {}", e.getMessage());
            return new ResponseEntity<>(UsersConstants.ERROR_SAVING_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * End point to update an existing user.
     * @param usersDTO The user data to update
     * @return The updated user data or an error message if update fails
     */
    @PutMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateUser(@RequestBody UsersDTO usersDTO) {
        log.debug("UsersController::updateUser() - Entering method with usersDTO: {}", usersDTO);
        
        try {
            ApiGenericResponse<UsersDTO> updatedUser = usersServiceImpl.updateUser(usersDTO);
            
            if (updatedUser.getData() != null) {
                log.debug("UsersController::updateUser() - User updated successfully: {}", updatedUser.getData());
                return new ResponseEntity<>(updatedUser.getData(), HttpStatus.OK);
            } else {
                log.error("UsersController::updateUser() - Failed to update user. Error message: {}", updatedUser.getMessage());
                return new ResponseEntity<>(updatedUser.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UsersController::updateUser() - Error occurred while updating user. Error: {}", e.getMessage());
            return new ResponseEntity<>(UsersConstants.ERROR_UPDATING_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * End point to delete a user by ID.
     * @param userId The ID of the user to delete
     * @return A message indicating whether the user was deleted successfully or not
     */
    @DeleteMapping(UsersConstants.GET_DELETE_USER_BY_ID_PATH)
    public ResponseEntity<?> deleteUser(@PathVariable int userId) {
        log.debug("UsersController::deleteUser() - Entering method with userId: {}", userId);
        
        try {
            Boolean isDeleted = usersServiceImpl.deleteUser(userId);
            
            if (isDeleted) {
                log.debug("UsersController::deleteUser() - Successfully deleted user with id: {}", userId);
                return new ResponseEntity<>(UsersConstants.SUCCESS_USER_DELETED.getMessage(), HttpStatus.OK);
            } else {
                log.debug("UsersController::deleteUser() - User not found with id: {}", userId);
                return new ResponseEntity<>(UsersConstants.ERROR_USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("UsersController::deleteUser() - Error occurred while deleting user with id: {}. Error: {}", userId, e.getMessage());
            return new ResponseEntity<>(UsersConstants.ERROR_DELETING_USER.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
