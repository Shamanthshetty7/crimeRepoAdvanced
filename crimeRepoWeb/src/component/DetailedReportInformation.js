import React, { useContext, useEffect, useRef, useState } from "react";
import "../styles/DetailedReportInformation.scss";
import style from "../styles/CustomStyles.module.css";
import CommentOnReports from "./CommentOnReports";
import InsertCommentIcon from "@mui/icons-material/InsertComment";
import Swal from "sweetalert2";
import AuthContext from "../store/AuthContext";

import AccessTimeIcon from "@mui/icons-material/AccessTime";
import NewReleasesIcon from "@mui/icons-material/NewReleases";
import RemoveCircleIcon from "@mui/icons-material/RemoveCircle";
import CheckCircleOutlineIcon from "@mui/icons-material/CheckCircleOutline";

const DetailedReportInformation = ({
  detailedReportForm,
  SetDetailedReportForm,
  currentReport,
}) => {
  const [commentVisbility, setCommentVisbility] = useState(false);
  const commentSectionRef = useRef(null);
  const context = useContext(AuthContext);

  const getStatusIcon = (status) => {
    switch (status) {
      case "newReport":
        return <NewReleasesIcon className="text-warning" />;
      case "underInvestigationReport":
        return <AccessTimeIcon />;

      case "resolvedReport":
        return <CheckCircleOutlineIcon className="text-success" />;
      case "removedReport":
        return <RemoveCircleIcon className="text-danger" />;

      default:
        return null;
    }
  };

  const handleCommentIconClick = () => {
    setCommentVisbility((prev) => !prev);
   
  };

  useEffect(() => {
    if (commentVisbility && commentSectionRef.current) {
      commentSectionRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [commentVisbility]);
  
  return (
    <div
      className={`modal fade  ${style.modalBackgroundFade} ${
        detailedReportForm ? "show" : ""
      }`}
      style={{ display: detailedReportForm ? "block" : "none" }}
      id="report-detail-modal"
      tabIndex="-1"
      aria-labelledby="report-detail-modal-label"
      aria-hidden="true"
    >
      <div className="modal-dialog modal-lg" role="document">
        <div className="modal-content">
          <div className="modal-header">
         
            <h5 className="modal-title">Detailed Report Information</h5>
            <button
              type="button"
              className="close"
              data-dismiss="modal"
              aria-label="Close"
              onClick={() => SetDetailedReportForm(false)}
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>

          <div className="modal-body row ">
            <div className="col-4">
              <img
                src={currentReport.reportImage}
                alt="Report"
                className={`report-image img-fluid rounded `}
              />
            </div>

            <div className="col-8 ">
              <div className=" d-flex w-100 justify-content-between">
                <p className="report-title">{currentReport.reportTitle}</p>
                <div className={` d-flex justify-content-end  ${style.cursorPointer}`}   title="view comments">
                
                   <InsertCommentIcon
              onClick={()=>handleCommentIconClick()}
              className={commentVisbility ? `${style.commentIcon}` : ""}
            />
            </div>
              </div>
              
              <div className="d-flex text-start">
                <p className="report-description">
                  <strong>Description:</strong>
                  {currentReport.reportDescription}
                </p>
              </div>
              <div className=" d-flex">
                <p className="report-location">
                  <strong>Location:</strong>
                  {currentReport.reportLocation}
                </p>
              </div>
              <div className="d-flex gap-4 text-start">
                <p className="text-muted">
                  <strong>Reported on:</strong>{" "}
                  {new Date(currentReport.createdAt).toLocaleString()}
                </p>
                <div className="status-badge border ">
                  <span className={`badge text-dark`}>
                    {currentReport.reportStatus}
                    {getStatusIcon(currentReport.reportStatus)}
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div className="p-3 m-3 border border-dark">
            <p className="report-detailedInfo">
              <strong>Detailed Information:</strong>
              <br></br>
              {currentReport.detailedInformation}
            </p>
          </div>

          <div className="comment-section" ref={commentSectionRef}>
           
            {commentVisbility && (
              <CommentOnReports
                currentReport={currentReport}
                Swal={Swal}
                context={context}
              />
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DetailedReportInformation;
