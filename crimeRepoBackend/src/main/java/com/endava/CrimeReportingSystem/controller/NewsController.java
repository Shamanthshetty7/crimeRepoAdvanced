package com.endava.CrimeReportingSystem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.constants.NewsConstants;
import com.endava.CrimeReportingSystem.entity.dto.NewsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.NewsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(NewsConstants.BASE_NEWS_URL)
@Slf4j  // Logger created automatically
public class NewsController {

	 private final NewsServiceImpl newsServiceImpl;

	    // Constructor injection
	   
	    public NewsController(NewsServiceImpl newsServiceImpl) {
	        this.newsServiceImpl = newsServiceImpl;
	    }

    /**
     * End point to fetch a news item by its ID.
     * @param newsId The ID of the news item to fetch
     * @return News details or an error message if not found
     */
    @GetMapping(path=NewsConstants.GET_NEWS_BY_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getNewsById(@PathVariable int newsId) {
        log.debug("NewsController::getNewsById() - Fetching news for newsId: {}", newsId);

        try {
            ApiGenericResponse<NewsDTO> newsResponse = newsServiceImpl.getNewsById(newsId);

            if (newsResponse.getData() != null) {
                log.debug("NewsController::getNewsById() - Successfully retrieved news for newsId: {}", newsId);
                return new ResponseEntity<>(newsResponse.getData(), HttpStatus.OK);
            } else {
                log.debug("NewsController::getNewsById() - No news found for newsId: {}", newsId);
                return new ResponseEntity<>(NewsConstants.ERROR_NO_NEWS_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NewsController::getNewsById() - Error occurred while fetching news for newsId: {}. Error: {}", newsId, e.getMessage());
            return new ResponseEntity<>(NewsConstants.ERROR_FETCHING_NEWS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to fetch all news items.
     * @return List of all news items or an error message if none are found
     */
    @GetMapping(path=NewsConstants.GET_ALL_NEWS_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllNews() {
        log.debug("NewsController::getAllNews() - Fetching all news.");

        try {
            List<NewsDTO> newsList = newsServiceImpl.getAllNews();

            if (!newsList.isEmpty()) {
                log.debug("NewsController::getAllNews() - Successfully retrieved {} news items.", newsList.size());
                return new ResponseEntity<>(newsList, HttpStatus.OK);
            } else {
                log.debug("NewsController::getAllNews() - No news found.");
                return new ResponseEntity<>(NewsConstants.ERROR_NO_NEWS_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NewsController::getAllNews() - Error occurred while fetching all news. Error: {}", e.getMessage());
            return new ResponseEntity<>(NewsConstants.ERROR_FETCHING_ALL_NEWS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /**
     * End point to save a new news item.
     * @param newsDTO The news data to be saved
     * @return The saved news item or an error message
     */
    @PostMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveNews(@RequestBody NewsDTO newsDTO) {
        log.debug("NewsController::saveNews() - Saving new news: {}", newsDTO);

        try {
            ApiGenericResponse<NewsDTO> addedNews = newsServiceImpl.saveNews(newsDTO);

            if (addedNews.getData() != null) {
                log.debug("NewsController::saveNews() - Successfully saved news: {}", addedNews.getData());
                return new ResponseEntity<>(addedNews.getData(), HttpStatus.OK);
            } else {
                log.debug("NewsController::saveNews() - Failed to save news: {}", addedNews.getMessage());
                return new ResponseEntity<>(addedNews.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NewsController::saveNews() - Error occurred while saving news. Error: {}", e.getMessage());
            return new ResponseEntity<>(NewsConstants.ERROR_SAVING_NEWS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to save the image associated with a news item.
     * @param newsId The news item ID to associate the image with
     * @param newsImage The image to be saved for the news item
     * @return The updated news item or an error message
     */
    @PostMapping(path=NewsConstants.SAVE_NEWS_IMAGE_PATH, consumes = {CommonConstants.MULTIPART_FILE}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveNewsImage(@PathVariable int newsId, @RequestPart("newsImage") MultipartFile newsImage) throws IOException {
        log.debug("NewsController::saveNewsImage() - Saving news image for newsId: {}", newsId);

        try {
            ApiGenericResponse<NewsDTO> addedNews = newsServiceImpl.saveNewsImage(newsId, newsImage);

            if (addedNews.getData() != null) {
                log.debug("NewsController::saveNewsImage() - Successfully saved news image for newsId: {}", newsId);
                return new ResponseEntity<>(addedNews.getData(), HttpStatus.OK);
            } else {
                log.debug("NewsController::saveNewsImage() - Failed to save news image for newsId: {}", newsId);
                return new ResponseEntity<>(addedNews.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NewsController::saveNewsImage() - Error occurred while saving news image for newsId: {}. Error: {}", newsId, e.getMessage());
            return new ResponseEntity<>(NewsConstants.ERROR_SAVING_NEWS_IMAGE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to update an existing news item.
     * @param newsDTO The news data containing the updated information
     * @return The updated news item or an error message
     */
    @PutMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> updateNews(@RequestBody NewsDTO newsDTO) {
        log.debug("NewsController::updateNews() - Updating news for newsId: {}", newsDTO.getNewsId());

        try {
            ApiGenericResponse<NewsDTO> updatedNews = newsServiceImpl.updateNews(newsDTO);

            if (updatedNews.getData() != null) {
                log.debug("NewsController::updateNews() - Successfully updated news for newsId: {}", newsDTO.getNewsId());
                return new ResponseEntity<>(updatedNews.getData(), HttpStatus.OK);
            } else {
                log.debug("NewsController::updateNews() - Failed to update news for newsId: {}", newsDTO.getNewsId());
                return new ResponseEntity<>(updatedNews.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NewsController::updateNews() - Error occurred while updating news for newsId: {}. Error: {}", newsDTO.getNewsId(), e.getMessage());
            return new ResponseEntity<>(NewsConstants.ERROR_UPDATING_NEWS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * End point to delete a news item by its ID.
     * @param newsId The ID of the news item to delete
     * @return A success or failure message
     */
    @DeleteMapping(path=NewsConstants.DELETE_BY_ID_PATH, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> deleteNews(@PathVariable int newsId) {
        log.debug("NewsController::deleteNews() - Deleting news for newsId: {}", newsId);

        try {
            Boolean isDeleted = newsServiceImpl.deleteNews(newsId);

            if (isDeleted) {
                log.debug("NewsController::deleteNews() - Successfully deleted news for newsId: {}", newsId);
                return new ResponseEntity<>(NewsConstants.SUCCESS_NEWS_DELETED.getMessage(), HttpStatus.OK);
            } else {
                log.debug("NewsController::deleteNews() - Failed to delete news for newsId: {}", newsId);
                return new ResponseEntity<>(NewsConstants.ERROR_NEWS_DELETION_FAILED.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("NewsController::deleteNews() - Error occurred while deleting news for newsId: {}. Error: {}", newsId, e.getMessage());
            return new ResponseEntity<>(NewsConstants.ERROR_DELETING_NEWS.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}