package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.endava.CrimeReportingSystem.entity.Comments;
import com.endava.CrimeReportingSystem.entity.Reports;
import com.endava.CrimeReportingSystem.entity.Users;
import com.endava.CrimeReportingSystem.entity.dto.CommentsDTO;
import com.endava.CrimeReportingSystem.entity.dto.UsersDTO;
import com.endava.CrimeReportingSystem.enums.UserType;
import com.endava.CrimeReportingSystem.mapper.CommentsMapper;
import com.endava.CrimeReportingSystem.mapper.ReportsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.CommentsRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceTest {

    @Mock
    private CommentsMapper commentsMapper;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private ReportsMapper reportsMapper;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private CommentsServiceImpl commentsService;

    private Comments comment;
    private CommentsDTO commentDTO;
    private Users informantUser;
    private Users investigatorUser;
    private Reports report;

    @BeforeEach
    void setUp() {
        informantUser = new Users();
        informantUser.setUserId(1);
        informantUser.setUserType(UserType.Informant);

        investigatorUser = new Users();
        investigatorUser.setUserId(2);
        investigatorUser.setUserType(UserType.Investigator);

        report = new Reports();
        report.setReportId(1);

        comment = new Comments();
        comment.setCommentId(1);
        comment.setUsers(informantUser);
        comment.setReports(report);
        comment.setCommentText("Test Comment");
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

         commentDTO = CommentsDTO.builder()
        	    .commentId(1)
        	    .users(usersMapper.usersToUsersDTO(informantUser))
        	    .reports(reportsMapper.reportToReportDTO(report))
        	    .commentText("Test Comment")
        	    .build();
    }

    @Test
    void testSaveComments_Success() {
        when(commentsMapper.commentsDTOToComments(any(CommentsDTO.class))).thenReturn(comment);
        when(commentsRepository.save(any(Comments.class))).thenReturn(comment);
        when(commentsMapper.commentsToCommentsDTO(any(Comments.class))).thenReturn(commentDTO);

        ApiGenericResponse<CommentsDTO> response = commentsService.saveComments(commentDTO);

        assertNotNull(response.getData());
        assertEquals("Test Comment", response.getData().getCommentText());
    }

   

    @Test
    void testGetCommentsByReportId_Success() {
        List<Comments> commentList = new ArrayList<>();
        commentList.add(comment);

        Comments investigatorComment = new Comments();
        investigatorComment.setCommentId(2);
        investigatorComment.setUsers(investigatorUser);
        investigatorComment.setReports(report);
        investigatorComment.setCommentText("Investigator Comment");
        investigatorComment.setCreatedAt(LocalDateTime.now());
        investigatorComment.setUpdatedAt(LocalDateTime.now());
        commentList.add(investigatorComment);

        when(commentsRepository.findByReportsReportId(anyInt())).thenReturn(commentList);
        when(commentsMapper.commentsToCommentsDTO(any(Comments.class))).thenAnswer(invocation -> {
            Comments comments = invocation.getArgument(0);
            return  CommentsDTO.builder()
            	    .commentId(comments.getCommentId())
            	    .commentText(comments.getCommentText())
            	    .users(usersMapper.usersToUsersDTO(comments.getUsers()))
            	    .reports(reportsMapper.reportToReportDTO(comments.getReports()))
            	    .build();

        });
        when(usersMapper.usersToUsersDTO(any(Users.class))).thenAnswer(invocation -> {
            Users user = invocation.getArgument(0);
            UsersDTO usersDTO = new UsersDTO();
            usersDTO.setUserId(user.getUserId());
            usersDTO.setUserType(user.getUserType());
            return usersDTO;
        });
        when(reportsMapper.reportToReportDTO(any(Reports.class))).thenReturn(commentDTO.getReports());

        List<CommentsDTO> response = commentsService.getCommentsByReportId(1);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Investigator Comment", response.get(0).getCommentText()); // Investigator comment should come first
        assertEquals("Test Comment", response.get(1).getCommentText()); // Informant comment should come second
    }

    @Test
    void testDeleteCommentById_Success() {
        when(commentsRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(commentsRepository).deleteById(anyInt());

        Boolean response = commentsService.deleteCommentById(1);
        assertTrue(response);
    }

    @Test
    void testDeleteCommentById_Failure() {
        when(commentsRepository.existsById(anyInt())).thenReturn(false);

        Boolean response = commentsService.deleteCommentById(1);
        assertEquals(false, response);
    }
}
