package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.Comments;
import com.endava.CrimeReportingSystem.entity.dto.CommentsDTO;

@Component
public class CommentsMapper {

	public CommentsDTO commentsToCommentsDTO(Comments comments) {
		return CommentsDTO.builder()
				.commentId(comments.getCommentId())
				.commentText(comments.getCommentText())
				.createdAt(comments.getCreatedAt())
				.build();
	}

	public Comments commentsDTOToComments(CommentsDTO commentsDTO) {
		Comments comments = new Comments();
		comments.setCommentId(commentsDTO.getCommentId());
		comments.setCommentText(commentsDTO.getCommentText());
		comments.setCreatedAt(commentsDTO.getCreatedAt());
		return comments;
	}
}
