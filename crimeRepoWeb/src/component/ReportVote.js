import React, { useEffect, useState } from 'react';
import {  updateReportVote } from '../service/ReportVoteService';
import { IconButton, Typography } from '@mui/material';
import { BiDownvote, BiUpvote } from 'react-icons/bi';
import { saveRepupdateReportVote } from '../service/ReportsService';

const ReportVote = ({ user, reportData, fetchReports ,allReportVotes,fetchReportVotes}) => {

  const [userVoteStatus, setUserVoteStatus] = useState(null);
  

 


  useEffect(() => {
    if(user==null)return
    const checkUserVoteStatus =  () => {
        const existingVote = allReportVotes.find(vote => vote.report?.reportId === reportData.reportId && vote.user?.userId === user?.userId);
        if (existingVote) {
          setUserVoteStatus(existingVote); 
        }
      }
    
    
    checkUserVoteStatus();
   
  }, []);

  const updateVoteCount = async (voteStatus) => {
    if (user==null) return; 

    if (userVoteStatus) {
      // Update existing vote
      const reportVoteUpdate = await updateReportVote(userVoteStatus.reportVoteId, voteStatus);
      if (reportVoteUpdate.status) {
        setUserVoteStatus(reportVoteUpdate.data); 
      }
    } else {
      // Create a new vote
      const response = await saveRepupdateReportVote(reportData.reportId, voteStatus, user.userId);
      if (response.status) {
        setUserVoteStatus(response.data); 
      }
    }
    
    fetchReports(); 
    fetchReportVotes();
  };

  const handleDownvote = (e) => {
    e.stopPropagation();
    updateVoteCount("down");
  };

  const handleUpvote = (e) => {
    e.stopPropagation();
    updateVoteCount("up");
  };

  const isUpvoted = userVoteStatus?.upVoted;
  const isDownvoted = userVoteStatus?.downVoted;

  return (
    <div className="upvote-downvote">
      <IconButton
        onClick={handleUpvote}
        disabled={user===null}
        
        style={{
          color: isUpvoted ? "green" : "inherit",
          opacity:user===null?0.5:1
        }}
      >
        <BiUpvote />
        
        <Typography>{reportData?.reportUpvoteCount}</Typography>
      </IconButton>

      <IconButton
        onClick={handleDownvote}
        disabled={user===null}
        style={{
          color: isDownvoted ? "red" : "inherit",
          opacity:user===null?0.5:1
        }}
      >
        <BiDownvote />
        <Typography>{reportData?.reportDownvoteCount}</Typography>
      </IconButton>
    </div>
  );
};

export default ReportVote;
