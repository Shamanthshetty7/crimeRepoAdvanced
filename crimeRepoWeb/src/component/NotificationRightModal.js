import React, { useContext, useState } from "react";
import "../styles/NotificationRightModal.css";
import style from "../styles/CustomStyles.module.css";
import { clearNotification, updateNotification } from "../service/NotificationService";
import AuthContext from "../store/AuthContext";
import Swal from "sweetalert2";
import { STATUSMESSAGES } from "../utils/Messages";

const NotificationRightModal = ({
  openNotification,
  setOpenNotification,
  notificationData,
  fecthNotifications
}) => {
  const [collapseShow, setCollpaseShow] = useState({});
  const context=useContext(AuthContext)

  const onNotificationSeen = async (notificationId) => {
    setCollpaseShow((prevState) => ({
      ...prevState,
      [notificationId]: !prevState[notificationId],
    }));

    if (!collapseShow[notificationId]) {
      await updateNotification(notificationId);
      
    }
  };


  const clarBtnClick=async()=>{
    const response=await clearNotification(context.user.userId) 
    if(response){
      Swal.fire({
        icon: STATUSMESSAGES.SUCCESS,  
        title: 'Notifications cleared successfully',  
        toast: true,  
        position: 'top-end',  
        showConfirmButton: false, 
        timer: 2500,  
        background: '#d4edda',  
        iconColor: '#155724',  
        color: '#155724'  
      });
      fecthNotifications();
    }else{
      Swal.fire({
        icon: STATUSMESSAGES.error,  
        title: STATUSMESSAGES.ERROR, 
        text: 'Error in clearing notifications', 
        toast: true,  
        position: 'top-end',  
        showConfirmButton: false, 
        timer: 2500,  
        background: '#f8d7da',  
        iconColor: '#721c24',  
        color: '#721c24'  
      }); 
       }
  }

  return (
    <div className="page-content page-container" id="page-content">
      <div className="padding">
        <div className="row container d-flex justify-content-center">
          <div className="col-sm-8 col-md-6">
            <div
              id="modal-right"
              className={`modal fade ${style.modalBackgroundFade} notification-modal  ${
                openNotification ? "show" : ""
              }`}
              style={{ display: openNotification ? "block" : "none" }}
              data-backdrop="true"
            >
              <div
                className={`modal-dialog modal-right w-xl modalHeight scrollNotificationModal`}
              >
                <div className="modal-content no-radius">
                  <div className="modal-header">
                    <div className="modal-title text-md">Notifications</div>
                    <button
                      className="close"
                      data-dismiss="modal"
                      onClick={() => setOpenNotification(false)}
                    >
                      &times;
                    </button>
                  </div>
                  <div className="modal-body">
                    <div className="p-4 text-center">
                     
                      {notificationData.length === 0 && (
                        <p>No latest notifications available</p>
                      )}
                      {notificationData?.map((notification) => {
                        return (
                          <div id="accordion" key={notification.notificationId}>
                            <div className="card m-3 shadow-sm rounded-lg">
                              <div
                                className="card-header"
                                id={`heading${notification.notificationId}`}
                              >
                                <h5 className="mb-0">
                                  <button
                                    className="btn btn-link"
                                    data-toggle="collapse"
                                    data-target={`#collapse${notification.notificationId}`}
                                    aria-expanded="true"
                                    aria-controls={`collapse${notification.notificationId}`}
                                    onClick={() =>
                                      onNotificationSeen(notification.notificationId)
                                    }
                                  >
                                    <strong>{notification.notificationTitle}</strong>
                                  </button>
                                </h5>
                              </div>

                              <div
                                id={`collapse${notification.notificationId}`}
                                className={`collapse ${
                                  collapseShow[notification.notificationId] ? "show" : ""
                                }`}
                                aria-labelledby={`heading${notification.notificationId}`}
                                data-parent="#accordion"
                              >
                                <div className="card-body">
                                  <p>{notification.notificationMessage}</p>
                                  <small>
                                    <i>Received at: {new Date(notification.createdAt).toLocaleString()}</i>
                                  </small>
                                </div>
                              </div>
                            </div>
                          </div>
                        );
                      })}
                    </div>
                  </div>
                  <div className="modal-footer">
                  <button
                  type="button"
                  className="btn btn-warning"
                  onClick={()=>clarBtnClick()}
                  disabled={notificationData.length===0}
                  >
                    Clear
                  </button>
                    <button
                      type="button"
                      className="btn btn-secondary"
                      data-dismiss="modal"
                      onClick={() => setOpenNotification(false)}
                    >
                      Close
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
    </div>
  );
};

export default NotificationRightModal;
