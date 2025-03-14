import {
  MDBCard,
  MDBCardBody,
  MDBCardImage,
  MDBCol,
  MDBContainer,
  MDBInput,
  MDBRow,
} from "mdb-react-ui-kit";
import { useEffect, useState, useRef } from "react";
import { deleteComment, getAllCommentsByReport, saveComment } from "../service/CommentsService";
import UserDetails from "./UserDetails";
import { getUserProfileByUserId } from "../service/UserProfileService";
import { MESSAGES, PROFILEMESSAGES, STATUSMESSAGES, USERTYPE } from "../utils/Messages";
const EmptyUserProfileImage = "/images/empty-profile-image.jpg";

const CommentOnReports = ({ currentReport, Swal, context }) => {
  const [commentValue, setCommentValue] = useState("");
  const [allComments, setAllComments] = useState([]);
  const [addedComment, setAddedComment] = useState("");
  const [userDetailModal, SetUserDetailModal] = useState(false);
  const [commentUserProfile, setCommentUserProfile] = useState({});
  const [userProfileExist, setUserProfileExist] = useState(true);
  
  const [editComment, setEditComment] = useState(null);
  const [editCommentText, setEditCommentText] = useState("");
  
  const inputRef = useRef(null); // Add ref for input field

  const fetchAllComments = async () => {
    const allCommentsResponse = await getAllCommentsByReport(currentReport?.reportId);
    if (allCommentsResponse != null) {
      setAllComments(allCommentsResponse);
    }
  };

  const fetchUserProfile = async () => {
    const checkProfileExist = await getUserProfileByUserId(context.user?.userId);
    if (!checkProfileExist.status) {
      setUserProfileExist(false);
    }
  };

  useEffect(() => {
    if (context.user) {
      fetchUserProfile();
    } else {
      setUserProfileExist(false);
    }
    fetchAllComments();
  }, [addedComment, context.user]);

  useEffect(() => {
    // Focus the input field when we enter edit mode
    if (editComment) {
      inputRef.current?.focus();
    }
  }, [editComment]); // Only run when `editCommentId` changes

  const handleInputChange = (e) => {
    if (editComment) {
      setEditCommentText(e.target.value);
    } else {
      setCommentValue(e.target.value);
    }
  };

  const saveCommentToDatabase = async (comment) => {
    let response;

    if (editComment) {
      editComment.commentText=comment.commentText
      response = await saveComment(editComment);
    } else {
      response = await saveComment(comment);
    }

    if (response.status) {
      if (editComment) {
        Swal.fire({
          title: MESSAGES.commentUpdated,
          icon: STATUSMESSAGES.SUCCESS,
          draggable: true,
        });
      } else {
        Swal.fire({
          title: MESSAGES.commentAdded,
          icon: STATUSMESSAGES.SUCCESS,
          draggable: true,
        });
      }

      setAddedComment(response.data);
      setCommentValue("");
      setEditComment(null);
      setEditCommentText("");
    }
  };

  const handleAddOrUpdateComment = (e) => {
    e.preventDefault();
    const comment = {
      users: context.user,
      reports: currentReport,
      commentText: editComment ? editCommentText : commentValue,
    };

    saveCommentToDatabase(comment);
  };

  const handleEditComment = (comment) => {
    setEditComment(comment);
    setEditCommentText(comment.commentText);
  };

  const handleDeleteComment=async(comment)=>{
       Swal.fire({
            title: "Are you sure You want Delete the comment?",
            icon: STATUSMESSAGES.WARNING,
            confirmButtonText: "Ok",
            cancelButtonText: "Cancel",
            showCancelButton: true,
            showCloseButton: true,
          }).then(async(result) => {
            if (result.isConfirmed) {
      const result = await deleteComment(comment.commentId);
      if (result.status) {
          Swal.fire({
                  icon: STATUSMESSAGES.SUCCESS,  
                  title: 'Comment deleted successfully',  
                  toast: true,  
                  position: 'top-end',  
                  showConfirmButton: false, 
                  timer: 2500,  
                  background: '#d4edda',  
                  iconColor: '#155724',  
                  color: '#155724'  
                });
                fetchAllComments()
      } else {
        Swal.fire({
                icon: STATUSMESSAGES.error,  
                title: STATUSMESSAGES.ERROR, 
                text: 'Error in deleting comment', 
                toast: true,  
                position: 'top-end',  
                showConfirmButton: false, 
                timer: 2500,  
                background: '#f8d7da',  
                iconColor: '#721c24',  
                color: '#721c24'  
              }); 
      }
    }
  });
   
    
 
  }

  const TimeDiffOfComments = ({ commentedDateTime }) => {
    const commentedTime = new Date(commentedDateTime);
    const currentTime = new Date();
    const diffInSeconds = (currentTime - commentedTime) / 1000;

    let finalTime = "";
    if (diffInSeconds < 60) {
      finalTime = `${diffInSeconds.toFixed(0)} seconds`;
    } else if (diffInSeconds < 3600) {
      finalTime = `${(diffInSeconds / 60).toFixed(0)} minutes`;
    } else if (diffInSeconds < 86400) { // 86400 seconds = 1 day
      finalTime = `${(diffInSeconds / 3600).toFixed(0)} hours`;
    } else {
      finalTime = `${(diffInSeconds / 86400).toFixed(0)} days`;
    }

    return <span>{finalTime}</span>;
};


  const handleProfileClick = (user) => {
    if (context.user?.userType === USERTYPE.INVESTIGATOR) {
      if (user.userType === USERTYPE.INFORMANT) {
        setCommentUserProfile(user);
        SetUserDetailModal(true);
      } else {
        Swal.fire(PROFILEMESSAGES.restrictedProfile);
      }
    }
  };

  return (
    <MDBContainer className="d-flex justify-content-center align-items-center mb-5 mt-1">
      <MDBRow className="justify-content-center w-100">
        <MDBCol md="12" lg="12">
          <MDBCard className="shadow-0 border " style={{ backgroundColor: "#f0f2f5" }}>
            <MDBCardBody>
              {userProfileExist ? (
                <div className="d-flex w-100 justify-content-center align-items-center m-3">
                  <MDBInput
                    placeholder="Add your thought..."
                    style={{ width: "500px" }}
                    value={editComment ? editCommentText : commentValue}
                    onChange={handleInputChange}
                    ref={inputRef} 
                  />
                  <button
                    className="btn btn-success ms-3"
                    onClick={(e) => handleAddOrUpdateComment(e)}
                  >
                    {editComment ? "Update" : "Add"}
                  </button>
                  {editComment && (
                    <button
                      className="btn btn-danger ms-3"
                      onClick={() => {
                        setEditComment(null);
                        setEditCommentText("");
                      }}
                    >
                      Cancel
                    </button>
                  )}
                </div>
              ) : (
                <div className="d-flex w-100 justify-content-center align-items-center m-3">
                  <p className="fw-bold text-danger">
                    {context.user ? "Complete your profile to give your thoughts here" : "Login to give your thoughts here"}
                  </p>
                </div>
              )}

              {allComments?.length === 0 && <p>No comments available</p>}
              <div className="comments">
                {allComments?.map((comments) => {
                  return (
                    <MDBCard className="mb-4" key={comments.id}>
                      <MDBCardBody>
                        <p className={comments.users.userType === USERTYPE.INVESTIGATOR && `text-danger fw-bold`}>
                          {comments.commentText}
                        </p>

                        <div className="d-flex justify-content-between">
                          <div className="d-flex flex-row align-items-center" onClick={() => handleProfileClick(comments.users)}>
                            <MDBCardImage src={EmptyUserProfileImage} alt="avatar" width="25" height="25" />
                            <p className="small mb-0 ms-2">{comments.users.userName}</p>
                          </div>
                          {comments.users.userId === context.user?.userId && (
                            <div className="d-flex">
                            <span
                              
                              onClick={() => handleEditComment(comments)}
                            >
                            <i className="bi bi-pencil m-2 text-warning" id="edit-image"></i>

                            </span>
                            <span
                              
                              onClick={() => handleDeleteComment(comments)}
                            >
                            <i className="bi bi-trash m-2 text-danger" id="edit-image"></i>

                            </span>
                            </div>
                          )}
                        </div>
                        <div className="d-flex justify-content-between align-items-center">
                          <p className="mb-1">
                            {comments.users.userType}
                            <span className="small"> - <TimeDiffOfComments commentedDateTime={comments.createdAt} /> ago</span>
                          </p>
                        </div>
                      </MDBCardBody>
                    </MDBCard>
                  );
                })}
              </div>
            </MDBCardBody>
          </MDBCard>
        </MDBCol>
      </MDBRow>

      {userDetailModal && (
        <UserDetails SetUserDetailModal={SetUserDetailModal} userDetailModal={userDetailModal} user={commentUserProfile} />
      )}
    </MDBContainer>
  );
};

export default CommentOnReports;
