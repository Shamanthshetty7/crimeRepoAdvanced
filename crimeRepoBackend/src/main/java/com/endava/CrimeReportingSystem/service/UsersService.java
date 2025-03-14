package com.endava.CrimeReportingSystem.service;

import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface UsersService {

	public ApiGenericResponse<UsersDTO> getUserById(int userId);
	public List<UsersDTO> getAllUsers();
	public ApiGenericResponse<UsersDTO> saveUser(UsersDTO userDto);
	public ApiGenericResponse<UsersDTO> updateUser(UsersDTO userDto);
	public Boolean deleteUser(int userId);
	public ApiGenericResponse<UsersDTO> checkUserLogin(UsersDTO userDto);
	
}
