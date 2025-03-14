import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const DashboardTopCitiesTable = ({ cityData }) => {

    const sortedCityData = cityData?.sort((a, b) => b.crimeRatepercntage - a.crimeRatepercntage).slice(0, 10);
  

  return (
    <div className="card mt-4">
     <div className="card-body">
      <h5 className="card-title">Top 10 Cities by Crime Rate</h5>
      <table className=" table table-striped">
        <thead>
          <tr>
            
            <th scope="col">City</th>
            <th scope="col">Total Reports</th>
            <th scope="col">Crime Rate (%)</th>
          </tr>
        </thead>
        <tbody>
          {sortedCityData?.map((city, index) => (
            <tr key={index}>
             
              <td>{city.name}</td>
              <td>{city.totalReports}</td>
              <td>
                <div className="progress" style={{ height: "20px" }}>
                  <div
                    className="progress-bar"
                    role="progressbar"
                    style={{ width: `${city.crimeRatepercntage}%` }}
                    aria-valuenow={city.crimeRatepercntage}
                    aria-valuemin="0"
                    aria-valuemax="100"
                  >
                    {city.crimeRatepercntage}%
                  </div>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
    </div>
  );
};

export default DashboardTopCitiesTable;