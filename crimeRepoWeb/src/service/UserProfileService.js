import axios from 'axios';
import axiosInstance from './AxiosInstance';
const { REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL } = process.env;
// const token=JSON.parse(localStorage.getItem("currentUser")).jwtToken

export const getUserProfileById = async (id) => {
 
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL}/${id}`).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};

export const getUserProfileByUserId = async (id) => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL}/userProfile/${id}`).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};

export const getUserProfileKycStatusByUserId = async (id) => {
  const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL}/KycStatus/${id}`).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
  return response;
};



export const deleteUserProfile = async (id) => {
    const response =  await axiosInstance.delete(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL}/${id}`).then((res)=>res?.data).catch((error)=> false);
    return response;
};

export const addUserProfile = async (userProfile) => {
  
    const response = await axiosInstance.post(REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL, userProfile

    ).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
  };

  export const addUserProfileImage = async (userProfileId, userProfileImage) => {
    
    const formData = new FormData();
    
    formData.append('userProfileImage', userProfileImage);

    try {
      const response = await axiosInstance.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL}/saveUserProfileImage/${userProfileId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return {status: true, data: response.data };
    } catch (error) {
      const errorMessage = error.response ? error.response.message : 'An unexpected error occurred';
      return { status: false, message: errorMessage };
    }
  };
 

  export const updateUserProfile= async (userProfile) => {
    const response = await axiosInstance.put(REACT_APP_CRIME_REPORTING_SYSTEM_USERPROFILE_URL, userProfile).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
  };
