package com.endava.CrimeReportingSystem.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.endava.CrimeReportingSystem.constants.CommentsConstants;
import com.endava.CrimeReportingSystem.entity.Comments;
import com.endava.CrimeReportingSystem.entity.dto.CommentsDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.mapper.CommentsMapper;
import com.endava.CrimeReportingSystem.mapper.ReportsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.CommentsRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService {

	private final CommentsMapper commentsMapper;
    private final CommentsRepository commentsRepository;
    private final ReportsMapper reportsMapper;
    private final UsersMapper usersMapper;

    // Constructor injection
    public CommentsServiceImpl(CommentsMapper commentsMapper,
                               CommentsRepository commentsRepository,
                               ReportsMapper reportsMapper,
                               UsersMapper usersMapper) {
        this.commentsMapper = commentsMapper;
        this.commentsRepository = commentsRepository;
        this.reportsMapper = reportsMapper;
        this.usersMapper = usersMapper;
    }

	/* Save the comment and associate it with a report and user */
	@Override
	public ApiGenericResponse<CommentsDTO> saveComments(CommentsDTO commentsDTO)  {

		ApiGenericResponse<CommentsDTO> response = new ApiGenericResponse<>(null, null);
		Comments recivedComment = commentsMapper.commentsDTOToComments(commentsDTO);
		recivedComment.setReports(reportsMapper.reportDtoToReport(commentsDTO.getReports()));
		recivedComment.setUsers(usersMapper.usersDTOtoUsers(commentsDTO.getUsers()));

		if(recivedComment.getCommentId()==0) {
			recivedComment.setCreatedAt(LocalDateTime.now());
		}
		recivedComment.setUpdatedAt(LocalDateTime.now());
		Comments savedComment = Optional.ofNullable(commentsRepository.save(recivedComment))
	            .orElseThrow(() -> new RuntimeException(CommentsConstants.ERROR_SAVE_COMMENT_FALIED.getMessage()));


		response.setData(commentsMapper.commentsToCommentsDTO(savedComment));


		return response;
	}

	/* Get comments for a specific report and map entities to DTOs */
	@Override
	public List<CommentsDTO> getCommentsByReportId(int reportId) {
	
			return	 commentsRepository.findByReportsReportId(reportId).stream()
				.map((comments) -> {
					CommentsDTO commentsDTO = commentsMapper.commentsToCommentsDTO(comments);
					commentsDTO.setUsers(usersMapper.usersToUsersDTO(comments.getUsers()));
					commentsDTO.setReports(reportsMapper.reportToReportDTO(comments.getReports()));
					return commentsDTO;
				})

				// Sorting by userType, placing 'Investigator' at the top
				.sorted((comment1, comment2) -> {
				    UserType userType1 = comment1.getUsers().getUserType();
				    UserType userType2 = comment2.getUsers().getUserType();

				    return switch (userType1) {
				        case Investigator -> (UserType.Investigator.equals(userType2) ? 0 : -1); // comment1 should come before comment2 if comment2 is not Investigator
				        default -> (UserType.Investigator.equals(userType2) ? 1 : 0); // comment2 should come before comment1 if comment1 is not Investigator
				    }; 
				}).toList();
		
		



	}

	@Override
	public Boolean deleteCommentById(int commentId) {

		if (commentsRepository.existsById(commentId)) {
			commentsRepository.deleteById(commentId);
			return true;
		} else {
			return false; 
		}
	}


}
