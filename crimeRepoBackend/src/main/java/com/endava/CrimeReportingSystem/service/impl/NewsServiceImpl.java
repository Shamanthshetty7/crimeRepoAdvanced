package com.endava.CrimeReportingSystem.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.NewsConstants;
import com.endava.CrimeReportingSystem.entity.News;
import com.endava.CrimeReportingSystem.entity.dto.NewsDTO;
import com.endava.CrimeReportingSystem.mapper.NewsMapper;
import com.endava.CrimeReportingSystem.mapper.UsersMapper;
import com.endava.CrimeReportingSystem.repository.NewsRepository;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

	 private final NewsRepository newsRepository;
	    private final NewsMapper newsMapper;
	    private final UsersMapper usersMapper;

	    // Constructor injection
	    public NewsServiceImpl(NewsRepository newsRepository,
	                           NewsMapper newsMapper,
	                           UsersMapper usersMapper) {
	        this.newsRepository = newsRepository;
	        this.newsMapper = newsMapper;
	        this.usersMapper = usersMapper;
	    }

    
    /** 
     * Method to retrieve a specific news item by its ID.
     */
    @Override
    public ApiGenericResponse<NewsDTO> getNewsById(int newsId) {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null, null);
        Optional<News> news = newsRepository.findById(newsId);
        
        if (!news.isEmpty()) {
        NewsDTO newsDTO = newsMapper.newsToNewsDTO(news.get());
        if (news.get().getNewsImage() != null) {
            
            String base64Image = Base64.getEncoder().encodeToString(news.get().getNewsImage());
            newsDTO.setNewsImage(CommonConstants.BASE64_IMAGE_JPEG_PREFIX + base64Image);
        }
        response.setData(newsDTO);
        } else {
            response.setMessage(NewsConstants.ERROR_NO_NEWS_FOUND.getMessage());
        }
        
      
        	
       
        return response;
    }

    
    /** 
     * Method to retrieve all news items, sorted by latest.
     */
    @Override
    public List<NewsDTO> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        
      //latest news sorting
        newsList.sort((news1, news2) -> news2.getCreatedAt().compareTo(news1.getCreatedAt()));
        
       return newsList.stream().map((news) -> {
            NewsDTO newsDTO = newsMapper.newsToNewsDTO(news);
            
            if (news.getNewsImage() != null) {
            
                String base64Image = Base64.getEncoder().encodeToString(news.getNewsImage());
                newsDTO.setNewsImage(CommonConstants.BASE64_IMAGE_JPEG_PREFIX + base64Image);
            }
            
            return newsDTO;
        }).toList();
        
       
    }

    
    /** 
     * Method to save a news item.
     */
    @Override
    public ApiGenericResponse<NewsDTO> saveNews(NewsDTO newsDTO) {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null, null);
        News news = newsMapper.newsDTOToNews(newsDTO);
        news.setUser(usersMapper.usersDTOtoUsers(newsDTO.getUser()));
        news.setCreatedAt(LocalDateTime.now());
        news.setUpdatedAt(LocalDateTime.now());
        
        News savedNews =Optional.ofNullable(newsRepository.save(news)).orElseThrow(()->new RuntimeException(NewsConstants.ERROR_SAVING_NEWS.getMessage()));
        response.setData(newsMapper.newsToNewsDTO(savedNews));
        response.setMessage(NewsConstants.SUCCESS_NEWS_SAVED.getMessage());
        return response;
    }

    
    /** 
     * Method to update an existing news item.
     */
    @Override
    public ApiGenericResponse<NewsDTO> updateNews(NewsDTO newsDTO) {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null, null);
        Optional<News> existingNews = newsRepository.findById(newsDTO.getNewsId());
        if (!existingNews.isEmpty()) {
           
            existingNews.get().setNewsHeadline(newsDTO.getNewsHeadline());
            existingNews.get().setNewsSmallDescription(newsDTO.getNewsSmallDescription());
            existingNews.get().setNewsDetails(newsDTO.getNewsDetails());
            existingNews.get().setUpdatedAt(LocalDateTime.now());
            
            News updatedNews = Optional.ofNullable(newsRepository.save(existingNews.get())).orElseThrow(()->new RuntimeException(NewsConstants.ERROR_SAVING_NEWS.getMessage()));
            response.setData(newsMapper.newsToNewsDTO(updatedNews));
            response.setMessage(NewsConstants.SUCCESS_NEWS_UPDATED.getMessage());
        } else {
            response.setMessage(NewsConstants.ERROR_NO_NEWS_FOUND.getMessage());
        }
        return response;
    }

    /** 
     * Method to delete a news item by its ID.
     */
    @Override
    public Boolean deleteNews(int newsId) {
        Optional<News> existingNews = newsRepository.findById(newsId);
        if (!existingNews.isEmpty()) {
            newsRepository.delete(existingNews.get());
            return true;
        } else {
            return false;
        }
    }

    /** 
     * Method to save a news image.
     */
    @Override
    public ApiGenericResponse<NewsDTO> saveNewsImage(int newsId, MultipartFile newsImage) throws IOException {
        ApiGenericResponse<NewsDTO> response = new ApiGenericResponse<>(null, null);
        Optional<News> existingNews = newsRepository.findById(newsId);
        if (!existingNews.isEmpty()) {
            existingNews.get().setNewsImage(newsImage.getBytes());
            existingNews.get().setUpdatedAt(LocalDateTime.now());
            
            News updatedNews =Optional.ofNullable(newsRepository.save(existingNews.get())).orElseThrow(()->new RuntimeException(NewsConstants.ERROR_SAVING_NEWS.getMessage()));
            response.setData(newsMapper.newsToNewsDTO(updatedNews));
            response.setMessage(NewsConstants.SUCCESS_NEWS_IMAGE_SAVED.getMessage());
        } else {
            response.setMessage(NewsConstants.ERROR_NO_NEWS_FOUND.getMessage());
        }
        return response;
    }
}
