import React, { useState } from "react";
import "../styles/RegisterLogin.scss";
import { addUser, chekcUserLogin } from "../service/UsersService";
import { useNavigate } from "react-router-dom";
import { saveNotification } from "../service/NotificationService";
import style from "../styles/CustomStyles.module.css";
import { LOGINLOGOUT, STATUSMESSAGES, USERTYPE } from "../utils/Messages";
import ValidateForm from "./ValidateForms";
import { checkVerifiedEmail } from "../service/ExternalApiService";

const RegisterLogin = ({
  viewForm,
  setViewForm,
  Swal,
  setCurrentUser,
  context,
  toast,
}) => {
  const [investigator, setInvestigator] = useState(false);
  const [isSignupForm, setIsSignupForm] = useState(false);
  const [user, setUser] = useState(USERTYPE.INFORMANT);
  const [valiadtionError, setValidationError] = useState({});
  const [loading, setLoading] = useState(false);
  const [verified, setVerified] = useState(null);
  const navigate = useNavigate();

  const userLogin = async (userData) => {
    setValidationError({});
    const validationResult = ValidateForm(user, userData);

    if (Object.keys(validationResult).length === 0) {
      context.fetchLatLong();
      userData.userLocationCoordinates =
        context.liveLocationLatLong?.fecthedUserLocation;

      const loginUser = await chekcUserLogin(userData);

      if (loginUser.status) {
        localStorage.setItem(
          USERTYPE.CURRENTUSER,
          JSON.stringify(loginUser.data)
        );

        context.login(loginUser.data);
        context.fetchUser();
        setCurrentUser(loginUser.data);

        Swal.fire({
          icon: STATUSMESSAGES.SUCCESS,
          title: "Welcome " + loginUser.data.userName + "!",
          toast: true,
          position: "top-end",
          text: LOGINLOGOUT.loginSuccess,
          showConfirmButton: false,
          timer: 2500,
        });

        if (user === USERTYPE.INVESTIGATOR) {
          navigate("/InvestigatorReports");
        } else {
          context.fetchUser();
          navigate("/Reports");
        }

        setViewForm(false);
      } else {
        toast.error(loginUser?.message);
      }
    } else {
      setValidationError(validationResult);
    }
  };

  const userSignup = async (userData) => {
    setValidationError({});
    const validationResult = ValidateForm(user, userData);
    setLoading(true);
    const emailVerificationResponse = await checkVerifiedEmail(
      userData.userEmail
    );
    setLoading(false);

    if (
      emailVerificationResponse.status &&
      emailVerificationResponse.data.score < 70
    ) {
      setVerified(false);
      validationResult.email =
        "Email verification failed. Please use a valid email address.";
    } else {
      setVerified(true);
    }

    if (Object.keys(validationResult).length === 0) {
      const userSignupStatus = await addUser(userData);
      if (userSignupStatus.status) {
        setIsSignupForm(false);
        if (userData.userType === USERTYPE.INFORMANT) {
          await saveNotification(
            "New User Registration",
            userData.userName + " registered to the Application.",
            userSignupStatus.data.userId,
            USERTYPE.INVESTIGATOR
          );
        }
        Swal.fire({
          icon: STATUSMESSAGES.SUCCESS,
          title: "Success!",
          text: LOGINLOGOUT.registerSuccess,
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
        });
      } else {
        toast.error(userSignupStatus.message);
      }
    } else {
      setValidationError(validationResult);
    }
  };

  const onLoginRegisterFormSubmit = (e) => {
    e.preventDefault();
    let UserName, Email, Password, PhoneNumber, InvestigationCentreCode;
    //userData holds the login/register user json datas
    let userData = {};
    if (!isSignupForm) {
      if (user === USERTYPE.INFORMANT) {
        Email = e.target[0].value;
        Password = e.target[1].value;
        userData = { userEmail: Email, userPassword: Password };
      } else {
        Email = e.target[0].value;
        InvestigationCentreCode = e.target[1].value;
        Password = e.target[2].value;
        userData = {
          userEmail: Email,
          userPassword: Password,
          investigationCentreCode: InvestigationCentreCode,
        };
      }
      userLogin(userData);
    } else {
      if (user === USERTYPE.INFORMANT) {
        UserName = e.target[0].value;
        Email = e.target[1].value;
        PhoneNumber = e.target[2].value;
        Password = e.target[3].value;
        userData = {
          userName: UserName,
          userEmail: Email,
          userPhoneNumber: PhoneNumber,
          userPassword: Password,
          userType: user,
        };
      } else {
        UserName = e.target[0].value;
        Email = e.target[1].value;
        PhoneNumber = e.target[2].value;
        InvestigationCentreCode = e.target[3].value;
        Password = e.target[4].value;
        userData = {
          userName: UserName,
          userEmail: Email,
          userPassword: Password,
          userPhoneNumber: PhoneNumber,
          investigationCentreCode: InvestigationCentreCode,
          userType: user,
        };
      }
      userSignup(userData);
    }
  };

  return (
    <div
      className={`modal fade shadow-lg ${style.modalBackgroundFade} ${
        viewForm ? "show" : ""
      }`}
      style={{ display: viewForm ? "block" : "none" }}
      id="register-login-modal"
      tabIndex="-1"
      aria-labelledby="register-login-modal-label"
      aria-hidden="true"
    >
      <div className={`modal-dialog modal-lg `} role="document">
        <div className={`modal-content`}>
          <div className="modal-header">
            <h5 className="modal-title" id="register-login-modal-label">
              Login to Your Account
            </h5>
            <button
              type="button"
              className="close"
              onClick={() => setViewForm(false)}
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body">
            <div className="register-login-modal-container row justify-content-center align-items-center">
              <div className="register-login-modal-image-container col-md-6 p-4 d-flex justify-content-center align-items-center flex-column">
                <div className="register-login-modal-image">
                  <img
                    src="/crimeReportingSystemLoginFormLogo.png"
                    className="img-fluid shadow-lg "
                    alt="crimeRepo"
                  />
                </div>
                <p className="register-login-modal-title text-center fs-6 mt-3">
                  Report Protect Prevent
                </p>
              </div>
              <div className="register-login-modal-form-container col-md-6 p-4">
                <form
                  className="register-login-modal-form"
                  onSubmit={(e) => onLoginRegisterFormSubmit(e)}
                >
                  {isSignupForm && (
                    <div className="form-group">
                      <label
                        htmlFor="username"
                        className="register-login-modal-label"
                      >
                        Username
                      </label>
                      <input
                        type="text"
                        className="form-control bg-light register-login-modal-input"
                        id="username"
                        placeholder="Enter Username"
                        required
                      />
                      {valiadtionError?.username && (
                        <span className="text-danger">
                          {valiadtionError.username}
                        </span>
                      )}
                    </div>
                  )}
                  <div className="form-group">
                    <label
                      htmlFor="email"
                      className="register-login-modal-label"
                    >
                      Email
                      {loading && <span className="spinner"></span>}
                      {verified === true && (
                        <span style={{ color: "green" }}>&#10003;</span>
                      )}
                      {verified === false && (
                        <span style={{ color: "red" }}>&#10007;</span>
                      )}
                    </label>
                    <input
                      type="email"
                      className="form-control bg-light register-login-modal-input"
                      id="email"
                      placeholder="Enter your Email"
                    />
                    {valiadtionError?.email && (
                      <span className="text-danger">
                        {valiadtionError.email}
                      </span>
                    )}
                  </div>
                  {isSignupForm && (
                    <div className="form-group">
                      <label
                        htmlFor="phoneNumber"
                        className="register-login-modal-label"
                      >
                        Phone Number
                      </label>
                      <input
                        type="text"
                        className="form-control bg-light register-login-modal-input"
                        id="phoneNumber"
                        placeholder="Enter phone number"
                        required
                      />
                      {valiadtionError?.phoneNumber && (
                        <span className="text-danger">
                          {valiadtionError.phoneNumber}
                        </span>
                      )}
                    </div>
                  )}
                  {investigator && (
                    <div className="form-group">
                      <label
                        htmlFor="investigationCentreCode"
                        className="register-login-modal-label"
                      >
                        Investigation Centre Code
                      </label>
                      <input
                        type="password"
                        className="form-control bg-light register-login-modal-input"
                        id="investigationCentreCode"
                        placeholder="Enter the code"
                        required
                      />
                      {valiadtionError?.investigationCentreCode && (
                        <span className="text-danger">
                          {valiadtionError.investigationCentreCode}
                        </span>
                      )}
                    </div>
                  )}
                  <div className="form-group">
                    <label
                      htmlFor="password"
                      className="register-login-modal-label"
                    >
                      Password
                    </label>
                    <input
                      type="password"
                      className="form-control register-login-modal-input"
                      id="password"
                      placeholder="Password"
                      required
                    />
                    {valiadtionError?.password && (
                      <span className="text-danger">
                        {valiadtionError.password}
                      </span>
                    )}
                  </div>

                  {isSignupForm ? (
                    <button
                      type="submit"
                      className={`btn btn-success w-100 mt-3 ${style.cursorPointer}`}
                      
                    >
                      Sign Up
                    </button>
                  ) : (
                    <button
                      type="submit"
                      className={`btn btn-success w-100  mt-3 ${style.cursorPointer} `}
                    >
                      Sign In
                    </button>
                  )}

                  {isSignupForm ? (
                    <div className={` d-flex justify-content-center align-items-center mt-3  `} >
                      <span>Already have an account? </span>
                      <u>
                        <span onClick={() => setIsSignupForm(false)}  className={`${style.cursorPointer}`}>
                          Sign In
                        </span>
                      </u>
                    </div>
                  ) : (
                    <div className=" d-flex justify-content-center align-items-center mt-3">
                      <span >Don't have an account?</span>
                      <u>
                        <span onClick={() => setIsSignupForm(true)} className={`${style.cursorPointer}`}>
                          Sign Up
                        </span>
                      </u>
                    </div>
                  )}

                  {investigator ? (
                    <div className=" d-flex justify-content-center align-items-center mt-3">
                      <span> Informant </span>
                      <u>
                        <span
                          onClick={() => {
                            setInvestigator(false);
                            setUser("Informant");
                          }}
                        >
                          {" "}
                          {isSignupForm ? "Signup" : "Login"}
                        </span>
                      </u>
                    </div>
                  ) : (
                    <div className=" d-flex justify-content-center align-items-center mt-3">
                      <span> Investigation Team </span>
                      <u>
                        <span
                          onClick={() => {
                            setInvestigator(true);
                            setUser("Investigator");
                          }}
                          className={`${style.cursorPointer}`}
                        >
                          {" "}
                          {isSignupForm ? "Signup" : "Login"}
                        </span>
                      </u>
                    </div>
                  )}
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RegisterLogin;
