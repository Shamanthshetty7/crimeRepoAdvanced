package com.endava.CrimeReportingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.endava.CrimeReportingSystem.entity.Notifications;

import jakarta.transaction.Transactional;

public interface NotificationsRepository extends JpaRepository<Notifications, Integer>{

	 List<Notifications> findByUserUserId(int userId);
	 
	 @Modifying
	 @Transactional
	 @Query("UPDATE Notifications n SET n.isActive = false WHERE n.user.userId = :userId")
	 int updateNotificationsStatusByUserId(int userId);
	 
	 
}
