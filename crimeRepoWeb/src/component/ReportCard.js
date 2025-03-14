import { Fragment, useEffect, useState } from "react";
import styles from "../styles/CustomStyles.module.css";
import {
  deleteReport,
  saveRepupdateReportVote,
  updateReport,
} from "../service/ReportsService";

import ReportSubmitForm from "./ReportSubmitForm";
import DetailedReportInformation from "./DetailedReportInformation";
import PersonIcon from "@mui/icons-material/Person";
import UserDetails from "./UserDetails";
import { Tooltip } from "@mui/material";

import { saveNotification } from "../service/NotificationService";

import AccessTimeIcon from '@mui/icons-material/AccessTime';
import NewReleasesIcon from '@mui/icons-material/NewReleases';
import RemoveCircleIcon from "@mui/icons-material/RemoveCircle";
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import {
  getAllReportVotes,

} from "../service/ReportVoteService";
import {
  MDBCard,
  MDBCardBody,
  MDBCardImage,
  MDBCol,
  MDBRow,
  MDBTypography,
} from "mdb-react-ui-kit";
import {
  MESSAGES,
  REPORTMESSAGES,
  STATUSMESSAGES,
  USERTYPE,
} from "../utils/Messages";

import ReportVote from "./ReportVote";
const ReportCard = ({
  user,
  reports,
  type,
  fetchReports,
  Swal,
  InvestigationReportFilter,
  locationFilter,
}) => {
  const [reportEditForm, setReportEditForm] = useState(false);
  const [currentReport, setCurrentReport] = useState([]);
  const [detailedReportForm, SetDetailedReportForm] = useState(false);
  const [userDetailModal, SetUserDetailModal] = useState(false);
  const [reportData, setReportData] = useState({});
  const [allReportVotes, setAllReportVotes] = useState([]);
  const [currentPage, setCurrentPage] = useState(1); 
  let isReportPresent = true;

  const reportsPerPage = 5;

  const lastReportIndex = currentPage * reportsPerPage;
  const firstReportIndex = lastReportIndex - reportsPerPage;

  let totalPages ;
  const currentReports =()=>{
    if(type==="myReports"){
     
      reports= reports.filter(report => report.user.userId === user?.userId&&report.isActive);
     
    }else if(type==="investigatorReport"){
      reports= reports.filter(report => report.reportStatus === InvestigationReportFilter);
      if(reports.length===0){
        isReportPresent=false;
      }
      
    }else if(type==="allReports"){
      
      reports= reports.filter(report =>  locationFilter === null ||report.reportLocation.includes(locationFilter));
    }
    totalPages= Math.ceil(reports.length / reportsPerPage);
    return reports.slice(firstReportIndex, lastReportIndex);
  } 


  


  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

 
  
  const renderPagination = () => {
    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }

    return (
      <div className="pagination-container d-flex justify-content-center m-3  gap-3">
        <button
          className="btn btn-outline-secondary"
          disabled={currentPage === 1}
          onClick={() => handlePageChange(currentPage - 1)}
        >
          Previous
        </button>
        {pageNumbers.map((number) => (
          <button
            key={number}
            className={`btn btn-outline-secondary ${currentPage === number ? 'active' : ''}`}
            onClick={() => handlePageChange(number)}
          >
            {number}
          </button>
        ))}
        <button
          className="btn btn-outline-secondary"
          disabled={currentPage === totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
        >
          Next
        </button>
      </div>
    );
  };

  const onReportDeleteBtnClick = async (event, reportId) => {
    event.stopPropagation();
    try {
      const result = await Swal.fire({
        title: MESSAGES.areYouSure,
        text: "You won't be able to revert this!",
        icon: STATUSMESSAGES.WARNING,
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
      });

      if (result.isConfirmed) {
        const response = await deleteReport(reportId);

        if (response) {
          fetchReports();
          Swal.fire(
            STATUSMESSAGES.DELETED,
            "Your file has been deleted.",
            STATUSMESSAGES.SUCCESS
          );
        } else {
          Swal.fire(
            STATUSMESSAGES.ERROR,
            STATUSMESSAGES.WENTWRONG,
            STATUSMESSAGES.error
          );
        }
      }
    } catch (error) {
      Swal.fire(
        STATUSMESSAGES.ERROR,
        STATUSMESSAGES.WENTWRONG,
        STATUSMESSAGES.error
      );
    }
  };

  

  const fetchReportVotes = async () => {
    const reportVoteResponse = await getAllReportVotes();

    if (reportVoteResponse != null) {
      setAllReportVotes(reportVoteResponse);
    }
  };

  useEffect(() => {
    
    if (user != null) {
      fetchReportVotes();
    }
  }, [user]);



  const onReportEditBtnClick = (event, reportData) => {
    event.stopPropagation();
    setCurrentReport(reportData);
    setReportEditForm(true);
    fetchReports();
  };

  const onStatusChange = async (reportData, status) => {
    try {
      let response;

      const updatedReportData = {
        ...reportData,
        reportStatus: status,
      };
      delete updatedReportData.reportImage;
      response = await updateReport(updatedReportData);

      if (response.status) {
        //sending notification
        await saveNotification(
          REPORTMESSAGES.reportStatusUpdate,
          `Your submitted report is ${
            status === "underInvestigationReport"
              ? "under Investigation"
              : "Resolved"
          }`,
          reportData.user.userId,
          USERTYPE.SPECIFICUSER
        );

        Swal.fire(
          STATUSMESSAGES.UPDATED,
          REPORTMESSAGES.reportStatusUpdate,
          STATUSMESSAGES.SUCCESS
        );
        fetchReports();
      } else {
        Swal.fire(
          STATUSMESSAGES.ERROR,
          REPORTMESSAGES.reportUpdatingIssue,
          STATUSMESSAGES.error
        );
      }
    } catch (error) {
      Swal.fire(
        STATUSMESSAGES.ERROR,
        REPORTMESSAGES.reportUpdatingIssue,
        STATUSMESSAGES.error
      );
    }
  };

  const onViewReportDetailsClick = (reportData) => {
    SetDetailedReportForm(true);
    setCurrentReport(reportData);
  };

  const handleUserIconOnClick = (e, reportData) => {
    e.stopPropagation();
   
    setReportData(reportData);
    SetUserDetailModal(true);
  };

 
 

 

  const getStatusBadgeColor = (status) => {
    switch (status) {
      case "removedReport":
        return "bg-danger";
      case "resolvedReport":
        return "bg-success";
      case "newReport":
        return "bg-info";
      case "underInvestigationReport":
        return "bg-warning";
      default:
        return "bg-primary";
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "newReport":
        return <NewReleasesIcon  className="text-warning" />;
      case "underInvestigationReport":
        return <AccessTimeIcon />;
      
        case "resolvedReport":
          return <CheckCircleOutlineIcon className="text-success"/>;
      case "removedReport":
        return <RemoveCircleIcon className="text-danger"/>;
       
      default:
        return null;
    }
  };

  const getStatusText = (type,status) => {
    switch (status) {
      case "newReport":
        return type==="investigatorReport"?"This report is new":"New";
      case "underInvestigationReport":
        return type==="investigatorReport"?"This report is under investigation":"Under Investigation";
      case "removedReport":
        return "This report has been removed";
        case "resolvedReport":
        return type==="investigatorReport"?"This report  is been resolved":"Resolved";
      default:
        return "Status unknown";
    }
  };

  const Card = ({ reportData }) => {
    switch (type) {
      case "allReports":
         
          return (
            <section key={reportData.reportId}>
              <MDBRow
                className={`justify-content-center m-2 ${styles.cursorPointer} `}
                onClick={() => onViewReportDetailsClick(reportData)}
              >
                <MDBCol md="12" lg="10">
                  <MDBCard className="text-dark border-0">
                    <MDBCardBody className={`p-4 ${styles.allReportCard}`}>
                      <div className="d-flex flex-start">
                        <MDBCardImage
                          className="rounded-circle shadow-1-strong me-3"
                          src={reportData.reportImage}
                          alt="avatar"
                          width="100"
                          height="100"
                        />
                        <div className="w-100 text-start">
                        <div className="row text-center">
                          <MDBTypography tag="h6" className="fw-bold mb-1 col-8">
                            {reportData.reportTitle}
                          </MDBTypography>
                          <div className="col text-end">
                          <span
                                className={`badge ${getStatusBadgeColor(
                                  reportData.reportStatus
                                )} ms-2 `}
                              >
                                {getStatusText("allReports",reportData.reportStatus)}
                              </span>
                            </div>
                              </div>
                          <div className="d-flex align-items-center mb-3">
                            <p className="mb-0">
                              {new Date(reportData.createdAt).toLocaleString()}{" "}
                
                            </p>
                          </div>
                          <p className="mb-0">{reportData.reportDescription}</p>
                        </div>
                      </div>
                      <div className="features text-end">
                       
                  <ReportVote fetchReports={fetchReports} reportData={reportData} user={user}  setAllReportVotes={setAllReportVotes} allReportVotes={allReportVotes} fetchReportVotes={fetchReportVotes}/>
                      </div>
                    </MDBCardBody>

                    <hr className="my-2" />
                  </MDBCard>
                </MDBCol>
              </MDBRow>
            </section>
          );
       

      case "myReports":
       
      
        return (
     
          
            <div
              className="col-md-4 mb-4"
              onClick={() => onViewReportDetailsClick(reportData)}
            >
              <div
                className={`card border-light d-flex w-100 p-2 ${styles.card}`}
                key={reportData.reportId}
              >
                <img
                  className={`${styles.cardImg} rounded`}
                  src={reportData.reportImage}
                  alt="Card  cap"
                />
                <div className="card-body d-flex flex-column justify-content-between">
                  <h5 className="card-title">{reportData.reportTitle}
                  
                  </h5>
                  <p className="card-text">{reportData.reportDescription}</p>

                  <div className="d-flex justify-content-end">
                    <div className="features">
                    <ReportVote fetchReports={fetchReports} reportData={reportData} user={null}  setAllReportVotes={setAllReportVotes} />

                    </div>
                    <button
                      className="btn btn-success m-1"
                      onClick={(event) =>
                        onReportEditBtnClick(event, reportData)
                      }
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-danger m-1"
                      onClick={(event) =>
                        onReportDeleteBtnClick(event, reportData.reportId)
                      }
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            </div>
         
        );
      case "investigatorReport":
        return (
          <div className="col-md-4 mb-4">
            <div
              className={`card border-light d-flex w-100 p-2 ${styles.card}`}
              key={reportData.reportId}
            >
              <img
                className={`${styles.cardImg} rounded`}
                src={reportData.reportImage}
                alt="Card img cap"
              />
              <div className="card-body d-flex flex-column justify-content-between">
              <div className="d-flex w-100 justify-content-between">
              <h5 className="card-title"><strong>{reportData.reportTitle}</strong>
              </h5>
                <Tooltip title={getStatusText("investigatorReport",reportData.reportStatus)} placement="top">
                <span className="ms-2">
                  {getStatusIcon(reportData.reportStatus)}
                </span>
              </Tooltip>
              </div>

              <div className="d-flex w-100 justify-content-start">

                <p className="card-text text-start">{reportData.reportDescription}</p>
                </div>
                 
              
            
                <div className="d-flex justify-content-end align-items-center mt-3">
                  <div className="dropdown dropend">
                    {reportData.reportStatus !== "removedReport" && (
                      <i
                        className="bi bi-pencil m-2 dropdown-toggle"
                        id="edit-status"
                        data-bs-toggle="dropdown"
                        aria-expanded="false"
                      >
                        Edit Status
                      </i>
                    )}
                    <div
                      className="dropdown-menu  "
                      aria-labelledby="edit-status"
                    >
                      <button
                        className="dropdown-item"
                        onClick={() =>
                          onStatusChange(reportData, "underInvestigationReport")
                        }
                      >
                        Under Investigation
                      </button>
                      <button
                        className="dropdown-item"
                        onClick={() =>
                          onStatusChange(reportData, "resolvedReport")
                        }
                      >
                        Resolved
                      </button>

                      <button
                        className="dropdown-item"
                        onClick={(e) =>
                          onReportDeleteBtnClick(e, reportData.reportId)
                        }
                      >
                        Remove Report
                      </button>
                    </div>
                  </div>
                </div>

                <div className="d-flex justify-content-end align-items-center mt-3">
                <ReportVote fetchReports={fetchReports} reportData={reportData} user={null}  setAllReportVotes={setAllReportVotes} />


                  <PersonIcon
                    onClick={(e) => handleUserIconOnClick(e, reportData)}
                    className={`me-3 ${styles.cursorPointer}`}
                  />
                  <button
                    className={`btn ${styles.primaryButton} `}
                    onClick={() => onViewReportDetailsClick(reportData)}
                  >
                    View Details
                  </button>
                </div>
              </div>
            </div>
          </div>
        );

      default:
        return (
          <div>
            <p>Type error</p>
          </div>
        );
    }
  };


 

  return (
    <>
      {detailedReportForm && (
        <DetailedReportInformation
          detailedReportForm={detailedReportForm}
          SetDetailedReportForm={SetDetailedReportForm}
          currentReport={currentReport}
        />
      )}
      {userDetailModal && (
        <UserDetails
          SetUserDetailModal={SetUserDetailModal}
          userDetailModal={userDetailModal}
          user={reportData.user}
        />
      )}
      <ReportSubmitForm
        viewReportForm={reportEditForm}
        setViewReportForm={setReportEditForm}
        currentReport={currentReport}
        Swal={Swal}
        fetchReports={fetchReports}
      />

      {type === "investigatorReport" ? (
        <div className="row justify-content-center">
          {currentReports()?.filter(
            (report) => report.reportStatus === InvestigationReportFilter
          ).length === 0 ? (
             
               <p>No {InvestigationReportFilter} Available</p>
          ) : (
            currentReports()
              ?.filter(
                (report) => report.reportStatus === InvestigationReportFilter
              )
              .map((report) => (
                <Card key={report.reportId} reportData={report} />
              ))
          )}
        </div>
      ) : (
        <div className="row justify-content-center">
          {currentReports()
            ?.filter((report) => report.isActive)
            .map((report) => (
              <Card key={report.reportId} reportData={report} />
            ))}
        </div>
      )}
      {isReportPresent && renderPagination()}
    </>
  );
};

export default ReportCard;
