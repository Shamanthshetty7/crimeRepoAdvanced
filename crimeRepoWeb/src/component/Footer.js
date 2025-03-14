import React from "react";
import "../styles/Footer.scss";  // Assuming you will create a separate scss file for footer styles
import style from '../styles/CustomStyles.module.css';
import { useNavigate } from "react-router-dom";

const Footer = () => {
  const navigate = useNavigate();
  return (
    <footer className="footer row w-100 m-auto">
      

      <div className="footer-items col-10 row">
       

        <div className="sub-footer-items col-4 d-flex align-items-center justify-content-start">
          <p className="footer-text">© 2025 Crime Repo. All Rights Reserved.</p>
        </div>
      </div>
      <div className={`footer-logo col-2 d-flex align-items-center ${style.cursorPointer}`}   onClick={() => navigate("/")}>
        
        <h3 className="d-flex align-items-center">
          <span className="crime-text">crime</span>
          <span className="repo-text">Repo</span>
        </h3>
        <img
          src="/crimeReportingSystemLogo1.png"
          alt="crimeRepo-logo"
          className="rounded p-2 rounded-circle w-25 "
        />
      </div>
    </footer>
  );
};

export default Footer;
