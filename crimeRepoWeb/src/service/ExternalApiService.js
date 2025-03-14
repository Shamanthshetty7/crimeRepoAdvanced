
import axios from "axios";
import axiosInstance from "./AxiosInstance";

const {REACT_APP_CRIME_REPORTING_SYSTEM_EXTERNALAPI_URL}=process.env;

export const getAllMatchedCities= async (cityName) => {
   
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_EXTERNALAPI_URL}/${cityName}`).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
};

export const getCoordinatesByAddresses = async (addresses) => {
    try {
        const response = await axios.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_EXTERNALAPI_URL}/coordinates`, addresses);
        return {status:true,data:response?.data};
    } catch (error) {
       
        return {status:false,message:error.response?.data} ;
    }
};


export const getNearbyEmergencyServices = async (latitude, longitude) => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_EXTERNALAPI_URL}/emergency-services/${latitude}/${longitude}`).then((res) => {  return { status: true, data: res.data }; }).catch((error) => {  return { status: false, message: error.response.data };});
    return response;
}

export const checkVerifiedEmail = async (email) => {
    try {
        const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_EXTERNALAPI_URL}/verifyEmail/${email}`);
        return { status: true, data: response.data };
    } catch (error) {
        return { status: false, message: error.response.data };
    }
};