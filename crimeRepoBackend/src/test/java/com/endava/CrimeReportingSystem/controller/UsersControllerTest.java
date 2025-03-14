package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.constants.UsersConstants;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.UsersServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UsersServiceImpl usersServiceImpl;

    @InjectMocks
    private UsersController usersController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Create test data
        UsersDTO user1 = new UsersDTO(1, "Shamanth", "shamanth.shetty@gmail.com", "1234567890", "password", "jwtToken", UserType.Investigator, LocalDateTime.now(), "investigationCentreCode", true, "12.9716 77.5946", null);
        UsersDTO user2 = new UsersDTO(2, "Jane Smith", "jane.smith@example.com", "0987654321", "password", "jwtToken", UserType.Informant, LocalDateTime.now(), "investigationCentreCode", true, "12.9716 77.5946", null);

        List<UsersDTO> usersList = Arrays.asList(user1, user2);

        // Mock service behavior
        when(usersServiceImpl.getAllUsers()).thenReturn(usersList);

        // Perform GET request and verify result
        mockMvc.perform(get("/crime-reporting-system/users/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userName").value("Shamanth"))
                .andExpect(jsonPath("$[1].userName").value("Jane Smith"));
    }

    @Test
    void testGetAllUsers_NotFound() throws Exception {
        // Mock service behavior
        when(usersServiceImpl.getAllUsers()).thenReturn(Collections.emptyList());

        // Perform GET request and verify result
        mockMvc.perform(get("/crime-reporting-system/users/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(UsersConstants.ERROR_NO_USERS_FOUND.getMessage()));
    }

    @Test
    void testGetUserById() throws Exception {
        UsersDTO user = new UsersDTO(1, "Shamanth", "shamanth.shetty@gmail.com", "1234567890", "password", "jwtToken", UserType.Investigator, LocalDateTime.now(), "investigationCentreCode", true, "12.9716 77.5946", null);
        ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(user);
        response.setMessage("User found");
        when(usersServiceImpl.getUserById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
    	
    	ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(null);
        response.setMessage( "User doesn't exist");
        when(usersServiceImpl.getUserById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User doesn't exist"));
    }

    @Test
    void testCheckUserLogin() throws Exception {
        UsersDTO user = new UsersDTO(1, "Shamanth", "shamanth.shetty@gmail.com", "1234567890", "password", "jwtToken", UserType.Investigator, LocalDateTime.now(), "investigationCentreCode", true, "12.9716 77.5946", null);
        ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(user);
        response.setMessage("User found");
        when(usersServiceImpl.checkUserLogin(any(UsersDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/users/userLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testCheckUserLogin_Invalid() throws Exception {
     
    	ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(null);
        response.setMessage("Invalid login");
        when(usersServiceImpl.checkUserLogin(any(UsersDTO.class))).thenReturn(response);

      
        mockMvc.perform(post("/crime-reporting-system/users/userLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UsersDTO())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Invalid login"));
    }

    @Test
    void testSaveUser() throws Exception {
     
        UsersDTO user = new UsersDTO(1, "Shamanth", "shamanth.shetty@gmail.com", "1234567890", "password", "jwtToken", UserType.Investigator, LocalDateTime.now(), "investigationCentreCode", true, "12.9716 77.5946", null);
        ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(user);
        response.setMessage("User added successfully");
        when(usersServiceImpl.saveUser(any(UsersDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/users/userRegister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }

    @Test
    void testSaveUser_Failure() throws Exception {
    	
    	ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(null);
        response.setMessage("Failed to save user");
        when(usersServiceImpl.saveUser(any(UsersDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/users/userRegister")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UsersDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Failed to save user"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UsersDTO user = new UsersDTO(1, "Shamanth", "shamanth.shetty@gmail.com", "1234567890", "password", "jwtToken", UserType.Investigator, LocalDateTime.now(), "investigationCentreCode", true, "12.9716 77.5946", null);

        ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(user);
        response.setMessage("User updated successfully");

        when(usersServiceImpl.updateUser(any(UsersDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Shamanth"));
    }
    
    @Test
    void testUpdateUser_Failure() throws Exception {
    	
    	ApiGenericResponse<UsersDTO> response = new ApiGenericResponse<>(null, null);

        response.setData(null);
        response.setMessage("Failed to update user");
        when(usersServiceImpl.updateUser(any(UsersDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UsersDTO())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to update user"));
    }

    @Test
    void testDeleteUser() throws Exception {
        when(usersServiceImpl.deleteUser(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/crime-reporting-system/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User deleted successfully"));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(usersServiceImpl.deleteUser(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/crime-reporting-system/users/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(UsersConstants.ERROR_USER_NOT_FOUND.getMessage()));
    }
}
