package com.endava.CrimeReportingSystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endava.CrimeReportingSystem.constants.CommentsConstants;
import com.endava.CrimeReportingSystem.constants.CommonConstants;
import com.endava.CrimeReportingSystem.entity.dto.CommentsDTO;
import com.endava.CrimeReportingSystem.response.ApiGenericResponse;
import com.endava.CrimeReportingSystem.service.impl.CommentsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(CommentsConstants.COMMENTS_BASE_URL)
@Slf4j  // creates a logger automatically
public class CommentsController {

	 private final CommentsServiceImpl commentsServiceImpl;

	    // Constructor injection
	  
	    public CommentsController(CommentsServiceImpl commentsServiceImpl) {
	        this.commentsServiceImpl = commentsServiceImpl;
	    }
    
/**
 * Fetching all comments by report Id
 * @param reportId
 * @return List of all comments
 */
    @GetMapping(path=CommentsConstants.GET_ALL_COMMENTS, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> getAllCommentsByReportId(@PathVariable int reportId) {
        log.debug("CommentsController::getAllCommentsByReportId() passed id: {} ", reportId);
        
        try {
            List<CommentsDTO> comments = commentsServiceImpl.getCommentsByReportId(reportId);
            
            if (!comments.isEmpty()) {
                log.debug("CommentsController::getAllCommentsByReportId() -success ");
                return new ResponseEntity<>(comments, HttpStatus.OK);
            } else {
                log.debug("CommentsController::getAllCommentsByReportId() - Comments not available ");
                return new ResponseEntity<>(CommentsConstants.ERROR_COMMENTS_NOT_AVAILABLE.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("CommentsController::getAllCommentsByReportId() - Error occurred while fetching comments for reportId: {}. Error: {}", reportId, e.getMessage());
            return new ResponseEntity<>(CommentsConstants.ERROR_UNEXPECTED_FETCHING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   
    /**
     * Saving added comments 
     * @param commentsDTO
     * @return added status either saved data or error message
     */
    @PostMapping(consumes = {CommonConstants.APPLICATION_JSON}, produces = {CommonConstants.APPLICATION_JSON})
    public ResponseEntity<?> saveComment(@RequestBody CommentsDTO commentsDTO) {
        log.debug("CommentsController::saveComment() - Entering method with commentsDTO: {}", commentsDTO);
        
        try {
            ApiGenericResponse<CommentsDTO> addedComments = commentsServiceImpl.saveComments(commentsDTO);
            
            if (addedComments.getData() != null) {
                log.debug("CommentsController::saveComment() - Comment added successfully: {} ", addedComments.getData());
                return new ResponseEntity<>(addedComments.getData(), HttpStatus.OK);
            } else {
                log.error("CommentsController::saveComment() - Failed to save comment. Error message: {} ", addedComments.getMessage());
                return new ResponseEntity<>(addedComments.getMessage(), HttpStatus.BAD_REQUEST); 
            }
        } catch (Exception e) {
            log.error("CommentsController::saveComment() - Error occurred while saving comment. Error: {}", e.getMessage());
            return new ResponseEntity<>(CommentsConstants.ERROR_UNEXPECTED_SAVING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Delete a comment by its ID
     * @param commentId
     * @return Success or failure message
     */
    @DeleteMapping(CommentsConstants.DELETE_BY_COMMENT_ID)
    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
        log.debug("CommentsController::deleteComment() - Request to delete comment with id: {}", commentId);

        try {
            boolean isDeleted = commentsServiceImpl.deleteCommentById(commentId);

            if (isDeleted) {
                log.debug("CommentsController::deleteComment() - Comment deleted successfully with id: {}", commentId);
                return new ResponseEntity<>(CommentsConstants.SUCCESS_COMMENT_DELETED.getMessage(), HttpStatus.OK);
            } else {
                log.error("CommentsController::deleteComment() - Comment not found with id: {}", commentId);
                return new ResponseEntity<>(CommentsConstants.ERROR_COMMENT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("CommentsController::deleteComment() - Error occurred while deleting comment with id: {}. Error: {}", commentId, e.getMessage());
            return new ResponseEntity<>(CommentsConstants.ERROR_UNEXPECTED_DELETING.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
