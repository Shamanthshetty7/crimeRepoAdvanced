package com.endava.CrimeReportingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.endava.CrimeReportingSystem.entity.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {

	public List<Comments> findByReportsReportId(int reportId);
}
