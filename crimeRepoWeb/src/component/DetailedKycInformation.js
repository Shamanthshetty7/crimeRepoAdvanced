import React, { useState } from "react";
import Swal from "sweetalert2";
import { updateKycStatus } from "../service/KycApplicationService";
import style from "../styles/CustomStyles.module.css";
import { saveNotification } from "../service/NotificationService";
import UserLocationMap from "./UserLocationMap";
import { KYCMESSAGES, MESSAGES, STATUSMESSAGES, TYPES, USERTYPE } from "../utils/Messages";

const DetailedKycInformation = ({
  kycApplicationData,
  viewKycDetailModal,
  setViewKycDetailModal,
}) => {
  const [verificationStatus, setVerificationStatus] = useState({
    imageVerified: false,
    nameVerified: false,
    dobVerified: false,
    addressVerified: false,
  });

  const [viewUserLocation, setViewUserLocation] = useState(false);

  //latttitude and longitude
  const latAndLong = kycApplicationData?.fecthedUserLocation
    ? kycApplicationData.fecthedUserLocation.split(" ")
    : [0, 0];

  // Function to handle the animated checkmark when verifying
  const handleVerify = (field) => {
    setVerificationStatus((prevState) => ({
      ...prevState,
      [field]: true,
    }));
  };

  //base64 to pdf conversion
  const Base64ToPdfDownload = (userAdhaarBase64String, userName) => {
    let cleanBase64String = userAdhaarBase64String;
    let mimeType =TYPES.mimeType;

    if (userAdhaarBase64String.startsWith(MESSAGES.bse64PDFPrefix)) {
      cleanBase64String = userAdhaarBase64String.split(
       MESSAGES.bse64PDFPrefix
      )[1];
    } else if (userAdhaarBase64String.startsWith("data:image")) {
      const match = userAdhaarBase64String.match(/^data:image\/(.*);base64,/);
      if (match) {
        mimeType = `image/${match[1]}`;
        cleanBase64String = userAdhaarBase64String.split(
          `data:image/${match[1]};base64,`
        )[1];
      }
    }

    const byteCharacters = atob(cleanBase64String);
    const byteArray = new Uint8Array(byteCharacters.length);

    for (let i = 0; i < byteCharacters.length; i++) {
      byteArray[i] = byteCharacters.charCodeAt(i);
    }

    const blob = new Blob([byteArray], { type: mimeType });
    const link = document.createElement("a");
    const url = URL.createObjectURL(blob);

    const fileExtension =
      mimeType === TYPES.mimeType ? ".pdf" : mimeType.split("/")[1];
    link.href = url;
    link.download = `${userName}${fileExtension}`;

    link.click();
    URL.revokeObjectURL(url);
  };

  const VerifyButton = ({ field, isVerified }) => {
    return (
      <button
        className="btn btn-warning m-3"
        onClick={() => handleVerify(field)}
      >
        {isVerified ||
        kycApplicationData.kycVerificationStatus != "underVerification" ? (
          <span className="verified-checkmark">&#10004; Verified</span>
        ) : (
          "Verify"
        )}
      </button>
    );
  };

  //finishing the kyc processs
  const onFinishBtnClick = async () => {
    if (
      verificationStatus.dobVerified &&
      verificationStatus.imageVerified &&
      verificationStatus.nameVerified &&
      verificationStatus.addressVerified
    ) {
      kycApplicationData.kycVerificationStatus = "verified";
      const kycStatusUpdationStatus = await updateKycStatus(kycApplicationData);
      
      if (kycStatusUpdationStatus.status) {
        await saveNotification(
          KYCMESSAGES.kycStatusUpdate,
          KYCMESSAGES.kycAccepted,
          kycApplicationData.userProfile?.userId,
          USERTYPE.SPECIFICUSER
        );
        Swal.fire({
          title: KYCMESSAGES.kycVerified,
          icon: STATUSMESSAGES.SUCCESS,
          draggable: true,
        });
        setViewKycDetailModal(false);
      } else {
        Swal.fire(STATUSMESSAGES.ERROR,STATUSMESSAGES.WENTWRONG);
      }
    } else {
      Swal.fire(STATUSMESSAGES.ATTENTION, KYCMESSAGES.kyvVerifyOrReject);
    }
  };

  const onRejectBtnClick = async () => {
    Swal.fire({
      title: KYCMESSAGES.kycRejectQuestion,
      confirmButtonText: "Ok",
      cancelButtonText: "Cancel",
      showCancelButton: true,
      showCloseButton: true,
    }).then(async (result) => {
      if (result.isConfirmed) {
        kycApplicationData.kycVerificationStatus = "rejected";
        const kycStatusUpdationStatus = await updateKycStatus(
          kycApplicationData
        );
       
        if (kycStatusUpdationStatus.status) {
          await saveNotification(
           KYCMESSAGES.kycStatusUpdate,
            KYCMESSAGES.kycRejectedNotification,
            kycApplicationData.userProfile?.userId,
            USERTYPE.SPECIFICUSER
          );
          Swal.fire(STATUSMESSAGES.REJECTED, KYCMESSAGES.kycRejected);
          setViewKycDetailModal(false);
        } else {
          Swal.fire(STATUSMESSAGES.ERROR, STATUSMESSAGES.WENTWRONG);
        }
      }
    });
  };

  return (
    <div
      className={`modal fade ${style.modalBackgroundFade}  ${
        viewKycDetailModal ? "show" : ""
      }`}
      style={{ display: viewKycDetailModal ? "block" : "none" }}
      id="report-form-modal"
      tabIndex="-1"
      aria-labelledby="report-form-modal-label"
      aria-hidden="true"
    >
      <div
        className={`modal-dialog modal-lg ${style.mdoalDesign}`}
        role="document"
      >
        <div className={`modal-content  ${style.modalBackground}`}>
          <div className={`modal-header ${style.modalHeader}`}>
            <h5 className="modal-title">Verify The User KYC Here</h5>
            <button
              type="button"
              className="close"
              data-dismiss="modal"
              aria-label="Close"
              onClick={() => setViewKycDetailModal(false)}
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body row">
            <div className="container container-fluid">
              <div className="kyc-image-verification d-flex flex-column justify-content-around align-items-center border border-dark m-2">
                <img
                  className="m-2 w-50 img-fluid "
                  src={kycApplicationData.userVerificationImage}
                  alt=""
                />
                <VerifyButton
                  field="imageVerified"
                  isVerified={verificationStatus.imageVerified}
                />
              </div>
              <div className="kyc-fullname-verification d-flex justify-content-around align-items-center border border-dark m-2">
                <p className="w-50 text-start">
                  <strong>User Full Name:</strong>{" "}
                  {kycApplicationData.userFullName}
                </p>
                <VerifyButton
                  field="nameVerified"
                  isVerified={verificationStatus.nameVerified}
                />
              </div>
              <div className="kyc-dob-verification d-flex justify-content-around align-items-center border border-dark m-2">
                <p className="w-50 text-start">
                  <strong>User Date of Birth:</strong>{" "}
                  {kycApplicationData.userDOB}
                </p>
                <VerifyButton
                  field="dobVerified"
                  isVerified={verificationStatus.dobVerified}
                />
              </div>
              <div className="kyc-address-verification d-flex flex-column justify-content-around align-items-center border border-dark m-2">
                <p className="w-50 ">
                  <strong>Entered City:</strong>{" "}
                  {kycApplicationData.currentCity}
                </p>
                <p className="w-50 ">
                  <strong>User Current Location:</strong>{" "}
                  <button
                    className="btn btn-primary"
                    onClick={() => setViewUserLocation((prev) => !prev)}
                  >
                    {!viewUserLocation ? "View Location" : "Close Location"}
                  </button>
                </p>

                {viewUserLocation && (
                  <div className="w-100 d-flex justify-content-center ">
                    <UserLocationMap
                      position={[latAndLong[0], latAndLong[1]]}
                    />
                  </div>
                )}
                <VerifyButton
                  field="addressVerified"
                  isVerified={verificationStatus.addressVerified}
                />
              </div>
              <div className=" d-flex justify-content-end align-items-center ">
                <button
                  className="btn btn-primary m-2"
                  onClick={() =>
                    Base64ToPdfDownload(
                      kycApplicationData.userAdhaarFile,
                      kycApplicationData.userFullName
                    )
                  }
                >
                  Download Adhaar Card
                </button>
                {kycApplicationData.kycVerificationStatus !== "rejected" && (
                  <button
                    className={`btn btn-danger m-2 ${style.dangerBtn}`}
                    onClick={() => onRejectBtnClick()}
                  >
                    {kycApplicationData.kycVerificationStatus ===
                    "underVerification"
                      ? "Reject KYC"
                      : "Cancel KYC"}
                  </button>
                )}
              </div>
              <div className=" d-flex justify-content-end align-items-center ">
                {kycApplicationData.kycVerificationStatus ===
                  "underVerification" && (
                  <button
                    className="btn btn-success m-2"
                    onClick={() => onFinishBtnClick()}
                  >
                    Finish
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DetailedKycInformation;
