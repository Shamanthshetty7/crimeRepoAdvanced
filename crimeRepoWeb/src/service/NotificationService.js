
import axiosInstance from "./AxiosInstance";

const { REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL } = process.env;

export const saveNotification = async (notificationTitle,notificationMessage,userId,toUserType) => {
    const notificationData={
        notificationTitle:notificationTitle,
        notificationMessage:notificationMessage,
        user:{userId:userId},
        toUserType:toUserType
    }
    const response = await axiosInstance.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL}/sendNotification`, notificationData).then(res=>res.data).catch((error)=>{ return error.response?.data });
    return response;
  };

  export const getNotificationsByUserId = async (userId) => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL}/${userId}`).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response?.data,statusCode:error.status} });
    return response;
};

export const updateNotification = async (notificationId) => {
    const response = await axiosInstance.put(`${REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL}/${notificationId}`).then((res)=>res).catch((error)=>{ return false });
    
    return response;
};


export const clearNotification = async (usrId) => {
    try {
       
        const response = await axiosInstance.put(
            `${REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL}/clear/${usrId}`
        );
        return response;
    } catch (error) {
        return false;
    }
};

export const sendEmergencyAlertNotification=async(notificationTitle, notificationMessage,context)=>{
    context.fetchLatLong() 
    const userLatLong=context.liveLocationLatLong?.fecthedUserLocation
    
    
    const notificationData={
        notificationTitle:notificationTitle,
        notificationMessage:notificationMessage,
        userLatLong:userLatLong,
        createdAt:new Date().toISOString()
    }
    const response = await axiosInstance.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL}/emergencyAlertNotification`, notificationData).then(res=>res.data).catch((error)=>{ return error.response?.data });
    
    return response;
}




