
import axios from "axios";
import axiosInstance from "./AxiosInstance";
const {REACT_APP_CRIME_REPORTING_SYSTEM_COMMENTS_URL}=process.env;

export const saveComment = async (comment) => {
    const response = await axiosInstance.post(REACT_APP_CRIME_REPORTING_SYSTEM_COMMENTS_URL, comment).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};

export const getAllCommentsByReport = async (reportId) => {
    const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_COMMENTS_URL}/getAllComments/${reportId}`).then((res)=>res?.data).catch((error)=>{return null});
    return response;
};

export const deleteComment = async (commentId) => {
    const response = await axiosInstance
        .delete(`${REACT_APP_CRIME_REPORTING_SYSTEM_COMMENTS_URL}/${commentId}`)
        .then((res) => {
            return { status: true, message: res?.data };
        })
        .catch((error) => {
            return { status: false, message: error.response?.data };
        });
    return response;
};