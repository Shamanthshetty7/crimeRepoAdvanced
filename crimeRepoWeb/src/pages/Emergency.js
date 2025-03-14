import React, { useContext, useEffect, useState } from 'react';
import { fetchAllEmergencyNumbers } from '../service/EmergencyService';
import style from "../styles/CustomStyles.module.css";
import EmergencyCentersMap from '../component/EmergencyCentersMap';
import Swal from 'sweetalert2';
import { sendEmergencyAlertNotification } from '../service/NotificationService';
import AuthContext from '../store/AuthContext';


const Emergency = () => {
  const [emergencyList, setEmergencyList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isMapLoading,setIsMapLoading]=useState(false);
  const [emergencyMap,setEmergencymap]=useState(false);
  const emergencyAlertIcon='/images/emergency-alert.png'
  const context=useContext(AuthContext)

  const fetchEmergencies = async () => {
    const response = await fetchAllEmergencyNumbers();
   
    setIsLoading(false);
    if (response.status) {
      setEmergencyList(response.data);
    }
  };

  useEffect(() => {
 
    fetchEmergencies();
  }, []);

  const Loader=()=>{
return  <div className="d-flex justify-content-center w-100 align-items-center">
<div className="spinner-border" role="status">
  <span className="visually-hidden">Loading...</span>
</div>
</div>
  }


  const handleEmergencyAlert=()=>{
    Swal.fire({
      title: 'Send Emergency Alert?',
      text: 'Do you want to send an emergency alert?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, send it!',
      cancelButtonText: 'No, cancel',
    }).then((result) => {
      if (result.isConfirmed) {
        const notificationTitle = 'Emergency Alert';
        const notificationMessage = 'A user sent an emergency alert please take action!';
        

        sendEmergencyAlertNotification(notificationTitle, notificationMessage,context)
          .then((response) => {
            if (response) {
              Swal.fire('Sent!', 'Your emergency alert has been sent.', 'success');
            } else {
              Swal.fire('Failed!', 'Failed to send the emergency alert.', 'error');
            }
          });
      }
    });
  }
  return (
    
    <div className={`emergency-view w-100 d-flex justify-content-center align-items-center flex-column p-2 ${style.mainDivBackgorundColor}`}>
      <h2 className="mb-4">Emergency Contacts</h2>
      <div className='Browse-emergency w-75 d-flex justify-content-end'>
         <button className='btn btn-info 'onClick={()=>setEmergencymap((prev)=>!prev)} >{emergencyMap?"close Map": "Browse Emergency Map"}</button>
         <img
          src={emergencyAlertIcon}
          alt="Emergency Icon"
          className={`ms-3 ${style.cursorPointer}`}
          style={{ width: '40px', height: '40px' }}
          onClick={handleEmergencyAlert}
        />
      </div>
      {isLoading ? (
       <Loader/>
      ) : (
        
          <div className='emergency-container container p-3 m-3 border border-dark'>
          {emergencyMap && <div
                className={`${
                  isMapLoading ? style.mapLoading : style.mapLoaded
                } `}
              >
                <EmergencyCentersMap setIsMapLoading={setIsMapLoading}/>
              </div>}
          {    emergencyList.length > 0 ?  (  <table className="table">
              <thead >
                <tr className='table-warning border border-dark'>
                  <th scope="col">Contact Type</th>
                  <th scope="col">Contact Number</th>
                </tr>
              </thead>
              <tbody>
                {emergencyList.map((emergency) => (
                  <tr key={emergency.emergencyId} className='border border-dark'>
                    <td>{emergency.emergencyContactType}</td>
                    <td>{emergency.emergencyContactNumber}</td>
                  </tr>
                ))}
              </tbody>
            </table>
      ):(
        <div className='emergency-container container p-3 m-2 border border-dark '>
            <p>No Emergency Numbers Available</p>
          </div>
      )}
          </div>
      )
         
          
        
      }
    </div>
  );
};

export default Emergency;