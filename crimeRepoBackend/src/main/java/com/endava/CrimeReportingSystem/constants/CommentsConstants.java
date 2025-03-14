package com.endava.CrimeReportingSystem.constants;

public enum CommentsConstants {

	
	// All Messages for service impl
	ERROR_UNABLE_TO_POST_COMMENT("Unable to post the comment!"),
	
	
	//all messaegs and constant for controller
	ERROR_COMMENTS_NOT_AVAILABLE("Comments not available"),
	ERROR_UNEXPECTED_FETCHING("An unexpected error occurred while fetching comments."),
	ERROR_UNEXPECTED_SAVING("An unexpected error occurred while saving comment."),
	ERROR_UNEXPECTED_DELETING("An unexpected error occurred while deleting comment."),
	ERROR_COMMENT_NOT_FOUND("Comment not found"),
	ERROR_SAVE_COMMENT_FALIED("Falied to save comment"),
	SUCCESS_COMMENT_DELETED("Comment deleted successfully");
	
	//constants
	public static final String COMMENTS_BASE_URL="/crime-reporting-system/comments";
	public static final String GET_ALL_COMMENTS="/getAllComments/{reportId}";
	public static final String DELETE_BY_COMMENT_ID="/{commentId}";
	
	private  final String value;
	CommentsConstants(String value){
		this.value=value;
	}
	
	public String getMessage() {
		return value;
	}
	
}
