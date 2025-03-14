
import axiosInstance from './AxiosInstance';
import axios from 'axios';
const { REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL } = process.env;

export const getAllNews = async () => {
    const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL}/getAllNews`)
        .then((res) => { return { status: true, data: res?.data } })
        .catch((error) => { return { status: false, message: error.response?.data } });
    return response;
};

export const getNewsById = async (newsId) => {
    const response = await axios.get(`${REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL}/getNewsById/${newsId}`)
        .then((res) => { return { status: true, data: res?.data } })
        .catch((error) => { return { status: false, message: error.response?.data } });
    return response;
};

export const saveNews = async (userId,newsDTO) => {
  
    newsDTO.user={
        userId:userId
    }
    const response = await axiosInstance.post(REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL, newsDTO)
        .then((res) => { return { status: true, data: res?.data } })
        .catch((error) => { return { status: false, message: error.response?.data } });
    return response;
};

export const updateNews = async (newsDTO) => {
    const response = await axiosInstance.put(REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL, newsDTO)
        .then((res) => { return { status: true, data: res?.data } })
        .catch((error) => { return { status: false, message: error.response?.data } });
    return response;
};

export const deleteNews = async (newsId) => {
    const response = await axiosInstance.delete(`${REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL}/${newsId}`)
        .then((res) => { return { status: true, data: res?.data } })
        .catch((error) => { return { status: false, message: error.response?.data } });
    return response;
};

export const saveNewsImage = async (newsId, newsImage) => {
    const formData = new FormData();
    formData.append("newsImage", newsImage);

    const response = await axiosInstance.post(
        `${REACT_APP_CRIME_REPORTING_SYSTEM_NEWS_URL}/saveNewsImage/${newsId}`,
        formData
    ).then((res) => { return { status: true, data: res?.data } })
    .catch((error) => { return { status: false, message: error.response?.data } });

    return response;
};