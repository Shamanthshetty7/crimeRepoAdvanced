import React, { useEffect, useState } from 'react'
import { getAllDashboardData } from '../service/DashboardService';

const DashboardCard = () => {
   const [allDashboardData,setAllDashboardData]=useState({});
   const totalUser=allDashboardData?.activeUsers+allDashboardData?.blockedUser
   const totalReports=allDashboardData?.newReport+allDashboardData?.resolvedReport+allDashboardData?.underInvestigationReport
   const totalKyc=allDashboardData?.verifiedKyc+allDashboardData?.underVerificationKyc+allDashboardData?.rejectedKyc
  const fetchDashboardData=async()=>{
    const response=await getAllDashboardData();
    setAllDashboardData(response?.data);
  }

  useEffect(()=>{
    fetchDashboardData();
  },[]);
  
  

  return (
    <div className="container mt-4">
    <div className="row">
    
      <div className="col-md-4">
        <div className="card">
          <div className="card-header">Total Users</div>
          <div className="card-body">
            <h5 className="card-title">{!isNaN(totalUser)?totalUser:0}</h5>
            <div className="card mt-2">
              <div className="card-body">
                <p className="card-text">Active Users: {allDashboardData?.activeUsers}</p>
                <p className="card-text">Blocked Users: {allDashboardData?.blockedUser}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
     
      <div className="col-md-4">
        <div className="card">
          <div className="card-header">Total Reports</div>
          <div className="card-body">
            <h5 className="card-title">{!isNaN(totalReports)?totalReports:0}</h5>
            <div className="card mt-2">
              <div className="card-body">
                <p className="card-text">New Reports: {allDashboardData?.newReport}</p>
                <p className="card-text">Resolved Reports: {allDashboardData?.resolvedReport}</p>
                <p className="card-text">Under Investigation: {allDashboardData?.underInvestigationReport}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
 
      <div className="col-md-4">
        <div className="card">
          <div className="card-header">Total KYC Applications</div>
          <div className="card-body">
            <h5 className="card-title">{!isNaN(totalKyc)?totalKyc:0}</h5>
            <div className="card mt-2">
              <div className="card-body">
                <p className="card-text">New Applications: {allDashboardData?.underVerificationKyc}</p>
                <p className="card-text">Verified Applications: {allDashboardData?.verifiedKyc}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  )
}

export default DashboardCard