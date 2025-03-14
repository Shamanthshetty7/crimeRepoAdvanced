import React, { useContext, useEffect, useState } from 'react';
import { getAllNews, deleteNews } from '../service/NewsService';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../store/AuthContext';
import NewsForm from '../component/NewsForm';
import style from "../styles/CustomStyles.module.css";


const InvestigatorNews = () => {
  const navigate = useNavigate();
  const context = useContext(AuthContext);
  const [newsList, setNewsList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [viewNewsForm, setViewNewsForm] = useState(false);
  const [selectedNews, setSelectedNews] = useState(null);

  const fetchNews = async () => {
    const response = await getAllNews();
    setIsLoading(false);
    if (response.status) {
      setNewsList(response.data);
    }
  };

 
  const DeleteBtnClick = async (newsId) => {
    const result = await Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!',
    });

    if (result.isConfirmed) {
      const response = await deleteNews(newsId);
     
      if (response.status) {
        Swal.fire('Deleted!', 'The news has been deleted.', 'success');
       
      } else {
        Swal.fire('Error!', 'There was an error deleting the news.', 'error');
      }
      
    }
  };

  useEffect(() => {
    if (context.user==null||context?.user?.userType === "Informant") {
      navigate("/pageNotFound");
    } else{
      fetchNews();
    }
  }, []);

  




  const AddNews = () => {
    setSelectedNews(null);
    setViewNewsForm(true);
  };

  const EditNews = (news) => {
    setSelectedNews(news);
    setViewNewsForm(true);
  };

  return (
    <div className="news w-100 d-flex justify-content-center align-items-center flex-column p-2">
      <div className='filter-btns w-75 d-flex justify-content-end '>
        <button className='btn btn-warning m-2 ' onClick={AddNews}>Add News</button>
      </div>
      {isLoading ? (
        <div className="d-flex justify-content-center w-100 align-items-center">
          <div className="spinner-border" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
        </div>
      ) : (
        newsList.length > 0 ? (
          <div className='news-container container p-3 m-3 border border-dark'>
            {newsList.map((news) => (
                
              <div key={news.newsId} className="news-item p-3 m-3 border shadow d-flex">
              <div className={`w-25 `} >
                <img src={news.newsImage} alt="" className={`${style.investigatorNewsImage} shadow-1-strong me-2`}/>

                </div>
                <div className='d-flex flex-column w-75'>
                <h5><strong>{news.newsHeadline}</strong></h5>
               
                <p>{news.newsSmallDescription}</p>
               
                <div className='action-btns d-flex justify-content-end'>
                <button className={`btn ${style.secondaryButton} m-2 `} onClick={() => EditNews(news)}>Edit</button>
                <button className={`btn ${style.dangerBtn} m-2`} onClick={() => DeleteBtnClick(news.newsId)}>Delete</button>
                </div>
              </div>
              </div>
            ))}
          </div>
        ) : (
          <div className='news-container container p-3 m-3 border border-dark'>
            <p>No News Available</p>
          </div>
        )
      )}
      <NewsForm
        viewNewsForm={viewNewsForm}
        setViewNewsForm={setViewNewsForm}
        toast={Swal}
        selectedNews={selectedNews}
        fetchNews={ fetchNews}
      />
    </div>
  );
};

export default InvestigatorNews;