package com.endava.CrimeReportingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.enums.UserType;

import jakarta.transaction.Transactional;


public interface  UsersRepository extends JpaRepository<Users, Integer>{

	

    @Transactional
    @Query("Select u from Users u where u.userEmail=:userEmail")
	Users selectUserByUserEmail(String userEmail);
    
    @Transactional
    @Query("Select u from Users u where u.userEmail=:userEmail or u.userPhoneNumber=:userPhoneNumber")
	Users selectUserByUserEmailOrUserPhoneNumber(String userEmail,String userPhoneNumber);
    
    List<Users> findByUserType(UserType userType);

	
    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.userLiveLocation = :userLiveLocation WHERE u.userId = :userId")
    void updateUserLiveLocationByUserId(Integer userId, String userLiveLocation);
    
    @Transactional
    @Query("SELECT u FROM Users u WHERE LOWER(u.userLiveLocation) LIKE LOWER(CONCAT('%', :searchString, '%')) AND u.investigationCentre IS NULL AND u.userId!=:userId")
    List<Users> findUsersByLiveLocationContainingIgnoreCase(String searchString,Integer userId);

}
