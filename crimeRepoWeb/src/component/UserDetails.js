import React, { useEffect, useState } from "react";
import { getUserProfileByUserId } from "../service/UserProfileService";
import {
  getKycApplicationByProfileId,
  updateKycStatus,
} from "../service/KycApplicationService";
import Swal from "sweetalert2";
import { deleteUser } from "../service/UsersService";
import { saveNotification } from "../service/NotificationService";
import style from "../styles/CustomStyles.module.css";
import {
  KYCMESSAGES,
  KYCTYPES,
  STATUSMESSAGES,
  USERTYPE,
} from "../utils/Messages";
const UserDetails = ({ userDetailModal, SetUserDetailModal, user }) => {
  const [userData, SetUserData] = useState({});
  const [userKycStatus, SetUserKycStatus] = useState(null);

  const fetchUserProfileData = async () => {
    const response = await getUserProfileByUserId(user?.userId);

    if (response.status) {
      SetUserData(response.data);
      const kycResponse = await getKycApplicationByProfileId(
        response.data?.profileId
      );
      if (kycResponse.status) {
        SetUserKycStatus(kycResponse.data);
      }
    } else {
      Swal.fire({
        title: "Error in fecthing user detail!",
        icon: STATUSMESSAGES.error,
        draggable: true,
      });
      SetUserDetailModal(false);
    }
  };
  useEffect(() => {
    fetchUserProfileData();
  }, [user]);

  const handleCancelKYC = async () => {
    const result = await Swal.fire({
      title: "Are you sure you want to cancel the KYC?",
      text: "This action cannot be undone.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, Cancel it!",
      cancelButtonText: "No, Keep it",
      reverseButtons: true,
    });

    // If user confirmed, proceed with the operation
    if (result.isConfirmed) {
      if (userKycStatus.kycVerificationStatus === KYCTYPES.REJECTED) {
        return;
      }
      userKycStatus.kycVerificationStatus = KYCTYPES.REJECTED;

      await saveNotification(
        STATUSMESSAGES.IMPORTANT,
        KYCMESSAGES.blockedKyc,
        user.userId,
        USERTYPE.SPECIFICUSER
      );

      const response = await updateKycStatus(userKycStatus);

      if (response.status) {
        Swal.fire({
          title: "KYC Cancelled successfully",
          icon: STATUSMESSAGES.SUCCESS,
          draggable: true,
        });
        fetchUserProfileData();
      }
    } else {
      Swal.fire({
        title: "KYC Cancellation was not performed",
        icon: "info",
        draggable: true,
      });
    }
  };

  const handleBlockUser = async () => {
    const result = await Swal.fire({
      title: "Are you sure you want to block this user?",
      text: "This action cannot be undone.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, Block it!",
      cancelButtonText: "No, Keep it",
      reverseButtons: true,
    });

    if (result.isConfirmed) {
      handleCancelKYC();

      // Block the user
      const response = await deleteUser(user?.userId);

      await saveNotification(
        "You are Blocked!",
        `To maintain the decorum of the platform, your account has been blocked by an Investigator! Please logout from the application.`,
        user.userId,
        USERTYPE.SPECIFICUSER
      );

      if (response) {
        Swal.fire({
          title: "User Blocked successfully",
          icon: STATUSMESSAGES.SUCCESS,
          draggable: true,
        });
        fetchUserProfileData();
      } else {
        Swal.fire({
          title: "Error in blocking user",
          icon: STATUSMESSAGES.ERROR,
          draggable: true,
        });
      }
    } else {
      Swal.fire({
        title: "User was not blocked",
        icon: "info",
        draggable: true,
      });
    }
  };

  return (
    <div
      className={`modal fade ${style.modalBackgroundFade} ${
        userDetailModal ? "show" : ""
      }`}
      style={{ display: userDetailModal ? "block" : "none" }}
      id="user-detail-modal"
      tabIndex="-1"
      aria-labelledby="user-detail-modal-label"
      aria-hidden="true"
    >
      <div className="modal-dialog " role="document">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title" id="user-detail-modal-label">
              User Details
            </h5>
            <button
              type="button"
              className="close"
              onClick={() => SetUserDetailModal(false)}
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body">
            <div className="container my-2">
              <div className="row justify-content-center">
                <div className="col-md-12">
                  <div className="card">
                    <div className="card-header text-center">
                      <h4>User Profile</h4>
                    </div>
                    <div className="card-body">
                      <div className="text-center">
                        <img
                          src={userData?.userProfileImage}
                          alt=""
                          className="rounded-circle img-fluid"
                        />
                      </div>

                      <div className="text-center my-3">
                        <h5>Name: {userData?.userName}</h5>
                        <p>
                          Age:{" "}
                          {userData?.userAge ? userData.userAge : "Not updated"}
                        </p>
                        <p>
                          Gender:{" "}
                          {userData?.userGender
                            ? userData.userGender
                            : "Not updated"}
                        </p>
                        <p>
                          Address:{" "}
                          {userData?.userAddress
                            ? userData.userAddress
                            : "Not updated"}
                        </p>
                        <p>
                          Alternative Number:{" "}
                          {userData?.userAlternativeNumber
                            ? userData.userAlternativeNumber
                            : "Not updated"}
                        </p>
                      </div>

                      <ul className="list-group">
                        <li className="list-group-item">
                          <strong>Profile created on:</strong> <br />
                          {userData?.createdAt
                            ? new Date(userData.createdAt).toLocaleString()
                            : "Profile Not created"}
                        </li>
                        <li className="list-group-item">
                          <strong>Profile updated on:</strong>
                          <br />
                          {userData?.updatedAt
                            ? new Date(userData.updatedAt).toLocaleString()
                            : "Profile Not updated"}
                        </li>
                      </ul>

                      <div className="d-flex justify-content-end align-items-center mt-4">
                        {userKycStatus?.kycVerificationStatus === "verified" ? (
                          <button
                            className="btn btn-success m-2"
                            onClick={() => handleCancelKYC()}
                          >
                            Cancel KYC
                          </button>
                        ) : (
                          <span className="text-danger fw-bold m-2">
                            {" "}
                            {userKycStatus?.kycVerificationStatus
                              ? "KYC " + userKycStatus?.kycVerificationStatus
                              : "User Not Submitted the KYC"}
                          </span>
                        )}
                        {user?.isActive ? (
                          <button
                            className="btn btn-warning m-2"
                            onClick={() => handleBlockUser()}
                          >
                            Block User
                          </button>
                        ) : (
                          <span className="text-danger fw-bold m-2">
                            Restricted User
                          </span>
                        )}
                      </div>
                    </div>
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

export default UserDetails;
