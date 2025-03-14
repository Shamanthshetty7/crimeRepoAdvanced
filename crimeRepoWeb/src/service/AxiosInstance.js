import axios from 'axios';




const getJwtToken = () => {
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));
  return currentUser?.jwtToken || null;
};


const axiosInstance = axios.create();


axiosInstance.interceptors.request.use(
  (config) => {
    const token = getJwtToken();
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle 401 errors (unauthorized)
axiosInstance.interceptors.response.use(
  response => response,
  (error) => {
    if (error.response?.status === 401) {
      console.error('Unauthorized access - 401');
      localStorage.removeItem("currentUser");
      window.location.href = "/"; 
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
