import React, { useContext, useEffect, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import AuthContext from "../store/AuthContext";

import {
  addUserProfile,
  addUserProfileImage,
  getUserProfileByUserId,
  updateUserProfile,
} from "../service/UserProfileService";
import Swal from "sweetalert2";
import { toast, ToastContainer } from "react-toastify";
import { getInvestigationCentreByInvestigatorCode } from "../service/InvestigationCentreService";
import style from "../styles/CustomStyles.module.css";
import KYCform from "../component/KYCform";
import { getKycApplicationByProfileId } from "../service/KycApplicationService";
import {
  KYCMESSAGES,
  PROFILEMESSAGES,
  STATUSMESSAGES,
  USERTYPE,
} from "../utils/Messages";
import ValidateForm from "../component/ValidateForms";
const EmptyUserProfileImage = "/images/empty-profile-image.jpg";

const UserProfile = () => {
  const context = useContext(AuthContext);
  const navigate = useNavigate();

  const [values, setValues] = useState({});
  const [saveProfileFlag, setSaveProfileFlag] = useState(false);
  const [image, setImage] = useState(null);
  const [selectedImage, setSelectedImage] = useState(null);
  const [investgationCentre, setInvestigationCentreCode] = useState(null);
  const [viewKycForm, setViewKycForm] = useState(false);
  const [kycStatus, setKycStatus] = useState(null);
  const [valiadtionError, setValidationError] = useState({});

  const onUserProfileImageChange = (event) => {
    const file = event.target.files[0];
    setSelectedImage(file);
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

  const fecthKycStatus = async (profileId) => {
    if (profileId === undefined) {
      return;
    }
    const response = await getKycApplicationByProfileId(profileId);

    if (response.status) {
      setKycStatus(response.data.kycVerificationStatus);
    }
  };

  const fetchUserProfileIfExist = async () => {
    if (context.user) {
      const userProfileData = await getUserProfileByUserId(context.user.userId);
      setValues({
        ...userProfileData.data,
        userName: context.user?.userName,
        userEmail: context.user?.userEmail,
        userPhoneNumber: context.user?.userPhoneNumber,
        user: { userId: context.user?.userId },
        userId: context.user?.userId,
      });

      context.setUserProfileImage(userProfileData.data?.userProfileImage);
      setImage(userProfileData.data?.userProfileImage);

      if (context.user.userType === USERTYPE.INFORMANT) {
        fecthKycStatus(userProfileData.data?.profileId);
      }
    }
  };

  const fecthInvestigatorCentre = async () => {
    const response = await getInvestigationCentreByInvestigatorCode(
      context.user?.investigationCentreCode
    );

    setInvestigationCentreCode(response.data);
  };

  useEffect(() => {
    if (context.user == null) {
      navigate("/pageNotFound");
    }

    if (context.user) {
      setValues({
        userName: context.user.userName,
        userEmail: context.user.userEmail,
        userPhoneNumber: context.user.userPhoneNumber,
        user: {
          userId: context.user.userId,
        },
      });
    }
    fetchUserProfileIfExist();
    if (context.user?.userType === USERTYPE.INVESTIGATOR) {
      fecthInvestigatorCentre();
    }
  }, [viewKycForm]);

  const handleOnInputChange = (event) => {
    const { name, value } = event.target;
    setValues({
      ...values,
      [name]: value,
    });
  };

  const onApplyForKycClick = () => {
    if (values.profileId != undefined) {
      setViewKycForm(true);
    } else {
      Swal.fire({
        toast: true,
        position: 'top-right',
        icon: 'error',
        title: KYCMESSAGES.updateProfileForKyc,
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        background: '#f8d7da',
        iconColor: '#dc3545',
        customClass: {
          popup: 'colored-toast',
        },
      });    }
  };
  const onProfileDataSaveBtnClick = async (e) => {
    e.preventDefault();
    if (values.userName && values.userName !== context.user.userName) {
      context.user.userName = values.userName;
      context.saveUser();
    }

    let userProfileData;
    if (values.createdAt == null) {
      setValidationError({});
      const validationResult = ValidateForm(null, values);

      if (Object.keys(validationResult).length === 0) {
        userProfileData = await addUserProfile(values);

        setValues((prevState) => ({
          ...prevState,
          profileId: userProfileData.data?.profileId,
        }));
        context.setUserProfileId(userProfileData.data?.profileId);
      } else {
        setValidationError(validationResult);
        return;
      }
    } else {
      setValidationError({});
      const validationResult = ValidateForm(null, values);

      if (Object.keys(validationResult).length === 0) {
        userProfileData = await updateUserProfile(values);
      } else {
        setValidationError(validationResult);
        return;
      }
    }

    if (image !== EmptyUserProfileImage && image !== values.userProfileImage) {
      if (userProfileData?.data?.profileId) {
        userProfileData = await addUserProfileImage(
          userProfileData.data.profileId,
          selectedImage
        );
      }
    }

    if (userProfileData.status) {
      Swal.fire(
        STATUSMESSAGES.UPDATED,
        PROFILEMESSAGES.profileUpdated,
        STATUSMESSAGES.SUCCESS
      );
    } else {
      Swal.fire(
        STATUSMESSAGES.ERROR,
        PROFILEMESSAGES.profileUpdatingProblem,
        STATUSMESSAGES.ERROR
      );
    }

    setSaveProfileFlag(false);
  };

  return (
    <div
      className={`profile w-100 d-flex  flex-column  ${style.mainDivBackgorundColor}`}
    >
      <div
        className={`profile-container mb-3 container border border-dark  ${style.containerBackgroundColor}`}
      >
        <section>
          <div className="container ">
            <div className="row">
              <div className="col">
                <nav
                  aria-label="breadcrumb"
                  className="bg-body-secondary  rounded-3 p-3 mb-4 "
                >
                  <ol className="breadcrumb mb-0">
                    <li className="breadcrumb-item">
                      <NavLink to="/Reports">Home</NavLink>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                      User Profile
                    </li>
                  </ol>
                </nav>
              </div>
            </div>
            {context.user?.userType === "Investigator" && (
              <div className="d-flex justify-content-center mb-2">
                <p>
                  <strong>Investigation Centre Name </strong>
                  <br></br>
                  <u>{investgationCentre?.investigationCentreName}</u>
                </p>
              </div>
            )}
            {saveProfileFlag && (
              <div className="d-flex justify-content-end mb-2">
                <button
                  type="button"
                  className="btn btn-outline-primary ms-1"
                  onClick={() => setSaveProfileFlag(false)}
                >
                  Cancel
                </button>
              </div>
            )}
            {!saveProfileFlag && (
              <div className="d-flex justify-content-end mb-2">
                <button
                  type="button"
                  className="btn btn-outline-primary ms-1"
                  onClick={() => setSaveProfileFlag(true)}
                >
                  Edit Profile
                </button>
              </div>
            )}

            <div className="row bg-light">
              <div className={`col-lg-4  p-2  `}>
                <div className="card mb-4  bg-body-secondary">
                  <div className="card-body text-center">
                    {image ? (
                      <img
                        src={image}
                        alt="avatar"
                        className="rounded-circle img-fluid profile-image"
                      />
                    ) : (
                      <img
                        src={EmptyUserProfileImage}
                        alt="avatar"
                        className="rounded-circle img-fluid profile-image"
                      />
                    )}
                    {saveProfileFlag && (
                      <div className="d-flex justify-content-end">
                        <label htmlFor="report-image">
                          <i className="bi bi-pencil m-2 " id="edit-image"></i>
                        </label>
                        <input
                          type="file"
                          className="form-control-file"
                          id="report-image"
                          name="userProfileImage"
                          onChange={onUserProfileImageChange}
                         
                          style={{ display: "none" }}
                        />

                        
                      </div>
                    )}

                    <h5 className="my-3">{context.user?.userName}</h5>
                    <p className="text-muted mb-1">{context.user?.userType}</p>
                    {context.user?.userType === "Informant" && (
                      <>
                        {(kycStatus == null || kycStatus === "rejected") && (
                          <div className="d-flex justify-content-center mb-2">
                            <button
                              type="button"
                              className="btn btn-outline-primary ms-1"
                              onClick={() => onApplyForKycClick()}
                              disabled={saveProfileFlag}
                            >
                              Apply FOR KYC
                            </button>
                          </div>
                        )}
                        {kycStatus != null && (
                          <div className="d-flex justify-content-center mb-2">
                            <strong>
                              <p className="badge bg-success">
                                KYC {kycStatus}
                              </p>
                            </strong>
                          </div>
                        )}
                      </>
                    )}
                  </div>
                </div>
              </div>

              <div className="col-lg-8 bg-light p-2">
                <div className="card mb-4">
                  <form
                    className="card-body"
                    onSubmit={(e) => onProfileDataSaveBtnClick(e)}
                  >
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Full Name</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <input
                          className="text-muted mb-0"
                          type="text"
                          name="userName"
                          value={values.userName?values.userName:""}
                          onChange={handleOnInputChange}
                          disabled={!saveProfileFlag}
                          required
                        />
                        {valiadtionError?.username && (
                          <span className="text-danger">
                            {valiadtionError.username}
                          </span>
                        )}
                      </div>
                    </div>
                    <hr />
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Gender</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <select
                          className="text-muted mb-0 py-1 px-4"
                          name="userGender"
                          value={values?.userGender?values?.userGender:""}
                          onChange={handleOnInputChange}
                          disabled={!saveProfileFlag}
                          required
                        >
                          <option defaultValue=""  disabled>
                            Select Gender
                          </option>{" "}
                          <option value="Male">Male</option>
                          <option value="Female">Female</option>
                        </select>
                        {valiadtionError?.userGender && (
                          <span className="text-danger">
                            {valiadtionError.userGender}
                          </span>
                        )}
                      </div>
                    </div>

                    <hr />
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Age</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <input
                          className="text-muted mb-0"
                          type="number"
                          name="userAge"
                          value={values?.userAge?values?.userAge:""}
                          onChange={handleOnInputChange}
                          disabled={!saveProfileFlag}
                          required
                        />
                        {valiadtionError?.userAge && (
                          <span className="text-danger">
                            {valiadtionError.userAge}
                          </span>
                        )}
                      </div>
                    </div>
                    <hr />
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Email</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <input
                          className="text-muted mb-0"
                          type="email"
                          name="userEmail"
                          value={values?.userEmail?values?.userEmail:""}
                          onChange={handleOnInputChange}
                          disabled
                        />
                      </div>
                    </div>
                    <hr />
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Mobile Number</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <input
                          className="text-muted mb-0"
                          type="Number"
                          name="userPhoneNumber"
                          value={values?.userPhoneNumber?values?.userPhoneNumber:""}
                          onChange={handleOnInputChange}
                          minLength={10}
                          maxLength={10}
                          disabled
                        />
                      </div>
                    </div>
                    <hr />
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Alternative Mobile Number</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <input
                          className="text-muted mb-0"
                          type="Number"
                          name="userAlternativeNumber"
                          value={values?.userAlternativeNumber?values?.userAlternativeNumber:""}
                          onChange={handleOnInputChange}
                          disabled={!saveProfileFlag}
                        />
                        {valiadtionError?.userAlternativeNumber && (
                          <span className="text-danger">
                            {valiadtionError.userAlternativeNumber}
                          </span>
                        )}
                      </div>
                    </div>
                    <hr />
                    <div className="row">
                      <div className="col-sm-3">
                        <p className="mb-0">Address</p>
                      </div>
                      <div className="col-sm-9 d-flex flex-column">
                        <input
                          className="text-muted mb-0"
                          type="text"
                          name="userAddress"
                          value={values?.userAddress?values?.userAddress:""}
                          onChange={handleOnInputChange}
                          disabled={!saveProfileFlag}
                          required
                        />
                        {valiadtionError?.userAddress && (
                          <span className="text-danger">
                            {valiadtionError.userAddress}
                          </span>
                        )}
                      </div>
                      {saveProfileFlag && (
                        <div className="d-flex justify-content-end mb-2">
                          <button
                            type="submit"
                            className="btn btn-outline-primary m-2"
                          >
                            Save Profile
                          </button>
                        </div>
                      )}
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </section>
        <KYCform
          viewKycForm={viewKycForm}
          setViewKycForm={setViewKycForm}
          Swal={Swal}
          userProfileId={values.profileId}
          toast={toast}
        />
        <ToastContainer />
      </div>
    </div>
  );
};

export default UserProfile;
