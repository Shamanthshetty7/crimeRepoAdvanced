package com.endava.CrimeReportingSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.endava.CrimeReportingSystem.entity.dto.NewsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.NewsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private NewsServiceImpl newsServiceImpl;

    @InjectMocks
    private NewsController newsController;

    private ObjectMapper objectMapper;
    private NewsDTO newsDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(newsController).build();

         newsDTO = NewsDTO.builder()
        	    .newsId(1)
        	    .newsHeadline("Test News")
        	    .newsDetails("This is a test news.")
        	    .build();

    }

    // Test for getNewsById (Success)
    @Test
    void testGetNewsById_Success() throws Exception {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(newsDTO);

        when(newsServiceImpl.getNewsById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/news/getNewsById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.newsHeadline").value("Test News"));
    }

    // Test for getNewsById (Not Found)
    @Test
    void testGetNewsById_NotFound() throws Exception {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("News not available");

        when(newsServiceImpl.getNewsById(anyInt())).thenReturn(response);

        mockMvc.perform(get("/crime-reporting-system/news/getNewsById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("News not found"));
    }

    // Test for getAllNews (Success)
    @Test
    void testGetAllNews_Success() throws Exception {
        List<NewsDTO> newsList = new ArrayList<>();
        newsList.add(newsDTO);

        when(newsServiceImpl.getAllNews()).thenReturn(newsList);

        mockMvc.perform(get("/crime-reporting-system/news/getAllNews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].newsHeadline").value("Test News"));
    }

    // Test for getAllNews (Not Found)
    @Test
    void testGetAllNews_NotFound() throws Exception {
        when(newsServiceImpl.getAllNews()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/crime-reporting-system/news/getAllNews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("News not found"));
    }

    // Test for saveNews (Success)
    @Test
    void testSaveNews_Success() throws Exception {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(newsDTO);

        when(newsServiceImpl.saveNews(any(NewsDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.newsHeadline").value("Test News"));
    }

    // Test for saveNews (Failure)
    @Test
    void testSaveNews_Failure() throws Exception {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("Failed to save news");

        when(newsServiceImpl.saveNews(any(NewsDTO.class))).thenReturn(response);

        mockMvc.perform(post("/crime-reporting-system/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to save news"));
    }

    // Test for updateNews (Success)
    @Test
    void testUpdateNews_Success() throws Exception {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(newsDTO);

        when(newsServiceImpl.updateNews(any(NewsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.newsHeadline").value("Test News"));
    }

    // Test for updateNews (Failure)
    @Test
    void testUpdateNews_Failure() throws Exception {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("Failed to update news");

        when(newsServiceImpl.updateNews(any(NewsDTO.class))).thenReturn(response);

        mockMvc.perform(put("/crime-reporting-system/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to update news"));
    }

    // Test for deleteNews (Success)
    @Test
    void testDeleteNews_Success() throws Exception {
        when(newsServiceImpl.deleteNews(anyInt())).thenReturn(true);

        mockMvc.perform(delete("/crime-reporting-system/news/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("News deleted successfully"));
    }

    // Test for deleteNews (Failure)
    @Test
    void testDeleteNews_Failure() throws Exception {
        when(newsServiceImpl.deleteNews(anyInt())).thenReturn(false);

        mockMvc.perform(delete("/crime-reporting-system/news/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("News deletion failed"));
    }

    
 // Test for saving news image (Success)
    @Test
    void testSaveNewsImage_Success() throws Exception {
     
        MockMultipartFile newsImage = new MockMultipartFile(
                "newsImage", "sample.jpg", "image/jpeg", "sample image content".getBytes());

      
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setData(newsDTO);
        when(newsServiceImpl.saveNewsImage(anyInt(), any())).thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/news/saveNewsImage/1") 
                .file(newsImage)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.newsHeadline").value("Test News"));
    }

    // Test for saving news image (Failure)
    @Test
    void testSaveNewsImage_Failure() throws Exception {
       
        MockMultipartFile newsImage = new MockMultipartFile(
                "newsImage", "sample.jpg", "image/jpeg", "sample image content".getBytes());

        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null,null);
        response.setMessage("Failed to save news image");
        when(newsServiceImpl.saveNewsImage(anyInt(), any())).thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/news/saveNewsImage/1") 
                .file(newsImage)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Failed to save news image"));
    }

    // Test for saving news image (Exception Handling)
    @Test
    void testSaveNewsImage_Exception() throws Exception {
      
        MockMultipartFile newsImage = new MockMultipartFile(
                "newsImage", "sample.jpg", "image/jpeg", "sample image content".getBytes());

       
        when(newsServiceImpl.saveNewsImage(anyInt(), any())).thenThrow(new RuntimeException("Unexpected error"));

        
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .multipart("/crime-reporting-system/news/saveNewsImage/1") 
                .file(newsImage)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while saving news image."));
    }
    
    @Test
    void testGetNewsById_Exception() throws Exception {
        when(newsServiceImpl.getNewsById(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/news/getNewsById/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching news."));
    }

    // Test for getAllNews (Exception Handling)
    @Test
    void testGetAllNews_Exception() throws Exception {
        when(newsServiceImpl.getAllNews()).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/crime-reporting-system/news/getAllNews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while fetching all news."));
    }

    // Test for saveNews (Exception Handling)
    @Test
    void testSaveNews_Exception() throws Exception {
        when(newsServiceImpl.saveNews(any(NewsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/crime-reporting-system/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while saving news."));
    }

    // Test for updateNews (Exception Handling)
    @Test
    void testUpdateNews_Exception() throws Exception {
        when(newsServiceImpl.updateNews(any(NewsDTO.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(put("/crime-reporting-system/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while updating news."));
    }

    // Test for deleteNews (Exception Handling)
    @Test
    void testDeleteNews_Exception() throws Exception {
        when(newsServiceImpl.deleteNews(anyInt())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(delete("/crime-reporting-system/news/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("An unexpected error occurred while deleting news."));
    }
    
}
