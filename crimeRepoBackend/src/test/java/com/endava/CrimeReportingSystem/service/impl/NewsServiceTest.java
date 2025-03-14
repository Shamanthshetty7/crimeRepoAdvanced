package com.endava.CrimeReportingSystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.entity.News;
import com.endava.CrimeReportingSystem.entity.dto.NewsDTO;
import com.endava.CrimeReportingSystem.mapper.NewsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.NewsRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News news;
    private NewsDTO newsDTO;

    @BeforeEach
    void setUp() {
        news = new News();
        news.setNewsId(1);
        news.setNewsHeadline("Test Headline");
        news.setNewsSmallDescription("Test Small Description");
        news.setNewsDetails("Test Details");
        news.setCreatedAt(LocalDateTime.now());
        news.setUpdatedAt(LocalDateTime.now());
        news.setNewsImage(new byte[]{1, 2, 3});

         newsDTO = NewsDTO.builder()
        	    .newsId(1)
        	    .newsHeadline("Test Headline")
        	    .newsSmallDescription("Test Small Description")
        	    .newsDetails("Test Details")
        	    .build();

    }

    @Test
    void testGetNewsById_Success() {
        when(newsRepository.findById(1)).thenReturn(Optional.of(news));
        when(newsMapper.newsToNewsDTO(news)).thenReturn(newsDTO);

        ApiGenericResponse<NewsDTO> response = newsService.getNewsById(1);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getNewsId());
        assertEquals("Test Headline", response.getData().getNewsHeadline());
    }
    
    @Test
    void testGetNewsById_WithImage() {
        when(newsRepository.findById(1)).thenReturn(Optional.of(news));
        when(newsMapper.newsToNewsDTO(news)).thenReturn(newsDTO);

        ApiGenericResponse<NewsDTO> response = newsService.getNewsById(1);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getNewsId());
        assertEquals("Test Headline", response.getData().getNewsHeadline());

       
        String expectedBase64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(news.getNewsImage());
        assertEquals(expectedBase64Image, response.getData().getNewsImage());
    }

    @Test
    void testGetNewsById_Failure() {
        when(newsRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<NewsDTO> response = newsService.getNewsById(1);

        assertNull(response.getData());
        assertEquals("News not found", response.getMessage());
    }

    @Test
    void testGetAllNews_Success() {
        List<News> newsList = new ArrayList<>();
        newsList.add(news);

        when(newsRepository.findAll()).thenReturn(newsList);
        when(newsMapper.newsToNewsDTO(news)).thenReturn(newsDTO);

        List<NewsDTO> response = newsService.getAllNews();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1, response.get(0).getNewsId());
        assertEquals("Test Headline", response.get(0).getNewsHeadline());
    }

    @Test
    void testSaveNews_Success() {
        when(newsMapper.newsDTOToNews(newsDTO)).thenReturn(news);
        when(newsMapper.newsToNewsDTO(news)).thenReturn(newsDTO);
        when(newsRepository.save(any(News.class))).thenReturn(news);

        ApiGenericResponse<NewsDTO> response = newsService.saveNews(newsDTO);

        assertNotNull(response.getData());
        assertEquals(1, response.getData().getNewsId());
        assertEquals("Test Headline", response.getData().getNewsHeadline());
        assertEquals("News saved successfully", response.getMessage());
    }

    @Test
    void testUpdateNews_Success() {
        when(newsRepository.findById(anyInt())).thenReturn(Optional.of(news));
        when(newsMapper.newsToNewsDTO(any())).thenReturn(newsDTO);
        when(newsRepository.save(news)).thenReturn(news);

        newsDTO.setNewsHeadline("Updated Headline");

        ApiGenericResponse<NewsDTO> response = newsService.updateNews(newsDTO);

        assertNotNull(response.getData());
        assertEquals("Updated Headline", response.getData().getNewsHeadline());
        assertEquals("News updated successfully", response.getMessage());
    }

    @Test
    void testUpdateNews_Failure() {
        when(newsRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<NewsDTO> response = newsService.updateNews(newsDTO);

        assertNull(response.getData());
        assertEquals("News not found", response.getMessage());
    }

    @Test
    void testDeleteNews_Success() {
        when(newsRepository.findById(anyInt())).thenReturn(Optional.of(news));

        Boolean response = newsService.deleteNews(1);

        assertEquals(true, response);
    }

    @Test
    void testDeleteNews_Failure() {
        when(newsRepository.findById(anyInt())).thenReturn(Optional.empty());

        Boolean response = newsService.deleteNews(1);

        assertEquals(false, response);
    }

    @Test
    void testSaveNewsImage_Success() throws IOException {
        MultipartFile mockMultipartFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(mockMultipartFile.getBytes()).thenReturn(new byte[1]);
        when(newsRepository.findById(anyInt())).thenReturn(Optional.of(news));
        when(newsMapper.newsToNewsDTO(any())).thenReturn(newsDTO);
        when(newsRepository.save(news)).thenReturn(news);
        ApiGenericResponse<NewsDTO> response = newsService.saveNewsImage(1, mockMultipartFile);

        assertNotNull(response.getData());
        assertEquals("News image saved successfully", response.getMessage());
    }

    @Test
    void testSaveNewsImage_Failure() throws IOException {
        MultipartFile mockMultipartFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(newsRepository.findById(anyInt())).thenReturn(Optional.empty());

        ApiGenericResponse<NewsDTO> response = newsService.saveNewsImage(1, mockMultipartFile);

        assertNull(response.getData());
        assertEquals("News not found", response.getMessage());
    }
}
