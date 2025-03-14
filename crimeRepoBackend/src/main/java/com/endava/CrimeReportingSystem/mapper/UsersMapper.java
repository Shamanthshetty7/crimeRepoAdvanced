package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;

@Component
public class UsersMapper {

	public UsersDTO usersToUsersDTO(Users user) {
		return UsersDTO.builder()
			    .userId(user.getUserId())
			    .userName(user.getUserName())
			    .userEmail(user.getUserEmail())
			    .userPassword(user.getUserPassword())
			    .userPhoneNumber(user.getUserPhoneNumber())
			    .isActive(user.getIsActive())
			    .userType(user.getUserType())
			    .build();

	}
	
	
	public Users usersDTOtoUsers(UsersDTO usersDto) {
		Users user=new Users();
		user.setUserId(usersDto.getUserId());
		user.setUserName(usersDto.getUserName());
		user.setUserEmail(usersDto.getUserEmail());
		user.setUserPassword(usersDto.getUserPassword());
		user.setUserPhoneNumber(usersDto.getUserPhoneNumber());
		user.setUserType(usersDto.getUserType());
		user.setIsActive(usersDto.getIsActive());
		return user;
	}

}
