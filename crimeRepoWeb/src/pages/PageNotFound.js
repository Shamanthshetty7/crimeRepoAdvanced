import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom'; // If using react-router for navigation
import '../styles/PageNotFound.scss'
import AuthContext from '../store/AuthContext';
import { USERTYPE } from '../utils/Messages';
const PageNotFound = () => {
  const context = useContext(AuthContext);
  const navigate = useNavigate();

  const onHomeBtnClick=()=>{
    if(context?.user?.userType===USERTYPE.INVESTIGATOR){
      navigate("/InvestigatorReports")
    }else{
      navigate("/")
    }
  }

  return (
    <div className="not-found-container">
    <div className="error-message">
      <div className="error-code">404</div>
      <div className="error-description">
        Oops! The page you’re looking for is not found. It might have been removed or you’ve mistyped the URL.
      </div>
      <button  className="back-button btn p-2 m-2" onClick={()=>onHomeBtnClick()}>Back to Home</button>
    </div>
  </div>
  );
};

export default PageNotFound;
