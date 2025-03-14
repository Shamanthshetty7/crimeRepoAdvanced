import React, { useState, useEffect } from 'react';
import AuthContext from './AuthContext';
import { getAllReports } from '../service/ReportsService';
import { MESSAGES } from '../utils/Messages';
import Swal from 'sweetalert2';
import { getUserProfileByUserId } from '../service/UserProfileService';

const ContextStore = ({ children }) => {
  const [user, setUser] = useState(()=>{
    const storedUser = localStorage.getItem('currentUser');
    return storedUser ? JSON.parse(storedUser) : null;
});

  const [viewForm, setViewForm] = useState(false);
  const [allReports,setAllReports]=useState([]);
  const [usrProfileId,setUserProfileId]=useState(null);
  const [crimeData,setCrimeData]=useState(null);
  const [liveLocationLatLong,setLiveLocationLatLong]=useState(null);
  const  [userProfileImage,setUserProfileImage] =useState("/images/empty-profile-image.jpg");


  
  const login = (userData) => {
    setUser(userData);
    fetchUserProfile();
  };

  const logout = () => {
    setUser(null);
  };

  const fetchLatLong = async () => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (position) => {
            const { latitude, longitude } = position.coords;
            setLiveLocationLatLong({
              fecthedUserLocation: "" + latitude + " " + longitude,
            });
          },
          (error) => {
           
            setLiveLocationLatLong({
              fecthedUserLocation: "" + 0 + " " + 0,
            });
         
          }
        
        );
        
      } else {
        Swal.fire(MESSAGES.LOCATIONNOTSUPPORTED);
      }
    };

    const fetchUserProfile=async()=>{
      if(user){
      const userProfileData = await getUserProfileByUserId(user?.userId);
       if(userProfileData.status){
        setUserProfileImage(userProfileData.data?.userProfileImage);
       }
      }
    }

  const activeReportCount=()=>{
    let isActive=false;
    allReports?.filter(report=>{
      if(report.isActive){
        isActive=true;
        return isActive;
      }
    })
    return isActive;
  }

   const fetchReports=async()=>{
       const reports=await getAllReports();
       setAllReports(reports);
    }

    const fetchUser=async()=>{
      const userData = JSON.parse(localStorage.getItem('currentUser'));
      if (userData) {
        setUser(userData);  
      }
    }

    const saveUser=async()=>{
      
      localStorage.setItem('currentUser',JSON.stringify(user));
      setUser(user);
    }


  return (
    <AuthContext.Provider value={{ user,login, logout,viewForm, setViewForm,allReports,setAllReports,fetchReports,activeReportCount,fetchUser,saveUser,usrProfileId,setUserProfileId,crimeData,setCrimeData,fetchLatLong,liveLocationLatLong,userProfileImage,setUserProfileImage,fetchUserProfile}}>
      {children}
    </AuthContext.Provider>
  );
};

export default ContextStore;