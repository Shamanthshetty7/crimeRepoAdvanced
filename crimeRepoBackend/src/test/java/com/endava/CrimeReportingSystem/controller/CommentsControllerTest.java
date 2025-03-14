package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.endava.CrimeReportingSystem.entity.dto.CommentsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.CommentsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CommentsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentsServiceImpl commentsServiceImpl;

    @InjectMocks
    private CommentsController commentsController;

    private ObjectMapper objectMapper;

    private CommentsDTO commentsDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(commentsController).build();
        commentsDTO =  CommentsDTO.builder().build();
      
    }

    @Test
    void testGetAllCommentsByReportId_Success() throws Exception {
        List<CommentsDTO> commentsList = Arrays.asList(CommentsDTO.builder().build(),
        	    CommentsDTO.builder().build());

        when(commentsServiceImpl.getCommentsByReportId(anyInt())).thenReturn(commentsList);

        mockMvc.perform(get("/crime-reporting-system/comments/getAllComments/{reportId}", 1)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentsList)));
    }

    @Test
    void testGetAllCommentsByReportId_NotFound() throws Exception {
        when(commentsServiceImpl.getCommentsByReportId(anyInt())).thenReturn(List.of());

        mockMvc.perform(get("/crime-reporting-system/comments/getAllComments/{reportId}", 1)
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Comments not available"));
    }

    @Test
    void testGetAllCommentsByReportId_Exception() throws Exception {
        when(commentsServiceImpl.getCommentsByReportId(anyInt())).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(get("/crime-reporting-system/comments/getAllComments/{reportId}", 1)
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while fetching comments."));
    }

    @Test
    void testSaveComment_Success() throws Exception {
        ApiGenericResponse<CommentsDTO> response = new ApiGenericResponse<>(null, null);
        response.setData(commentsDTO);
        when(commentsServiceImpl.saveComments(any(CommentsDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/comments")
                .content(objectMapper.writeValueAsString(commentsDTO))
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentsDTO)));
    }

    @Test
    void testSaveComment_Failure() throws Exception {
        ApiGenericResponse<CommentsDTO> response = new ApiGenericResponse<>(null, null);
        response.setMessage("Failed to save comment");
        when(commentsServiceImpl.saveComments(any(CommentsDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/comments")
                .content(objectMapper.writeValueAsString(commentsDTO))
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to save comment"));
    }

    @Test
    void testSaveComment_Exception() throws Exception {
        when(commentsServiceImpl.saveComments(any(CommentsDTO.class))).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(post("/crime-reporting-system/comments")
                .content(objectMapper.writeValueAsString(commentsDTO))
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while saving comment."));
    }

    @Test
    void testDeleteComment_Success() throws Exception {
        when(commentsServiceImpl.deleteCommentById(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/crime-reporting-system/comments/{commentId}", 1)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));
    }

    @Test
    void testDeleteComment_NotFound() throws Exception {
        when(commentsServiceImpl.deleteCommentById(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/crime-reporting-system/comments/{commentId}", 1)
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Comment not found"));
    }

    @Test
    void testDeleteComment_Exception() throws Exception {
        when(commentsServiceImpl.deleteCommentById(anyInt())).thenThrow(new RuntimeException("Unexpected Error"));

        mockMvc.perform(delete("/crime-reporting-system/comments/{commentId}", 1)
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred while deleting comment."));
    }
}
