package com.endava.CrimeReportingSystem.service;

import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.CommentsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface CommentsService {

	public ApiGenericResponse<CommentsDTO> saveComments(CommentsDTO commentsDTO);
	public List<CommentsDTO> getCommentsByReportId(int reportId);
    public Boolean deleteCommentById(int commentId);

}
