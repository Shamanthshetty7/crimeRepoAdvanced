package com.endava.CrimeReportingSystem.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.UserType;

public class UsersMapperTest {

    private UsersMapper usersMapper;

    @BeforeEach
    void setUp() {
        usersMapper = new UsersMapper();
    }

    @Test
    void testUsersToUsersDTO() {
        // Create a sample Users entity
        Users user = new Users();
        user.setUserId(1);
        user.setUserName("Shamanth");
        user.setUserEmail("shamanth.shetty@gmail.com");
        user.setUserPassword("password");
        user.setUserPhoneNumber("1234567890");
        user.setUserType(UserType.Investigator);
        user.setIsActive(true);

        // Convert Users to UsersDTO
        UsersDTO usersDTO = usersMapper.usersToUsersDTO(user);

        // Verify the conversion
        assertNotNull(usersDTO);
        assertEquals(1, usersDTO.getUserId());
        assertEquals("Shamanth", usersDTO.getUserName());
        assertEquals("shamanth.shetty@gmail.com", usersDTO.getUserEmail());
        assertEquals("password", usersDTO.getUserPassword());
        assertEquals("1234567890", usersDTO.getUserPhoneNumber());
        assertEquals(UserType.Investigator, usersDTO.getUserType());
        assertEquals(true, usersDTO.getIsActive());
    }

    @Test
    void testUsersDTOToUsers() {
        // Create a sample UsersDTO
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserId(1);
        usersDTO.setUserName("Shamanth");
        usersDTO.setUserEmail("shamanth.shetty@gmail.com");
        usersDTO.setUserPassword("password");
        usersDTO.setUserPhoneNumber("1234567890");
        usersDTO.setUserType(UserType.Investigator);
        usersDTO.setIsActive(true);

        // Convert UsersDTO to Users
        Users user = usersMapper.usersDTOtoUsers(usersDTO);

        // Verify the conversion
        assertNotNull(user);
        assertEquals(1, user.getUserId());
        assertEquals("Shamanth", user.getUserName());
        assertEquals("shamanth.shetty@gmail.com", user.getUserEmail());
        assertEquals("password", user.getUserPassword());
        assertEquals("1234567890", user.getUserPhoneNumber());
        assertEquals(UserType.Investigator, user.getUserType());
        assertEquals(true, user.getIsActive());
    }
}
