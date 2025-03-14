import React, { Fragment, useContext, useEffect, useState } from "react";
import ReportCard from "../component/ReportCard";
import AuthContext from "../store/AuthContext";
import ReportSubmitForm from "../component/ReportSubmitForm";
import { toast, ToastContainer } from "react-toastify";
import { getUserProfileKycStatusByUserId } from "../service/UserProfileService";
import LocationFilter from "../component/LocationFilter";
import style from "../styles/CustomStyles.module.css";
import Swal from "sweetalert2";
import CrimeRateMap from "../component/CrimeRateMap";
import { KYCMESSAGES, KYCTYPES, USERTYPE } from "../utils/Messages";
import NewsAdvertisement from "../component/NewsAdvertisement";
import { useNavigate } from "react-router-dom";
import ChatBot from "../component/ChatBot";

const Reports = () => {
  const context = useContext(AuthContext);
  const navigate = useNavigate();
  const [viewReportForm, setViewReportForm] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isMapLoading, setIsMapLoading] = useState(true);
  const [locationFilter, setLocationFilter] = useState(null);

  const onReportCrimeBtnClick = async () => {
    if (context.user == null) {
      context.setViewForm(true);
    } else {
      const kycStatus = await getUserProfileKycStatusByUserId(
        context.user?.userId
      );
      if (kycStatus.status) {
        if (kycStatus.data === KYCTYPES.UNDERVERIFICATION) {
          toast.error(KYCMESSAGES.KycUnderReview);
        } else if (kycStatus.data === KYCTYPES.REJECTED) {
          toast.error(KYCMESSAGES.KycNotEligible);
        } else {
          setViewReportForm(true);
        }
      } else {
        toast.error(KYCMESSAGES.KycCompleteProcess);
      }
    }
  };

  useEffect(() => {
    if(context?.user?.userType===USERTYPE.INVESTIGATOR){
      navigate('/InvestigatorReports')
    }
    context.fetchReports();
    
    setIsLoading(false);
  }, [viewReportForm, locationFilter,context.user]);

  return (
    <div
      className={`d-flex justify-content-center align-items-center  flex-column p-2 ${style.mainDivBackgorundColor}`}
    >
      <div className="d-flex w-100">
        <div className="crimeRateMap w-75 border m-3 shadow">
          <h4>Crime Rate Map</h4>
          {context.allReports == null ? (
            <div>No reports are done to display</div>
          ) : (
            <Fragment>
              {isMapLoading && (
                <div
                  className={`d-flex justify-content-center w-100 ${style.loaderStyle}`}
                >
                  <div className="spinner-border" role="status">
                    <span className="visually-hidden">Loading...</span>
                  </div>
                </div>
              )}

              <div
                className={`${
                  isMapLoading ? style.mapLoading : style.mapLoaded
                } `}
              >
                <CrimeRateMap
                  setIsMapLoading={setIsMapLoading}
                  context={context}
                />
              </div>
            </Fragment>
          )}
        </div>
        <div className={`news-advertisement w-25 `}>
          <NewsAdvertisement />
        </div>
      </div>
      <div className="d-flex  w-100 justify-content-end me-5">
        <LocationFilter
          context={context}
          setLocationFilter={setLocationFilter}
        />
      </div>
      <div
        className={`reports-container container p-3 m-3 border border-dark ${style.containerBackgroundColor}`}
      >
        <div className="col-12 d-flex justify-content-end">
          <button
            className={`btn m-2 ${style.primaryButton}`}
            onClick={() => onReportCrimeBtnClick()}
          >
            Report A Crime
          </button>
        </div>
        {isLoading ? (
          <div className="d-flex justify-content-center w-100">
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        ) : context.activeReportCount() ? (
          <ReportCard
            user={context.user}
            reports={context.allReports}
            type={"allReports"}
            locationFilter={locationFilter}
            fetchReports={context.fetchReports}
          />
        ) : (
          <p>No Reports Available</p>
        )}
        <ReportSubmitForm
          setViewReportForm={setViewReportForm}
          viewReportForm={viewReportForm}
          toast={toast}
          user={context.user}
          Swal={Swal}
        />
      </div>
      
      <ToastContainer/>
      <ChatBot className={`${style.cursorPointer}`} />
    </div>
  );
};

export default Reports;
