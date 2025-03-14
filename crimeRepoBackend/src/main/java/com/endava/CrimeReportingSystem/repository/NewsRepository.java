package com.endava.CrimeReportingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.News;

public interface NewsRepository extends JpaRepository<News, Integer>{

}
