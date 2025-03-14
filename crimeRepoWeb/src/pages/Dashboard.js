import React, { useContext, useEffect, useState } from "react";
import style from "../styles/CustomStyles.module.css";
import DashboardCard from "../component/DashboardCard";
import DashboardTopCitiesChart from "../component/DashboardTopCitiesChart";
import AuthContext from "../store/AuthContext";
import CrimeRateMap from "../component/CrimeRateMap";
import DashboardReportLineChart from "../component/DashboardReportLineChart";
import { useNavigate } from "react-router-dom";

const Dashboard = () => {
  const [isLoading, setIsLoading] = useState(true);
  const [cityData,setCityData]=useState([]);
  const context = useContext(AuthContext);
  const navigate = useNavigate();
 

const fetchAndSetCity=(crimeData)=>{
    let count=0;
    crimeData?.forEach(city => {
        count+=city.crimerate;
    });
   const citydata=crimeData?.map((city)=>{
    return {
        name:city.locationName,
        totalReports:city.crimerate,
        crimeRatepercntage:(city.crimerate/count)*100
     }

   });
   setCityData(citydata);
}

useEffect(()=>{
if(context.user?.userType==="Informant"){
    navigate("/");
  
}

fetchAndSetCity(context?.crimeData);
},[context?.crimeData])



  return (
    <div
      className={`d-flex justify-content-center align-items-center flex-column p-2 ${style.mainDivBackgorundColor}`}
    >
      <div
        className={`reports-container container p-3 m-3 border border-dark ${style.containerBackgroundColor}`}
      >
        <DashboardCard />
        {isLoading && (
          <div className="d-flex justify-content-center w-100 mt-3" >
            <div className="spinner-border" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
          </div>
        )}
        <div
           className={`${isLoading ? style.mapLoading : style.mapLoaded}`}
        >
          <DashboardTopCitiesChart cityData={cityData} />
        </div>
        <div className="lineChart border m-3 shadow">
        <DashboardReportLineChart />
        </div>
        <div className="crimeRateMap border m-3 shadow">
          <h4>Crime Rate Map</h4>
          {isLoading && (
            <div className="d-flex justify-content-center w-100">
              <div className="spinner-border" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
            </div>
          )}
          <div
            className={`${isLoading ? style.mapLoading : style.mapLoaded}`}
          >
            <CrimeRateMap setIsMapLoading={setIsLoading} context={context} />
          </div>
        </div>
        
      </div>
    </div>
  );
};

export default Dashboard;
