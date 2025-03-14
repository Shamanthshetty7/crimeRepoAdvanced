package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(1);
        user.setUserName("Shamanth");
        user.setUserEmail("shamanth@example.com");
        user.setUserPassword("password");
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(usersRepository.selectUserByUserEmail(anyString())).thenReturn(user);

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("shamanth@example.com");

        assertNotNull(userDetails);
        assertEquals("shamanth@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(usersRepository.selectUserByUserEmail(anyString())).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("unknown@example.com");
        });

        assertEquals("user not found", exception.getMessage());
    }
}
