import axios from 'axios';
import axiosInstance from './AxiosInstance';
const { REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL } = process.env;


// export const getUserById = async (id) => {
//     const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL}/${id}`).then((res)=>{return {status:true,data:res?.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
//     return response;
// };

export const chekcUserLogin = async (user) => {
    const response = await axios.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL}/userLogin`,user).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
};

export const getAllUsers = async () => {
    const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL}/getAllUsers`).then((res)=>res.data).catch((error)=>{return null});
    return response;
};

export const deleteUser = async (id) => {
    const response =  await axiosInstance.delete(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL}/${id}`).then((res)=>res.data).catch((error)=> false);
    return response;
};

export const addUser = async (user) => {
    const response = await axios.post(`${REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL}/userRegister`, user).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
  };

  export const updateUser= async (user) => {
    const response = await axios.put(REACT_APP_CRIME_REPORTING_SYSTEM_USERS_URL, user).then((res)=>{return {status:true,data:res.data}}).catch((error)=>{ return {status:false,message:error.response?.data} });
    return response;
  };
