import React, { useContext, useEffect, useState } from 'react';
import { fetchAllEmergencyNumbers, deleteEmergencyNumber } from '../service/EmergencyService';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../store/AuthContext';

import style from "../styles/CustomStyles.module.css";
import EmergencyForm from '../component/EmergencyForm';

const InvestigatorEmergency = () => {
  
  const [emergencyList, setEmergencyList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [viewEmergencyForm, setViewEmergencyForm] = useState(false);
  const [selectedEmergency, setSelectedEmergency] = useState(null);
  const context=useContext(AuthContext);
  const navigate = useNavigate();
  const fetchEmergencies = async () => {
    const response = await fetchAllEmergencyNumbers();
    setIsLoading(false);
    if (response.status) {
      setEmergencyList(response.data);
    }
  };

  const DeleteBtnClick = async (emergencyId) => {
    const result = await Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
    });

    if (result.isConfirmed) {
      const response = await deleteEmergencyNumber(emergencyId);
      if (response.status) {
        Swal.fire('Deleted!', 'The emergency number has been deleted.', 'success');
        fetchEmergencies();
      } else {
        Swal.fire('Error!', 'There was an error deleting the emergency number.', 'error');
      }
    }
  };

  useEffect(() => {
    if (context.user==null||context?.user?.userType === "Informant") {
      navigate("/pageNotFound");
    }else{
      fetchEmergencies();
    }
    
  }, []);

  const AddEmergency = () => {
    setSelectedEmergency(null);
    setViewEmergencyForm(true);
  };

  const EditEmergency = (emergency) => {
    setSelectedEmergency(emergency);
    setViewEmergencyForm(true);
  };

  return (
    <div className={`emergency w-100 d-flex justify-content-center align-items-center flex-column p-2 ${style.mainDivBackgorundColor}`}>
      <div className='filter-btns w-75 d-flex justify-content-end '>
        <button className='btn btn-warning m-2 ' onClick={AddEmergency}>Add Emergency</button>
      </div>
      {isLoading ? (
        <div className="d-flex justify-content-center w-100 align-items-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        emergencyList.length > 0 ? (
          <div className='emergency-container container p-3 m-3 border border-dark'>
            <table className="table">
              <thead>
                <tr>
                  <th scope="col">Contact Type</th>
                  <th scope="col">Contact Number</th>
                  <th scope="col">Actions</th>
                </tr>
              </thead>
              <tbody>
                {emergencyList.map((emergency) => (
                  <tr key={emergency.emergencyId}>
                    <td>{emergency.emergencyContactType}</td>
                    <td>{emergency.emergencyContactNumber}</td>
                    <td>
                      <button className={`btn ${style.secondaryButton} m-2`} onClick={() => EditEmergency(emergency)}>Edit</button>
                      <button className={`btn ${style.dangerBtn} m-2`} onClick={() => DeleteBtnClick(emergency.emergencyId)}>Delete</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className='emergency-container container p-3 m-3 border border-dark'>
            <p>No Emergency Numbers Available</p>
          </div>
        )
      )}
      <EmergencyForm
        viewEmergencyForm={viewEmergencyForm}
        setViewEmergencyForm={setViewEmergencyForm}
        toast={Swal}
        selectedEmergency={selectedEmergency}
        fetchEmergencies={fetchEmergencies}
      />
    </div>
  );
};

export default InvestigatorEmergency;