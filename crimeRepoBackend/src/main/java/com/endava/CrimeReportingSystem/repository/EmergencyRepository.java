package com.endava.CrimeReportingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.Emergency;

public interface EmergencyRepository extends JpaRepository<Emergency, Integer>{

}
