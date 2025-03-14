import React, { useContext, useEffect, useState } from 'react'
import ReportCard from '../component/ReportCard'
import AuthContext from '../store/AuthContext';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import style from "../styles/CustomStyles.module.css";

const InvestigatorReport = () => {
const context=useContext(AuthContext);
const navigate = useNavigate();
const [reportFilter,SetReportFilter]=useState('newReport');


useEffect(() => {
 
  if (context.user==null||context?.user?.userType === "Informant") {
    navigate("/pageNotFound");
  } else {
     context.fetchReports();
  }
}, []);


  return (
    <div className={`reports w-100 d-flex  align-items-center flex-column p-2 ${style.mainDivBackgorundColor}`}>
    <div className='filter-btns w-100 d-flex justify-content-center'>
      <button className='btn btn-secondary m-2' onClick={()=>SetReportFilter('newReport')}>New Reports</button>
      <button className='btn btn-secondary m-2' onClick={()=>SetReportFilter('underInvestigationReport')}>Investigation Reports</button>
      <button className='btn btn-secondary m-2' onClick={()=>SetReportFilter('resolvedReport')}>Resolved Reports</button>
      <button className='btn btn-secondary m-2' onClick={()=>SetReportFilter('removedReport')}>Removed Reports</button>
    </div>
    {context.allReports?(
    <div className='reports-container container p-3 m-3 border border-dark'>
         <ReportCard user={context.user} reports={context.allReports} type={"investigatorReport"} fetchReports={context.fetchReports} Swal={Swal} InvestigationReportFilter={reportFilter}/>
    </div> 
    ):(
    <div className='reports-container container p-3 m-3 border border-dark'>
        <p>No Reports Available</p>
    </div> 
    )

    }

    </div>
  )
}

export default InvestigatorReport