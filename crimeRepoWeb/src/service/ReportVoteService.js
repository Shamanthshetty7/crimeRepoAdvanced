import axios from 'axios';
import axiosInstance from './AxiosInstance';
const { REACT_APP_CRIME_REPORTING_SYSTEM_REPORTVOTE_URL } = process.env;



export const getReportVoteStatusByUserIdAndReportId = async (userId,reportId) => {
    const response = await axiosInstance.get(REACT_APP_CRIME_REPORTING_SYSTEM_REPORTVOTE_URL,{
        params:{
            userId:userId,
            reportId:reportId
        }
    }).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
};

export const getAllReportVotes = async () => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_REPORTVOTE_URL}/getAllReportVotes`).then((res)=>res.data).catch((error)=>{return null});
    return response;
};


export const updateReportVote= async (reportVoteId,voteStatus) => {
    
    const response = await axiosInstance.put(REACT_APP_CRIME_REPORTING_SYSTEM_REPORTVOTE_URL, null,{
        params:{
            reportVoteId:reportVoteId,
            voteStatus:voteStatus
        }
    }).then((res)=>{return {status:true,message:res.data}}).catch((error)=>{ return {status:false,message:error.response} });
    return response;
  };