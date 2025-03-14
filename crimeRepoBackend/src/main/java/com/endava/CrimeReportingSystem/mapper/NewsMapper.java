package com.endava.CrimeReportingSystem.mapper;

import org.springframework.stereotype.Component;

import com.endava.CrimeReportingSystem.entity.News;
import com.endava.CrimeReportingSystem.entity.dto.NewsDTO;

@Component
public class NewsMapper {

    
    public NewsDTO newsToNewsDTO(News news) {
    	return NewsDTO.builder()
    		    .newsId(news.getNewsId())
    		    .newsHeadline(news.getNewsHeadline())
    		    .newsSmallDescription(news.getNewsSmallDescription())
    		    .newsDetails(news.getNewsDetails())
    		    .createdAt(news.getCreatedAt())
    		    .updatedAt(news.getUpdatedAt())
    		    .build();

    }

  
    public News newsDTOToNews(NewsDTO newsDTO) {
        News news = new News();
        
        news.setNewsId(newsDTO.getNewsId());
        news.setNewsHeadline(newsDTO.getNewsHeadline());
        news.setNewsSmallDescription(newsDTO.getNewsSmallDescription());
        news.setNewsDetails(newsDTO.getNewsDetails());
        news.setCreatedAt(newsDTO.getCreatedAt());
        news.setUpdatedAt(newsDTO.getUpdatedAt());
       
        
        return news;
    }
}