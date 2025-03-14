import React, { useContext, useEffect, useState } from 'react'
import { getAllKycApplications } from '../service/KycApplicationService';
import Swal from 'sweetalert2'
import KycCard from '../component/KycCard';
import { KYCTYPES } from '../utils/Messages';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../store/AuthContext';
import style from "../styles/CustomStyles.module.css";

const InvestigatorKyc = () => {
   const navigate = useNavigate();
   const context=useContext(AuthContext)
    const [kycApplicationFilter,setKycApplicationFilter]=useState(KYCTYPES.UNDERVERIFICATION);
    const [allKycApplication,setAllKycApplication]=useState({});
    const[isLoading,setIsLoading]=useState(true);

    const fecthKycApplications=async()=>{
        const response=await getAllKycApplications();
        setIsLoading(false);
        if(response.status){
            setAllKycApplication(response.data);
        }
    }

    useEffect(()=>{
    
      if(context.user==null||context?.user?.userType==="Informant"){
        navigate("/pageNotFound");
      }else{
        fecthKycApplications();
      }
       
    },[])

  return (
   
    <div className={`investigatorKyc w-100 d-flex align-items-center flex-column p-2 ${style.mainDivBackgorundColor}`}>
    <div className='filter-btns w-100 d-flex justify-content-center'>
      <button className='btn btn-secondary m-2' onClick={()=>setKycApplicationFilter('underVerification')}>New Kyc Applications</button>
      <button className='btn btn-secondary m-2' onClick={()=>setKycApplicationFilter('verified')}>Verified Kyc Applications</button>
      <button className='btn btn-secondary m-2' onClick={()=>setKycApplicationFilter('rejected')}>Cancelled Kyc Applications</button>
    </div>
    {isLoading ? (
        <div className="d-flex justify-content-center w-100 align-items-center">
          <div className="spinner-border " role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>)
        :(
    allKycApplication.length>0?(
    <div className='kyc-application-container container p-3 m-3 border border-dark'>
         <KycCard kycApplicationFilter={kycApplicationFilter} allKycApplication={allKycApplication} Swal={Swal}/>
    </div> 
    ):(
    <div className='kyc-application-container container p-3 m-3 border border-dark'>
        <p>No Applications Available</p>
    </div> 
    )

    )}

    </div>
  )
}
export default InvestigatorKyc