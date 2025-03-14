
import axiosInstance from './AxiosInstance';
const { REACT_APP_CRIME_REPORTING_SYSTEM_INVESTIGATIONCENTRE_URL } = process.env;


export const getInvestigationCentreByInvestigatorCode = async (invCode) => {
    const encodedInvCode = encodeURIComponent(invCode);
    const response = await axiosInstance.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_INVESTIGATIONCENTRE_URL}/${encodedInvCode}`).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response.data} });
    return response;
};