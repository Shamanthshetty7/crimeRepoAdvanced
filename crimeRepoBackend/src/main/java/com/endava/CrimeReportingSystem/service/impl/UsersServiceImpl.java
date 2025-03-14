package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.UsersConstants;
import com.endava.CrimeReportingSystem.entity.InvestigationCentre;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.InvestigationCentreRepository;
import com.endava.CrimeReportingSystem.repository.UsersRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.UsersService;
import com.endava.CrimeReportingSystem.service.utility.ExternalAPIs;

@Service
public class UsersServiceImpl implements UsersService{

	@Autowired
	UsersRepository usersRepository;

	@Autowired
	InvestigationCentreRepository investigationCentreRepository;

	@Autowired
	UsersMapper usersMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private JwtServiceImpl jwtServiceImpl;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	ExternalAPIs externalAPIs;

	/**
	 * fetching user user Id
	 */
	@Override
	public ApiGenericResponse<UsersDTO> getUserById(int userId) {
		Optional<Users> user=usersRepository.findById(userId);
		ApiGenericResponse<UsersDTO> response=new ApiGenericResponse<>(null,null);
		if(user.isEmpty()) {
			response.setMessage(UsersConstants.ERROR_USER_NOT_FOUND.getMessage());

		}else {
			UsersDTO usersDTO=usersMapper.usersToUsersDTO(user.get());
			response.setData(usersDTO);
		}

		return response;
	}

	/**
	 * method to fetch all users
	 */
	@Override
	public List<UsersDTO> getAllUsers() {
		List<Users> usersList=usersRepository.findAll();
		return usersList.stream()
			    .map(usersMapper::usersToUsersDTO)
			    .toList();		
	}

	/**
	 * saving the user after registration
	 */
	@Override
	public ApiGenericResponse<UsersDTO> saveUser(UsersDTO userDto) {
		Users users=usersMapper.usersDTOtoUsers(userDto);
		//fetching user by userEmail And UserEmail if already exist
		Users userByUserEmailorUserPhoneNumber=usersRepository.selectUserByUserEmailOrUserPhoneNumber(users.getUserEmail(),users.getUserPhoneNumber());
		ApiGenericResponse<UsersDTO> response=new ApiGenericResponse<>(null,null);

		if(userByUserEmailorUserPhoneNumber!=null) {
			if(!userByUserEmailorUserPhoneNumber.getIsActive()) {
				response.setMessage(UsersConstants.MESSAGE_ACCOUNT_BLOCKED.getMessage());
			}else {
				response.setMessage(UsersConstants.MESSAGE_DUPLICATE_EMAIL.getMessage());  
			}

			return response;
		}
		
		if(users.getUserType()==UserType.Investigator) {
			InvestigationCentre investigationCentre=investigationCentreRepository.getByInvestigationCentreCode(userDto.getInvestigationCentreCode());
			if(investigationCentre==null) {
				response.setMessage(UsersConstants.MESSAGE_WRONG_INV_CODE.getMessage());
				return response;
			}
			users.setInvestigationCentre(investigationCentre);
		}
		
		users.setCreatedAt(LocalDateTime.now());
		users.setUpdatedAt(LocalDateTime.now());
		String encrptedPassword=passwordEncoder.encode(users.getUserPassword());
		users.setUserPassword(encrptedPassword);
		users.setIsActive(true);
		Users savedUser=usersRepository.save(users);
		
		response.setData(usersMapper.usersToUsersDTO(savedUser));
		return response;
	}

	/**
	 * updating the user
	 */
	@Override
	public ApiGenericResponse<UsersDTO> updateUser(UsersDTO userDto) {
		ApiGenericResponse<UsersDTO> response=new ApiGenericResponse<>(null,null);
		Users users=usersMapper.usersDTOtoUsers(userDto);
		Users updatedUser=usersRepository.save(users);
		if(updatedUser==null) {
			response.setMessage(UsersConstants.ERROR_USER_NOT_FOUND.getMessage());
			return response;
		}
		updatedUser.setUpdatedAt(LocalDateTime.now());
		
		response.setData(usersMapper.usersToUsersDTO(updatedUser));
		return response;
	}

	/**
	 * deleting the user
	 */
	@Override
	public Boolean deleteUser(int userId) {
		Optional<Users> deletingUser=usersRepository.findById(userId);
		if(!deletingUser.isEmpty()) {
			deletingUser.get().setIsActive(false);
			usersRepository.save(deletingUser.get());
			return true;
		}
		return false;
	}

	/**
	 * checking user login 
	 */
	@Override
	public ApiGenericResponse<UsersDTO> checkUserLogin(UsersDTO userDto) {
		
		Users loginUser=usersMapper.usersDTOtoUsers(userDto);
	
		
		ApiGenericResponse<UsersDTO> response=new ApiGenericResponse<>(null,null);

		//generating jwt Token
		String jwtGeneratedToken;
		jwtGeneratedToken= jwtServiceImpl.generateToken(userDto.getUserEmail(),userDto.getUserType());



		Users existingUser=usersRepository.selectUserByUserEmail(loginUser.getUserEmail());


		if(existingUser==null) {
			response.setMessage(UsersConstants.MESSAGE_NO_ACCOUNT_ASSOCIATED.getMessage());
			return response;
		}else if(existingUser.getIsActive()==false) {
			response.setMessage(UsersConstants.MESSAGE_ACCOUNT_BLOCKED.getMessage());
			return response;
		}

		if(existingUser.getInvestigationCentre()==null&&userDto.getInvestigationCentreCode()==null) {

			if(passwordEncoder.matches(loginUser.getUserPassword(), existingUser.getUserPassword())) {
				UsersDTO usersDTO=usersMapper.usersToUsersDTO(existingUser);
				usersDTO.setJwtToken(jwtGeneratedToken);
				
				//setting users location address each time when they login
				//getting address by lat and long and pushing to users table
				var LatAndLong=userDto.getUserLocationCoordinates().strip().split(" ");
				String loc= externalAPIs.getAddressByLatLong(Double.parseDouble( LatAndLong[0]) ,Double.parseDouble( LatAndLong[1]));
               
                 usersRepository.updateUserLiveLocationByUserId(usersDTO.getUserId(),loc );
				

				response.setData(usersDTO);
				return response;
			}else {
				response.setMessage(UsersConstants.MESSAGE_WRONG_PASSWORD.getMessage());
				return response;
			}
		}else {
			if(userDto.getInvestigationCentreCode()==null) {
				response.setMessage(UsersConstants.MESSAGE_INV_ACCOUNT.getMessage());
				return response;
			}else if(existingUser.getInvestigationCentre()==null) {
				response.setMessage(UsersConstants.MESSAGE_INF_ACCOUNT.getMessage());
				return response;
			}
			if(passwordEncoder.matches(loginUser.getUserPassword(), existingUser.getUserPassword())&&existingUser.getInvestigationCentre().getInvestigationCentreCode().equals(userDto.getInvestigationCentreCode())) {

				UsersDTO userDataDto=usersMapper.usersToUsersDTO(existingUser);
				userDataDto.setJwtToken(jwtGeneratedToken);
				userDataDto.setInvestigationCentreCode(existingUser.getInvestigationCentre().getInvestigationCentreCode());
				
				//setting users location address each time when they login
				//getting address by lat and long and pushing to users table
				var LatAndLong=userDto.getUserLocationCoordinates().strip().split(" ");
				String loc= externalAPIs.getAddressByLatLong(Double.parseDouble( LatAndLong[0]) ,Double.parseDouble( LatAndLong[1]));
                usersRepository.updateUserLiveLocationByUserId(userDataDto.getUserId(),loc);

				response.setData(userDataDto);
				return response;
			}else {
				response.setMessage(UsersConstants.MESSAGE_WRONG_PASS_OR_INV_CODE.getMessage());
				return response;
			}
		}

	}





}
