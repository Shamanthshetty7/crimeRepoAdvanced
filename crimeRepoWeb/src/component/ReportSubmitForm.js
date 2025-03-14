import React, { useEffect, useState } from "react";
import {
  addReport,
  addReportImage,
  updateReport,
} from "../service/ReportsService";
import style from "../styles/CustomStyles.module.css";
import { saveNotification } from "../service/NotificationService";
import { REPORTMESSAGES, STATUSMESSAGES, TYPES, USERTYPE } from "../utils/Messages";
import { getAllMatchedCities } from "../service/ExternalApiService";
import ValidateForm from "./ValidateForms";
const ReportSubmitForm = ({
  viewReportForm,
  setViewReportForm,
  user,
  toast,
  currentReport,
  Swal,
  fetchReports,
}) => {
  const EmptyImage = "/images/no-image-available.webp";
  const [image, setImage] = useState(null);
  const [reportTitle, setReportTitle] = useState("");
  const [reportLocation, setReportLocation] = useState("");
  const [reportDescription, setReportDescription] = useState("");
  const [reportDetailedInfo, setReportDetailedInfo] = useState("");
  const [cities, setCities] = useState(null);
 const [valiadtionError,setValidationError]=useState({});

 const fetchMatchedCities = async(e, city) => {
    e.preventDefault();
    const response=await getAllMatchedCities(city);
    if(response.status){
      setCities(response.data[0]?.PostOffice || []);
    }else{
      setCities([]);
    }
   
   
  };
  

  const handleCitySelect = async (city) => {
    setReportLocation( `${city.Name}, ${city.District}`);
    setCities(null);
  };

  const onReportImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      if (file) {
        const reader = new FileReader();
        reader.onloadend = () => {
          setImage(reader.result);
        };
        reader.readAsDataURL(file);
      }
    }
  };

  useEffect(() => {
    if (currentReport && currentReport.reportImage) {
      setImage(currentReport.reportImage);
      setReportTitle(currentReport.reportTitle || "");
      setReportLocation(currentReport.reportLocation || "");
      setReportDescription(currentReport.reportDescription || "");
      setReportDetailedInfo(currentReport.detailedInformation || "");
    }
  }, [currentReport]);

  const addReportToDatabase = async (newReportData, reportImage) => {
    const addNewReport = await addReport(newReportData);
    const addNewReportImage = await addReportImage(
      addNewReport.data.reportId,
      reportImage
    );
    if (addNewReport.status && addNewReportImage.status) {
      //sending notification
      await saveNotification(
        REPORTMESSAGES.newReport,
        user.userName + " submitted a new report.",
        user.userId,
        USERTYPE.INVESTIGATOR
      );
      Swal.fire({
        title:REPORTMESSAGES.reportSubmitted,
        icon: STATUSMESSAGES.SUCCESS,
        draggable: true,
      });
      setViewReportForm(false);
    } else {
      toast.error(addNewReport.message);
    }
  };

  const updateEditedReportToDatabse = async (
    updatedReportData,
    reportImage
  ) => {
    const updateNewReport = await updateReport(updatedReportData);

    if (reportImage && image != currentReport.reportImage) {
      const updateNewReportImage = await addReportImage(
        updatedReportData.reportId,
        reportImage
      );
      if (!updateNewReportImage.status) {
        Swal.fire(
          STATUSMESSAGES.ERROR,
          STATUSMESSAGES.WENTWRONG,
          STATUSMESSAGES.error
        );
        return;
      }
    }

    if (updateNewReport.status) {
      Swal.fire(STATUSMESSAGES.UPDATED, REPORTMESSAGES.reportUpdated, STATUSMESSAGES.SUCCESS);
      fetchReports();
      setViewReportForm(false);
    } else {
      Swal.fire(STATUSMESSAGES.ERROR, STATUSMESSAGES.WENTWRONG, STATUSMESSAGES.error);
    }
  };

  const onReportFormSubmit = async (e) => {
    e.preventDefault();
   
    let validationErrors;
    const reportImageFile =
      e.target[0].files[0] ||
      (await fetch(EmptyImage)
        .then((response) => response.blob())
        .then(
          (blob) =>
            new File([blob], "no-image-available.webp", { type: TYPES.imageWebp })
        ));

    
    if (currentReport) {
      const updatedReportData = {
        ...currentReport,
        reportTitle,
        reportLocation,
        reportDescription,
        detailedInformation: reportDetailedInfo,
      };
     
      delete updatedReportData.reportImage;
      validationErrors = ValidateForm(null,updatedReportData);
      setValidationError(validationErrors)
      if (Object.keys(validationErrors).length > 0) {
        setValidationError(validationErrors);
        return;
      }
  
      updateEditedReportToDatabse(updatedReportData, reportImageFile);
    } else {
      const newReportData = {
        reportTitle,
        reportLocation,
        reportDescription,
        detailedInformation: reportDetailedInfo,
        user: { userId: user.userId },
      };
      validationErrors = ValidateForm(null,newReportData);
      setValidationError(validationErrors)
      if (Object.keys(validationErrors).length > 0) {
        setValidationError(validationErrors);
        return;
      }
  
      addReportToDatabase(newReportData, reportImageFile);
    }
  };

  return (
    <div
      className={`modal fade ${style.modalBackgroundFade} ${
        viewReportForm ? "show" : ""
      }`}
      style={{ display: viewReportForm ? "block" : "none" }}
      id="report-form-modal"
      tabIndex="-1"
      aria-labelledby="report-form-modal-label"
      aria-hidden="true"
    >
      <div
        className={`modal-dialog modal-lg ${style.mdoalDesign}`}
        role="document"
      >
        <div className={`modal-content ${style.modalBackground}`}>
          <div className={`modal-header ${style.modalHeader}`}>
            <h5 className="modal-title" id="report-form-modal-label">
              {currentReport
                ? "Edit the Report Here"
                : "Submit the Report Here"}
            </h5>
            <button
              type="button"
              className="close"
              data-dismiss="modal"
              aria-label="Close"
              onClick={() => setViewReportForm(false)}
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>

          <div className="modal-body">
            <div className=" d-flex flex-column align-items-center  border border-dark p-3 shadow ">
              <div className="Report-uploaded-image border shadow m-2">
                {image && (
                  <img
                    src={image}
                    alt="Uploaded"
                    className={`img-fluid m-2 ${style.imageSizing}`}
                  />
                )}
              </div>
              
              <div className="Form-data d-flex justify-content-center  ">
                <form
                  onSubmit={(e) => onReportFormSubmit(e)}
                  className="d-flex justify-content-center flex-column align-items-center"
                >
                  <div className="form-group w-75 d-flex gap-3">
                    <label htmlFor="report-image" className="float-start fw-bold">
                      Upload Related Image{" "}
                    </label>
                    <input
                      type="file"
                      className="form-control-file "
                      id="report-image"
                      onChange={onReportImageChange}
                    />
                  </div>
                  <div className="d-flex gap-3 border p-3 mt-3 shadow">
                    <div className=" mt-3  d-flex flex-column  ">
                      <div className="form-group mt-3 text-start">
                        <label htmlFor="report-title" className="fw-bold">Report Title</label>
                        <input
                          type="text"
                          className="form-control "
                          id="report-title"
                          value={reportTitle}
                          onChange={(e) => setReportTitle(e.target.value)}
                          placeholder="Enter Report Title"
                          required
                        />
                 {valiadtionError?.reportTitle && <span className="text-danger">{valiadtionError.reportTitle}</span>}

                      </div>
                      <div className="form-group mt-3  ">
                        <label
                          htmlFor="report-description"
                          className="float-start fw-bold"
                        >
                          Small Description
                        </label>
                        <input
                          className="form-control "
                          id="report-description"
                          value={reportDescription}
                          onChange={(e) => setReportDescription(e.target.value)}
                          placeholder=" Enter a small description"
                          required
                        ></input>
                  {valiadtionError?.reportDescription && <span className="text-danger">{valiadtionError.reportDescription}</span>}
                      </div>
                      <div className="form-group mt-3 text-start">
                        <label htmlFor="report-location" className="fw-bold">Location</label>
                        
                        <input
                          type="text"
                          className="form-control "
                          id="report-location"
                          value={reportLocation}
                          onChange={(e) => setReportLocation(e.target.value)}
                          placeholder="location"
                          required
                        />
                   {valiadtionError?.currentCity && <span className="text-danger">{valiadtionError.currentCity}</span>}

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
                        <div className='m-2 text-end'>
                        <button className='btn btn-primary  ' onClick={(e)=>fetchMatchedCities(e,reportLocation)}>Search</button>
                        </div>
                      </div>
                     

                      
                    </div>

                    <div className=" mt-3 ">
                      <div className="form-group mt-3 ">
                        <label
                          htmlFor="report-detailed-information "
                          className="float-start fw-bold"
                        >
                          Detailed Information
                        </label>
                        <textarea
                          className="form-control "
                          id="report-detailed-information"
                          value={reportDetailedInfo}
                          onChange={(e) =>
                            setReportDetailedInfo(e.target.value)
                          }
                          placeholder="Enter Detailed Information"
                          rows="8"
                          required
                        ></textarea>
                     {valiadtionError?.detailedInformation && <span className="text-danger">{valiadtionError.detailedInformation}</span>}

                      </div>
                    </div>
                  </div>
                  <div className="buttons text-end w-100 m-3">
                    <button
                      type="button"
                      className="btn btn-secondary m-2"
                      data-dismiss="modal"
                      onClick={() => setViewReportForm(false)}
                    >
                      Close
                    </button>
                    <button type="submit" className="btn btn-primary m-2">
                      {currentReport ? "Update" : "Submit"}
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportSubmitForm;
