import React, { useState, useEffect, useContext } from 'react';
import { addEmergencyNumber, updateEmergencyNumber } from '../service/EmergencyService';
import style from '../styles/CustomStyles.module.css';
import Swal from 'sweetalert2';
import { STATUSMESSAGES } from '../utils/Messages';
import { toast, ToastContainer } from 'react-toastify';
import AuthContext from '../store/AuthContext';

const EmergencyForm = ({ viewEmergencyForm, setViewEmergencyForm, selectedEmergency, fetchEmergencies }) => {
  const context = useContext(AuthContext);
  const [formValues, setFormValues] = useState({
    emergencyContactType: '',
    emergencyContactNumber: '',
  });
  const [isEditMode, setIsEditMode] = useState(false);

  useEffect(() => {
    if (selectedEmergency) {
      setIsEditMode(true);
      setFormValues(selectedEmergency);
    } else {
      setIsEditMode(false);
      setFormValues({
        emergencyContactType: '',
        emergencyContactNumber: '',
      });
    }
  }, [selectedEmergency]);

  const handleOnInputChange = (event) => {
    const { name, value } = event.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  const onEmergencyFormSubmit = async (e) => {
    e.preventDefault();
    let response;
    if (isEditMode) {
      response = await updateEmergencyNumber(formValues);
    } else {
      response = await addEmergencyNumber(context.user?.userId, formValues);
    }

    if (response.status) {
      setViewEmergencyForm(false);
      Swal.fire(
        STATUSMESSAGES.SUCCESS,
        isEditMode ? 'Emergency number updated successfully.' : 'Emergency number added successfully.',
        STATUSMESSAGES.SUCCESS
      );
    } else {
      toast.error(response.message);
    }
    fetchEmergencies();
  };

  return (
    <div
      className={`modal fade ${style.modalBackgroundFade} ${viewEmergencyForm ? 'show' : ''}`}
      style={{ display: viewEmergencyForm ? 'block' : 'none' }}
      id="emergency-modal"
      tabIndex="-1"
      aria-labelledby="emergency-modal-label"
      aria-hidden="true"
    >
      <div className={`modal-dialog modal-lg`} role="document">
        <div className={`modal-content ${style.modalBackground}`}>
          <div className={`modal-header ${style.modalHeader}`}>
            <h5 className="modal-title" id="emergency-modal-label">
              {isEditMode ? 'Edit Emergency Number' : 'Add Emergency Number'}
            </h5>
            <button
              type="button"
              className="close"
              onClick={() => setViewEmergencyForm(false)}
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body">
            <div className="col-md-12 mt-5 border border-dark shadow">
              <form onSubmit={(e) => onEmergencyFormSubmit(e)} className='d-flex flex-column justify-content-center'>
              <div className='d-flex gap-3'>
                <div className="form-group w-75 m-2 text-start">
                  <label htmlFor="emergency-contact-type " className='m-2 fw-bold'>Contact Type</label>
                  <input
                    type="text"
                    name="emergencyContactType"
                    className="form-control"
                    id="emergency-contact-type"
                    value={formValues.emergencyContactType}
                    onChange={handleOnInputChange}
                    placeholder="Enter contact type"
                    required
                  />
                </div>
                <div className="form-group w-75 m-2 text-start">
                  <label htmlFor="emergency-contact-number" className='m-2 fw-bold'>Contact Number</label>
                  <input
                    type="text"
                    name="emergencyContactNumber"
                    className="form-control"
                    id="emergency-contact-number"
                    value={formValues.emergencyContactNumber}
                    onChange={handleOnInputChange}
                    placeholder="Enter contact number"
                    required
                  />
                </div>
                </div>
                <div className="d-flex justify-content-end gap-1 align-items-center">
                  <button
                    type="button"
                    className="btn btn-secondary m-2"
                    data-dismiss="modal"
                    onClick={() => setViewEmergencyForm(false)}
                  >
                    Close
                  </button>
                  <button type="submit" className="btn btn-primary m-2">
                    {isEditMode ? 'Update' : 'Submit'}
                  </button>
                </div>
              </form>
            </div>
            <ToastContainer />
          </div>
        </div>
      </div>
    </div>
  );
};

export default EmergencyForm;