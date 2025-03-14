import React, { useContext, useEffect, useState } from "react";
import CameraCapture from "./CameraCapture";
import { saveKycApplication } from "../service/KycApplicationService";
import style from "../styles/CustomStyles.module.css";
import { saveNotification } from "../service/NotificationService";
import {
  KYCMESSAGES,
  STATUSMESSAGES,
  TYPES,
  USERTYPE,
} from "../utils/Messages";
import { getAllMatchedCities } from "../service/ExternalApiService";
import AuthContext from "../store/AuthContext";
import ValidateForm from "./ValidateForms";

const KYCform = ({
  viewKycForm,
  setViewKycForm,
  Swal,
  userProfileId,
  toast,
}) => {
  const [kycImage, setKycImage] = useState(null);
  const [formValues, setFormValues] = useState({});
  const context = useContext(AuthContext);
  const [cities, setCities] = useState(null);
  const [valiadtionError, setValidationError] = useState({});

  const fetchLatLong = async () => {
    context.fetchLatLong();
    setFormValues({
      ...formValues,
      ...context.liveLocationLatLong,
    });
  };

  const onKycFormSubmit = async (e) => {
    e.preventDefault();

    if (kycImage) {
      formValues.userVerificationImage = base64ToFile(
        kycImage,
        "kyc-image",
        TYPES.imageType
      );
    }
    const validationErrors = ValidateForm(context.user, formValues);

    if (!formValues.userVerificationImage) {
      setValidationError({
        userVerificationImage: "Please capture your KYC image.",
      });

      return;
    }

    if (Object.keys(validationErrors).length > 0) {
      setValidationError(validationErrors);
      return;
    }

    const response = await saveKycApplication(userProfileId, formValues);
    if (response.status) {
      setViewKycForm(false);
      await saveNotification(
        KYCMESSAGES.newKyc,
        formValues.userFullName + " submitted a KYC Application.",
        0,
        USERTYPE.INVESTIGATOR
      );
      Swal.fire(
        STATUSMESSAGES.SUBMITTED,
        KYCMESSAGES.KYCSubmitted,
        STATUSMESSAGES.SUCCESS
      );
    } else {
      toast.error(response.message);
    }
  };

  const fetchMatchedCities = async (e, city) => {
    e.preventDefault();
    const response = await getAllMatchedCities(city);
    if (response.status) {
      setCities(response.data[0]?.PostOffice || []);
    } else {
      setCities([]);
    }
  };

  const handleOnInputChange = (event) => {
    const { name, value } = event.target;
    if (event.target.type === "file") {
      
      setFormValues({
        ...formValues,
        [name]: event.target.files[0],
      });
    } else {
      setFormValues({
        ...formValues,
        [name]: value,
      });
    }
  };

  useEffect(() => {
    fetchLatLong();
  }, []);

  const handleCitySelect = async (city) => {
    setFormValues((prevValues) => ({
      ...prevValues,
      currentCity: `${city.Name}, ${city.District}`,
    }));

    setCities(null);
  };

  // Convert base64 string to file
  const base64ToFile = (base64Data, filename, mimeType) => {
    const byteCharacters = atob(base64Data.split(",")[1]);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += 1024) {
      const slice = byteCharacters.slice(offset, offset + 1024);
      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }
      byteArrays.push(new Uint8Array(byteNumbers));
    }

    const blob = new Blob(byteArrays, { type: mimeType });

    return new File([blob], filename, { type: mimeType });
  };

  return (
    <div
      className={`modal fade ${style.modalBackgroundFade} ${
        viewKycForm ? "show" : ""
      }`}
      style={{ display: viewKycForm ? "block" : "none" }}
      id="kyc-application-modal"
      tabIndex="-1"
      aria-labelledby="kyc-application-modal-label"
      aria-hidden="true"
    >
      <div className={`modal-dialog modal-lg `} role="document">
        <div className={`modal-content ${style.modalBackground}`}>
          <div className={`modal-header ${style.modalHeader}`}>
            <h5 className="modal-title" id="kyc-application-modal-label">
              Apply for KYC
            </h5>
            <button
              type="button"
              className="close"
              onClick={() => setViewKycForm(false)}
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body">
            <div className="col-md-12 mt-5 border">
              <form onSubmit={(e) => onKycFormSubmit(e)}>
                <div className="form-group ">
                  <CameraCapture
                    kycImage={kycImage}
                    setKycImage={setKycImage}
                  />
                  {valiadtionError?.userVerificationImage && (
                    <span className="text-danger">
                      {valiadtionError.userVerificationImage}
                    </span>
                  )}
                </div>

                <div className="d-flex justify-content-center flex-column align-items-center border p-2 m-3 shadow">
                  <div className="d-flex gap-3 w-100 ">
                    <div className="form-group mt-2 text-start w-50">
                      <label htmlFor="user-full-name">Full Name</label>
                      <input
                        type="text"
                        name="userFullName"
                        className="form-control"
                        id="user-full-name"
                        onChange={handleOnInputChange}
                        placeholder="Enter your full name"
                        required
                      ></input>
                      {valiadtionError?.userFullName && (
                        <span className="text-danger">
                          {valiadtionError.userFullName}
                        </span>
                      )}
                    </div>
                    <div className="form-group mt-2 text-start w-50 d-flex align-items-end">
                      <div className="position-relative w-100">
                        <label htmlFor="current-city">Current City</label>
                        <input
                          type="text"
                          name="currentCity"
                          className="form-control"
                          id="current-city"
                          value={formValues.currentCity}
                          onChange={handleOnInputChange}
                          placeholder="Enter your current city name"
                          required
                        />
                        {valiadtionError?.currentCity && (
                          <span className="text-danger">
                            {valiadtionError.currentCity}
                          </span>
                        )}

                        {cities !== null && (
                          <ul
                            className={`dropdown-menu show ${style.scrollableDropdowns}`}
                          >
                            {cities.length > 0 ? (
                              cities.map((city) => (
                                <li
                                  key={city.Name}
                                  onClick={() => handleCitySelect(city)}
                                  className={`${style.dropdownItem}`}
                                >
                                  {city.Name}, {city.District}
                                </li>
                              ))
                            ) : (
                              <li className={`${style.dropdownItem}`}>
                                No City available
                              </li>
                            )}
                          </ul>
                        )}
                      </div>
                      <div>
                        <button
                          className="btn btn-info ms-2"
                          onClick={(e) =>
                            fetchMatchedCities(e, formValues.currentCity)
                          }
                        >
                          Find
                        </button>
                      </div>
                    </div>
                  </div>
                  <div className="d-flex gap-3 w-100">
                    <div className=" form-group mt-3 text-start d-flex flex-column w-50 ">
                      <label htmlFor="date">Enter your Date of Birth:</label>
                      <input
                        type="date"
                        id="date"
                        name="userDOB"
                        onChange={handleOnInputChange}
                        required
                      />
                      {valiadtionError?.userDOB && (
                        <span className="text-danger">
                          {valiadtionError.userDOB}
                        </span>
                      )}
                    </div>

                    <hr />

                    <div className="form-group mt-3 text-start d-flex flex-column w-50">
                      <label className="mr-2">
                        Upload your Adhaar Card file:
                      </label>
                      <input
                        type="file"
                        id="file"
                        name="userAdhaarFile"
                        onChange={handleOnInputChange}
                        required
                      />
                      {valiadtionError?.userAdhaarFile && (
                        <span className="text-danger">
                          {valiadtionError?.userAdhaarFile}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
                <hr />
                <div className="d-flex justify-content-end gap-1 align-items-center">
                  <span className="fs-6">
                    Note: Your Live Location will be Fetched!
                  </span>
                  <button
                    type="button"
                    className="btn btn-secondary m-2"
                    data-dismiss="modal"
                    onClick={() => setViewKycForm(false)}
                  >
                    Close
                  </button>
                  <button type="submit" className="btn btn-primary m-2">
                    Submit
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default KYCform;
