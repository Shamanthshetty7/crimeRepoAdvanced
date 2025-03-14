import React, { useContext, useEffect } from 'react'
import ReportCard from '../component/ReportCard'
import AuthContext from '../store/AuthContext';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import style from "../styles/CustomStyles.module.css";

const MyReports = () => {
const context=useContext(AuthContext)
const navigate = useNavigate()
const  myReports=context.allReports.filter((report)=>report.user?.userId===context.user?.userId)


useEffect(()=>{
  if(context?.user?.userType==="Investigator"){
    navigate("/pageNotFound");
  }

    context.fetchReports();
   
  },[])

  return (
    <div className={`reports w-100 d-flex justify-content-center align-items-center flex-column p-2 ${style.mainDivBackgorundColor}`}>
    {myReports.length!==0?(
    <div className='reports-container container p-3 m-3 border border-dark'>
         <ReportCard user={context.user} reports={myReports} type={"myReports"} fetchReports={context.fetchReports} Swal={Swal}/>
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

export default MyReports