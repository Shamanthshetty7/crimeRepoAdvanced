import React, { useState } from 'react'
import DetailedKycInformation from './DetailedKycInformation';
import style from "../styles/CustomStyles.module.css";

const KycCard = ({ kycApplicationFilter, allKycApplication, Swal }) => {
    const [viewKycDetailModal, setViewKycDetailModal] = useState(false);
    const [kycApplicationData, setKycApplicationData] = useState({});
    const [currentPage, setCurrentPage] = useState(1); // Track the current page
    const [itemsPerPage] = useState(5); // Set how many items per page

    const onVerifyHereBtnClick = (KycApplicationData) => {
        setKycApplicationData(KycApplicationData);
        setViewKycDetailModal(true);
    }

    const Pagination=()=>{
     return <div className="d-flex justify-content-center mt-3">
                <nav>
                    <ul className="pagination">
                        {currentPage > 1 && (
                            <li className="page-item">
                                <button className="page-link" onClick={() => handlePageChange(currentPage - 1)}>Previous</button>
                            </li>
                        )}
                        {[...Array(totalPages)].map((_, index) => (
                            <li key={index} className={`page-item ${currentPage === index + 1 ? 'active' : ''}`}>
                                <button className="page-link" onClick={() => handlePageChange(index + 1)}>{index + 1}</button>
                            </li>
                        ))}
                        {currentPage < totalPages && (
                            <li className="page-item">
                                <button className="page-link" onClick={() => handlePageChange(currentPage + 1)}>Next</button>
                            </li>
                        )}
                    </ul>
                </nav>
            </div>
    }
    const Card = ({ kycApplicationData }) => {
        return (
            <div className={`col-sm-6 col-md-6 mb-4`}> 
                <div className={`card float-right ${style.kycCard}`}>
                    <div className="row">
                        <div className="col-sm-5 d-flex justify-content-center">
                            <img className="d-block w-75  rounded-circle" src={kycApplicationData.userVerificationImage} alt="" />
                        </div>
                        <div className="col-sm-7 d-flex justify-content-center">
                            <div className="card-block">
                                <p>Submitted By: <strong>{kycApplicationData.userFullName}</strong></p>
                                <p>Submitted On: <strong>{kycApplicationData.createdAt}</strong></p>
                                <br />
                                <button className="btn btn-primary btn-sm float-right" onClick={() => onVerifyHereBtnClick(kycApplicationData)}>Verify Here</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }

   
    const filteredKycApplications = allKycApplication?.filter((kycApplication) => kycApplication.kycVerificationStatus === kycApplicationFilter);

   
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = filteredKycApplications?.slice(indexOfFirstItem, indexOfLastItem);
    const totalPages = Math.ceil(filteredKycApplications?.length / itemsPerPage);

    
    const handlePageChange = (pageNumber) => {
        setCurrentPage(pageNumber);
    }

    return (
        <>
            <div className="row justify-content-center ">
                {filteredKycApplications?.length === 0 ? (
                    <p>No {kycApplicationFilter} Applications Available</p>
                ) : (
                    currentItems?.map((kycApplication) => {
                        return <Card key={kycApplication.kycId} kycApplicationData={kycApplication} />
                    })
                )}
            </div>

           
            
            <Pagination/>
            
            <DetailedKycInformation 
                kycApplicationData={kycApplicationData} 
                viewKycDetailModal={viewKycDetailModal} 
                setViewKycDetailModal={setViewKycDetailModal} 
            />
        </>
    );
}

export default KycCard;
