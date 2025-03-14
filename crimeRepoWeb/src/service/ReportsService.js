import axios from 'axios';
import axiosInstance from './AxiosInstance';
const { REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL } = process.env;


export const getReportById = async (id) => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL}/${id}`).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
};



export const getAllReports = async () => {
    const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL}/getAllReports`).then((res)=>res.data).catch((error)=>{return null});
    return response;
};

export const deleteReport = async (id) => {
    const response =  await axiosInstance.delete(`${REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL}/${id}`).then((res)=>res.data).catch((error)=> false);
    return response;
};

export const addReport = async (report) => {
    const response = await axiosInstance.post(REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL, report).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
  };

  export const addReportImage = async (reportId, reportImage) => {
    const formData = new FormData();
    formData.append('reportImage', reportImage);
  
    try {
      const response = await axiosInstance.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL}/saveReportImage/${reportId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return {status: true, data: response.data };
    } catch (error) {
      const errorMessage = error.response ? error.response.data : 'An unexpected error occurred';
      return { status: false, message: errorMessage };
    }
  };
 

  export const updateReport= async (report) => {
    const response = await axiosInstance.put(REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL, report).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
  };

  export const saveRepupdateReportVote= async (reportId,voteStatus,loggedInUserId) => {
    const reportBody={
      voteStatus,
      user:{userId:loggedInUserId}
    }
    const response = await axiosInstance.put(`${REACT_APP_CRIME_REPORTING_SYSTEM_REPORTS_URL}/vote/${reportId}`, reportBody).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
  };
