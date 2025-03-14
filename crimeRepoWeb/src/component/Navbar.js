import React, { Fragment, useContext, useEffect, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import "../styles/Navbar.scss";
import style from "../styles/CustomStyles.module.css";
import RegisterLogin from "./RegisterLogin";
import { toast, ToastContainer } from "react-toastify";
import Swal from "sweetalert2";
import AuthContext from "../store/AuthContext";
import { Badge, IconButton } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import NotificationRightModal from "./NotificationRightModal";
import { getNotificationsByUserId } from "../service/NotificationService";
import { LOGINLOGOUT, STATUSMESSAGES, USERTYPE } from "../utils/Messages";
const Navbar = () => {
  const [currentUser, setCurrentUser] = useState(null);

  const context = useContext(AuthContext);
  const navigate = useNavigate();
  const [openNotification, setOpenNotification] = useState(false);
  const [notificationCount, setNotificationCount] = useState(0);
  const [notificationData, setNotificationData] = useState([]);
  const currentUserType = currentUser?.userType;
 const {REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL}=process.env
  const onBeInformantClick = () => {
    context.setViewForm(true);
  };

  //logout
  const onLogoutOptionClick = () => {
    Swal.fire({
      title: LOGINLOGOUT.logoutQuestion,
      text: LOGINLOGOUT.loginAgain,
      icon: STATUSMESSAGES.WARNING,
      confirmButtonText: STATUSMESSAGES.OK,
      cancelButtonText: STATUSMESSAGES.Cancel,
      showCancelButton: true,
      showCloseButton: true,
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          icon: STATUSMESSAGES.SUCCESS,
          title: LOGINLOGOUT.logedOut,
          text: LOGINLOGOUT.logoutSuccess,
          showConfirmButton: false,
          timer: 1500,
        });
        localStorage.removeItem(USERTYPE.CURRENTUSER);
        context.logout();
        setCurrentUser(null);
        navigate("/");
      }
    });
  };

  //fetch notifiations
  const fecthNotifications = async () => {
    if (currentUser?.userId === undefined) {
      return;
    }
    const notificationsResponse = await getNotificationsByUserId(
      currentUser?.userId
    );

    if (notificationsResponse.status) {
      setNotificationCount(notificationsResponse.data.length);
      setNotificationData(notificationsResponse.data);
    }
  };

  

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem(USERTYPE.CURRENTUSER));

    if (user) {
      setCurrentUser(user);
      context.login(user);
    }
  }, []);
  
  

  useEffect(() => {
    const alertSound = new Audio('/emergencyAlert.wav');

    fecthNotifications();
    context.fetchUserProfile()
    context.fetchLatLong()
    
    if(currentUser?.userType===USERTYPE.INVESTIGATOR){
     
      const eventSource = new EventSource(REACT_APP_CRIME_REPORTING_SYSTEM_NOTIFICATION_URL+"/recieveAlertNotifications");
     
    

      eventSource.onmessage = (event) => {
       
        const newNotification = JSON.parse(event.data);
       
        newNotification.notificationId=0
        setNotificationData((prevData) => [newNotification, ...prevData]);
        setNotificationCount((prevCount) => prevCount + 1);

        const playAlertSound = () => {
          alertSound.loop = true;
         
          alertSound.play().catch((error) => {
            console.error("Error playing sound:", error);
          });
        };

     
      const stopAlertSound = () => {
          alertSound.pause();
          alertSound.currentTime = 0; 
      };

      
      Swal.fire({
          title: newNotification.notificationTitle,
          text: newNotification.notificationMessage,
          icon: STATUSMESSAGES.WARNING,
          showCancelButton: true,
          toast: true,
          position: 'top-end',
          customClass: {
              popup: 'colored-toast'
          },
          background: '#f8d7da',
          timerProgressBar: true,
          confirmButtonText: 'View Location',
          cancelButtonText: 'Dismiss',
          didOpen: (toast) => {
              toast.addEventListener('mouseenter', Swal.stopTimer);
              toast.addEventListener('mouseleave', Swal.resumeTimer);
              playAlertSound(); 
          },
          willClose: () => {
              stopAlertSound(); 
          }
      }).then((result) => {
          stopAlertSound();
          if (result.isConfirmed) {
           
          
            const emrgencyLatLong=newNotification.userLatLong.split(" ")
            let destLat,destLong;
            try{
             destLat=emrgencyLatLong[0]
             destLong=emrgencyLatLong[1]
          }catch(err){
            Swal.fire({
              icon: STATUSMESSAGES.error,
              title: STATUSMESSAGES.ERROR,
              text: "User location is not available!",
              showConfirmButton: false,
              timer: 1500,
            });
          }
            let coordinates=context?.liveLocationLatLong?.fecthedUserLocation.trim().split(" ")
            let originLat,originLong;
            try{
              originLat=coordinates[0]
              originLong=coordinates[1]
            }catch(err){
              Swal.fire({
                icon: STATUSMESSAGES.error,
                title: STATUSMESSAGES.ERROR,
                text: "unable to fetch your location!",
                showConfirmButton: false,
                timer: 1500,
              });
            }
           
            const directionsUrl = `https://www.google.com/maps/dir/?api=1&origin=${originLat},${originLong}&destination=${destLat},${destLong}`            
            window.open(directionsUrl, '_blank'); 
          }
      });

      };
  
      eventSource.onerror = (err) => {
        console.error("SSE connection error:", err);
       
       
      };

  
      return () => {
        eventSource.close();
      };
    }
   
    
  }, [context?.user]);

  
  return (
    <nav className="navbar navbar-expand-lg row crime-repo-nav w-100 m-auto ">
      <div
        className={`logo col-2 d-flex ${style.cursorPointer}`}
        onClick={() => navigate("/")}
      >
        <img
          src="/crimeReportingSystemLogo1.png"
          alt="crimeRepo-logo"
          className="rounded p-2 rounded-circle w-25 "
        />
        <h3 className="d-flex align-items-center">
          <span className="crime-text">crime</span>
          <span className="repo-text">Repo</span>
        </h3>
      </div>

      <div className="Nav-items col-10 row">
        <div className="main-nav-items col-8 d-flex align-items-center justify-content-center">
          {currentUserType === "Investigator" ? (
            <Fragment>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/InvestigatorReports"
              >
                Reports
              </NavLink>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/InvestigatorNews"
              >
                Upload News
              </NavLink>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/VerifyKyc"
              >
                Verify KYC
              </NavLink>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/Dashboard"
              >
                dashboard
              </NavLink>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/InvestigatorEmergency"
              >
                Emergency
              </NavLink>
            </Fragment>
          ) : (
            <Fragment>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/Reports"
              >
                Reports
              </NavLink>
              <NavLink
                to="/News"
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
              >
                News
              </NavLink>
              <NavLink
                className={({ isActive }) =>
                  `btn m-2 ${style.primaryButton} ${isActive ? "color" : ""}`
                }
                to="/Emergency"
              >
                Emergency
              </NavLink>
            </Fragment>
          )}
        </div>

        <div className="sub-nav-items col-4 d-flex align-items-center justify-content-end ">
          {currentUser ? (
            <div className="col-6 text-end">
              <IconButton edge="end" color="inherit">
                <Badge
                  badgeContent={notificationCount}
                  color="secondary"
                  className="me-4"
                >
                  <NotificationsIcon
                    className={`m ${style.bellIcon}`}
                    onClick={() => setOpenNotification(true)}
                  />
                </Badge>
              </IconButton>
            </div>
          ) : (
            <div className=" ">
              <button
                className={`btn btn-primary m-2 ${style.primaryButton}`}
                onClick={onBeInformantClick}
              >
                Be A Informant
              </button>
            </div>
          )}

          {currentUser && (
            <div className="user-section">
              <div className="d-flex align-items-center ">
                <img
                  src={context?.userProfileImage||"/images/empty-profile-image.jpg"}
                  alt="User Icon"
                  className="user-icon m-2  rounded-circle"
                  style={{ width: "40px", height: "40px" }}
                />{" "}
                <div className="dropdown">
                  <button
                    className="btn text-white dropdown-toggle "
                    type="button"
                    id="dropdownMenuButton"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                  ></button>
                  <ul
                    className="dropdown-menu dropdown-menu-end "
                    aria-labelledby="dropdownMenuButton"
                  >
                    <li className={`dropdown-item ${style.cursorPointer}`}>
                      <NavLink to="/Profile">Profile</NavLink>
                    </li>
                    {currentUserType === "Informant" && (
                      <li className={`dropdown-item ${style.cursorPointer}`}>
                        <NavLink to="my-reports">My Reports</NavLink>
                      </li>
                    )}
                    <li
                     className={`dropdown-item ${style.cursorPointer}`}
                      onClick={() => onLogoutOptionClick()}
                    >
                      Logout
                    </li>
                  </ul>
                </div>
              </div>
              <p className="username-text me-3">{currentUser?.userName}</p>
            </div>
          )}
        </div>
        <RegisterLogin
          viewForm={context.viewForm}
          setViewForm={context.setViewForm}
          Swal={Swal}
          toast={toast}
          setCurrentUser={setCurrentUser}
          context={context}
        />
      </div>
      <ToastContainer />
      {openNotification && (
        <NotificationRightModal
          openNotification={openNotification}
          setOpenNotification={setOpenNotification}
          notificationData={notificationData}
          fecthNotifications={fecthNotifications}
        />
      )}
    </nav>
  );
};

export default Navbar;
