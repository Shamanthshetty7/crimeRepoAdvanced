
import Swal from 'sweetalert2';
import axiosInstance from './AxiosInstance';
const { REACT_APP_CRIME_REPORTING_SYSTEM_KYCAPPLICATION_URL } = process.env;

export const getAllKycApplications = async () => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_KYCAPPLICATION_URL}/getAllKycApplications`).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};

export const getKycApplicationByProfileId = async (profileId) => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_KYCAPPLICATION_URL}/getKycApplicationByProfileId/${profileId}`).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};



export const updateKycStatus = async (kycApplicationData) => {
    const response = await axiosInstance.put(REACT_APP_CRIME_REPORTING_SYSTEM_KYCAPPLICATION_URL,kycApplicationData).then((res)=>{return {status:true,data:res}}).catch((error)=>{ return {status:false,message:error?.response} });
    return response;
};

export const saveKycApplication = async (userProfileId,kycApllication) => {
    const formData = new FormData();
    formData.append("userVerificationImage",kycApllication.userVerificationImage);
    formData.append("userAdhaarFile",kycApllication.userAdhaarFile);
   
    delete kycApllication.userVerificationImage;
    delete kycApllication.userAdhaarFile;
    if(userProfileId==undefined){
        Swal.fire("error","something went wrong");
        return;
    }
    
    kycApllication.userProfile={profileId:userProfileId};
  
    const responseData = await axiosInstance.post(
        REACT_APP_CRIME_REPORTING_SYSTEM_KYCAPPLICATION_URL,
        kycApllication
      ).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    
    let kycApplicationId;
    if(responseData.status){
        kycApplicationId=responseData.data.kycId;
        const response = await axiosInstance.post(
            `${REACT_APP_CRIME_REPORTING_SYSTEM_KYCAPPLICATION_URL}/saveKycApplicationImages/${kycApplicationId}`,
            formData
          ).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
        return response;
    } 

    return {status:false,message:"Kyc application Not Sumbmiteed!"};
    
    

  };