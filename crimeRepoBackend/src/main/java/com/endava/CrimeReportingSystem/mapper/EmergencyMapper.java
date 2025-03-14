package com.endava.CrimeReportingSystem.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.Emergency;
import com.endava.CrimeReportingSystem.entity.dto.EmergencyDTO;

@Component
public class EmergencyMapper {

	@Autowired
	UsersMapper usersMapper;
	
	public EmergencyDTO emergencyToEmergencyDTO(Emergency emergency) {
		return new EmergencyDTO(emergency.getEmergencyId(),emergency.getEmergencyContactType(),emergency.getEmergencyContactNumber(),emergency.getCreatedAt(),null,usersMapper.usersToUsersDTO(emergency.getUser()));
    }

    public Emergency emergencyDTOToEmergency(EmergencyDTO emergencyDTO) {
        Emergency emergency = new Emergency();
        emergency.setEmergencyId(emergencyDTO.emergencyId());
        emergency.setEmergencyContactType(emergencyDTO.emergencyContactType());
        emergency.setEmergencyContactNumber(emergencyDTO.emergencyContactNumber());
        emergency.setCreatedAt(emergencyDTO.createdAt());
        emergency.setUser(usersMapper.usersDTOtoUsers(emergencyDTO.user()));
        return emergency;
    }
}
