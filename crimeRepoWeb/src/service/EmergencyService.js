import axios from "axios";
import axiosInstance from "./AxiosInstance";

const { REACT_APP_CRIME_REPORTING_SYSTEM_EMERGENCY_URL } = process.env;

export const addEmergencyNumber = async (userId,emergencyDTO) => {
    emergencyDTO.user={
        userId:userId
    }
    const response = await axiosInstance.post(REACT_APP_CRIME_REPORTING_SYSTEM_EMERGENCY_URL, emergencyDTO)
        .then((res) => ({ status: true, data: res?.data }))
        .catch((error) => ({ status: false, message: error.response?.data }));
    return response;
};

export const fetchAllEmergencyNumbers = async () => {
    const response = await axios.get(REACT_APP_CRIME_REPORTING_SYSTEM_EMERGENCY_URL)
        .then((res) => ({ status: true, data: res?.data }))
        .catch((error) => ({ status: false, message: error.response?.data }));
    return response;
};

export const updateEmergencyNumber = async (emergencyDTO) => {
    const response = await axiosInstance.put(REACT_APP_CRIME_REPORTING_SYSTEM_EMERGENCY_URL, emergencyDTO)
        .then((res) => ({ status: true, data: res?.data }))
        .catch((error) => ({ status: false, message: error.response?.data }));
    return response;
};

export const deleteEmergencyNumber = async (id) => {
    const response = await axiosInstance.delete(`${REACT_APP_CRIME_REPORTING_SYSTEM_EMERGENCY_URL}/delete/${id}`)
        .then((res) => ({ status: true, data: res?.data }))
        .catch((error) => ({ status: false, message: error.response?.data }));
    return response;
};