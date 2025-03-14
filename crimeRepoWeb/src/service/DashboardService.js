
import axiosInstance from "./AxiosInstance";


const {REACT_APP_CRIME_REPORTING_SYSTEM_DASHBOARD_URL}=process.env;

export const getAllDashboardData= async () => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_DASHBOARD_URL}/allDataCounts`).then((res)=>res?.data).catch((error)=>{return null});
    return response;
};


export const getAllReportByMonth= async (year) => {
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_DASHBOARD_URL}/getReportsByMonth/${year}`).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};
