package com.endava.CrimeReportingSystem.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.entity.dto.NewsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;

public interface NewsService {
	public ApiGenericResponse<NewsDTO> getNewsById(int newsId);
    public List<NewsDTO> getAllNews();
    public ApiGenericResponse<NewsDTO> saveNews(NewsDTO newsDTO);
    public ApiGenericResponse<NewsDTO> updateNews(NewsDTO newsDTO);
    public Boolean deleteNews(int newsId);
    public ApiGenericResponse<NewsDTO> saveNewsImage(int newsId, MultipartFile newsImage) throws Exception;
}
