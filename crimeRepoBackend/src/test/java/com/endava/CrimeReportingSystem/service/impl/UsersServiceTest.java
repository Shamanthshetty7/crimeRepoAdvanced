package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.endava.CrimeReportingSystem.constants.UsersConstants;
import com.endava.CrimeReportingSystem.entity.InvestigationCentre;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.InvestigationCentreRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.utility.ExternalAPIs;

@ExtendWith(MockitoExtension.class)
 class UsersServiceTest {

	@Mock
	private UsersRepository usersRepository;

	@InjectMocks
	private UsersServiceImpl usersService;

	@Mock
	private UsersMapper usersMapper;

	@Mock
	private InvestigationCentreRepository investigationCentreRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@Mock
	JwtServiceImpl jwtServiceImpl;

	@Mock
	ExternalAPIs externalAPIs;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	 void testGetUserByID_RepositoryReturnEntityDTO() {
		Users user = new Users();
		user.setUserId(1);
		user.setUserName("Shamanth");
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		user.setIsActive(true);
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		
		UsersDTO userDTO = new UsersDTO();
		userDTO.setUserId(1);
		userDTO.setUserName("Shamanth");
		userDTO.setUserEmail("shamanth.shetty@gmail.com");
		userDTO.setUserPhoneNumber("1234567890");
		userDTO.setIsActive(true);
		userDTO.setCreatedAt(LocalDateTime.now());
		

		when(usersRepository.findById(1)).thenReturn(Optional.of(user));
        when(usersMapper.usersToUsersDTO(user)).thenReturn(userDTO);
		ApiGenericResponse<UsersDTO> response = usersService.getUserById(1);

		assertNotNull(response);
		assertNotNull(response.getData());
		assertEquals(1, response.getData().getUserId());
		assertEquals("Shamanth", response.getData().getUserName());
		assertEquals("shamanth.shetty@gmail.com", response.getData().getUserEmail());
		assertTrue(response.getData().getIsActive());

	}

	@Test
	 void testGetUserByID_UserNotFound() {
		when(usersRepository.findById(1)).thenReturn(Optional.empty());

		ApiGenericResponse<UsersDTO> response = usersService.getUserById(1);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals(UsersConstants.ERROR_USER_NOT_FOUND.getMessage(), response.getMessage());

	}

	@Test
	 void testGetAllUsers() {

		Users user1 = new Users();
		user1.setUserId(1);
		user1.setUserName("Shamanth");
		user1.setUserEmail("shamanth.shetty@gmail.com");
		user1.setUserType(UserType.Investigator);

		Users user2 = new Users();
		user2.setUserId(2);
		user2.setUserName("Jane Smith");
		user2.setUserEmail("jane.smith@example.com");
		user2.setUserType(UserType.Informant);
		
		UsersDTO userDto1 = new UsersDTO();
		userDto1.setUserId(1);
		userDto1.setUserName("Shamanth");
		userDto1.setUserEmail("shamanth.shetty@gmail.com");
		userDto1.setUserType(UserType.Investigator);
		
		UsersDTO userDto2 = new UsersDTO();
		userDto2.setUserId(2);
		userDto2.setUserName("Jane Smith");
		userDto2.setUserEmail("jane.smith@example.com");
		userDto2.setUserType(UserType.Informant);

		List<Users> usersList = Arrays.asList(user1, user2);

		when(usersRepository.findAll()).thenReturn(usersList);
		when(usersMapper.usersToUsersDTO(user1)).thenReturn(userDto1);
        when(usersMapper.usersToUsersDTO(user2)).thenReturn(userDto2);
        
		List<UsersDTO> result = usersService.getAllUsers();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("Shamanth", result.get(0).getUserName());
		assertEquals("Jane Smith", result.get(1).getUserName());

	}

	@Test
	 void testGetAllUsers_EmptyRepository() {
		when(usersRepository.findAll()).thenReturn(Collections.emptyList());

		List<UsersDTO> result = usersService.getAllUsers();

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	 void testSaveUser_Success() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPhoneNumber("1234567890");
		userDto.setUserPassword("password");
		userDto.setUserType(UserType.Investigator);
		userDto.setInvestigationCentreCode("ABC123");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		user.setUserPassword("password");
		user.setUserType(UserType.Investigator);

		InvestigationCentre investigationCentre = new InvestigationCentre();

		investigationCentre.setInvestigationCentreCode("ABC123");
		investigationCentre.setInvestigationCentreName("Cyber crimeRepo");

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmailOrUserPhoneNumber("shamanth.shetty@gmail.com", "1234567890"))
				.thenReturn(null);
		when(investigationCentreRepository.getByInvestigationCentreCode("ABC123")).thenReturn(investigationCentre);
		when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

		when(usersRepository.save(any(Users.class))).thenReturn(user);
		when(usersMapper.usersToUsersDTO(user)).thenReturn(userDto);

		ApiGenericResponse<UsersDTO> response = usersService.saveUser(userDto);

		assertNotNull(response);
		assertNotNull(response.getData());
		assertEquals("shamanth.shetty@gmail.com", response.getData().getUserEmail());
		assertEquals("1234567890", response.getData().getUserPhoneNumber());
	}

	@Test
	 void testSaveUser_DuplicateEmailOrPhoneNumber() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPhoneNumber("1234567890");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		
		
		Users existingUser = new Users();
		existingUser.setUserEmail("shamanth.shetty@gmail.com");
		existingUser.setUserPhoneNumber("1234567890");
		existingUser.setIsActive(true);

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmailOrUserPhoneNumber("shamanth.shetty@gmail.com", "1234567890"))
				.thenReturn(existingUser);

		ApiGenericResponse<UsersDTO> response = usersService.saveUser(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Duplicate Email or Phone number", response.getMessage());
	}

	@Test
	 void testSaveUser_AccountBlocked() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPhoneNumber("1234567890");

		
		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		
		
		Users existingUser = new Users();
		existingUser.setUserEmail("shamanth.shetty@gmail.com");
		existingUser.setUserPhoneNumber("1234567890");
		existingUser.setIsActive(false);

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmailOrUserPhoneNumber("shamanth.shetty@gmail.com", "1234567890"))
				.thenReturn(existingUser);

		ApiGenericResponse<UsersDTO> response = usersService.saveUser(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals(UsersConstants.MESSAGE_ACCOUNT_BLOCKED.getMessage(), response.getMessage());
	}

	@Test
	 void testSaveUser_WrongInvestigationCentreCode() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPhoneNumber("1234567890");
		userDto.setUserType(UserType.Investigator);
		userDto.setInvestigationCentreCode("ABC123");
		
		Users user=new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		user.setUserType(UserType.Investigator);
		

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmailOrUserPhoneNumber("shamanth.shetty@gmail.com", "1234567890"))
				.thenReturn(null);
		when(investigationCentreRepository.getByInvestigationCentreCode("ABC123")).thenReturn(null);

		ApiGenericResponse<UsersDTO> response = usersService.saveUser(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Wrong Investigation centre code!", response.getMessage());
	}

	@Test
	 void testUpdateUser_Success() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserId(1);
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPhoneNumber("1234567890");
		userDto.setUserPassword("password");
		userDto.setUserType(UserType.Investigator);

		Users user = new Users();
		user.setUserId(1);
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		user.setUserPassword("password");
		user.setUserType(UserType.Investigator);
		user.setUpdatedAt(LocalDateTime.now());

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.save(any(Users.class))).thenReturn(user);
		when(usersMapper.usersToUsersDTO(user)).thenReturn(userDto);

		ApiGenericResponse<UsersDTO> response = usersService.updateUser(userDto);

		assertNotNull(response);
		assertNotNull(response.getData());
		assertEquals("shamanth.shetty@gmail.com", response.getData().getUserEmail());
		assertEquals("1234567890", response.getData().getUserPhoneNumber());

	}

	@Test
	 void testUpdateUser_UserNotFound() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserId(1);
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPhoneNumber("1234567890");
		userDto.setUserPassword("password");
		userDto.setUserType(UserType.Investigator);

		Users user = new Users();
		user.setUserId(1);
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPhoneNumber("1234567890");
		user.setUserPassword("password");
		user.setUserType(UserType.Investigator);

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.save(user)).thenReturn(null);

		ApiGenericResponse<UsersDTO> response = usersService.updateUser(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("User not found", response.getMessage());
	}

	@Test
	 void testDeleteUser_Success() {
		int userId = 1;
		Users user = new Users();
		user.setUserId(userId);
		user.setIsActive(true);
		when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
		Boolean result = usersService.deleteUser(userId);

		assertNotNull(result);
		assertTrue(result);
		assertFalse(user.getIsActive());

	}

	@Test
	 void testDeleteUser_UserNotFound() {
		int userId = 1;

		when(usersRepository.findById(userId)).thenReturn(Optional.empty());

		Boolean result = usersService.deleteUser(userId);

		assertNotNull(result);
		assertFalse(result);
	}

	@Test
	 void testCheckUserLogin_Success() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPassword("password");
		userDto.setUserType(UserType.Investigator);
		userDto.setInvestigationCentreCode("ABC123");
		userDto.setUserLocationCoordinates("12.9716 77.5946");
		
		Users user1 = new Users();
		user1.setUserEmail("shamanth.shetty@gmail.com");
		user1.setUserPassword("password");
		user1.setUserType(UserType.Investigator);
		

		InvestigationCentre investigationCentre = new InvestigationCentre();
		investigationCentre.setInvestigationCentreCode("ABC123");
		investigationCentre.setInvestigationCentreName("Cyber crimeRepo");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPassword("encodedPassword");
		user.setUserType(UserType.Investigator);
		user.setIsActive(true);
		user.setInvestigationCentre(investigationCentre);
		
		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user1);
		when(usersMapper.usersToUsersDTO(user)).thenReturn(userDto);
		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(user);
		when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
		when(jwtServiceImpl.generateToken("shamanth.shetty@gmail.com", UserType.Investigator)).thenReturn("jwtToken");
		when(externalAPIs.getAddressByLatLong(12.9716, 77.5946)).thenReturn("Bengaluru, India");
		

		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(userDto);
		assertNotNull(response);
		assertNotNull(response.getData());
		assertEquals("shamanth.shetty@gmail.com", response.getData().getUserEmail());
		assertEquals("jwtToken", response.getData().getJwtToken());
		verify(usersRepository).updateUserLiveLocationByUserId(user.getUserId(), "Bengaluru, India");
	}

	@Test
	 void testCheckUserLogin_EmailNotRegistered() {
		UsersDTO usersDto = new UsersDTO();
		usersDto.setUserEmail("shamanth.shetty@gmail.com");
		usersDto.setUserPassword("password");
		
		Users user=new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPassword("password");

		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(null);
        when(usersMapper.usersDTOtoUsers(usersDto)).thenReturn(user);
		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(usersDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Entered email doesn't have an account!", response.getMessage());
	}
	

	@Test
	 void testCheckUserLogin_AccountBlocked() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPassword("password");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setIsActive(false);

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(user);

		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Your account is blocked !You cant log in.", response.getMessage());
	}

	@Test
	 void testCheckUserLogin_WrongPassword() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPassword("wrongPassword");

		Users user=new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPassword("wrongPassword");
		
		Users selectedUser = new Users();
		selectedUser.setUserEmail("shamanth.shetty@gmail.com");
		selectedUser.setUserPassword("encodedPassword");
		selectedUser.setIsActive(true);

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(selectedUser);
		when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Wrong password!", response.getMessage());
	}

	@Test
	 void testCheckUserLogin_WrongInvestigationCentreCode() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPassword("password");
		userDto.setInvestigationCentreCode("ABC123");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPassword("password");
		
		
		Users selctedUser =new Users();
		selctedUser.setUserEmail("shamanth.shetty@gmail.com");
		selctedUser.setUserPassword("encodedPassword");
		selctedUser.setIsActive(true);
		selctedUser.setInvestigationCentre(new InvestigationCentre());
		selctedUser.getInvestigationCentre().setInvestigationCentreCode("ABC456");

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(selctedUser);
		when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Wrong Password/investigation centre code!", response.getMessage());
	}

	@Test
	 void testCheckUserLogin_SwitchToInvestigationLogin() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPassword("password");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPassword("encodedPassword");
		user.setIsActive(true);
		user.setInvestigationCentre(new InvestigationCentre());

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(user);

		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Please switch to Investigation login!Acoount is associated with Investigator.",
				response.getMessage());
	}

	@Test
	 void testCheckUserLogin_SwitchToInformantLogin() {
		UsersDTO userDto = new UsersDTO();
		userDto.setUserEmail("shamanth.shetty@gmail.com");
		userDto.setUserPassword("password");
		userDto.setInvestigationCentreCode("ABC123");

		Users user = new Users();
		user.setUserEmail("shamanth.shetty@gmail.com");
		user.setUserPassword("encodedPassword");
		user.setIsActive(true);

		when(usersMapper.usersDTOtoUsers(userDto)).thenReturn(user);
		when(usersRepository.selectUserByUserEmail("shamanth.shetty@gmail.com")).thenReturn(user);

		ApiGenericResponse<UsersDTO> response = usersService.checkUserLogin(userDto);

		assertNotNull(response);
		assertNull(response.getData());
		assertEquals("Please switch to Informant login!Acoount is associated with Informant.", response.getMessage());
	}

}
