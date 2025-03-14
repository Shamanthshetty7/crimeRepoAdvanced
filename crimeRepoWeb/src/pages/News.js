import React, { useState, useEffect, useRef } from 'react';
import { getAllNews } from '../service/NewsService';
import '../styles/News.scss';
import style from "../styles/CustomStyles.module.css";
import { AllNews } from '../component/AllNews';

const News = () => {
  const [latestNews, setLatestNews] = useState(null);
  const [newsList, setNewsList] = useState([]);
  const [AllDetails,setAllDetails]=useState(false);
  const detailsRef = useRef(null);


  useEffect(() => {
    const fetchNews = async () => {
      const response = await getAllNews();
      if (response.status) {
        setNewsList(response.data);
        setLatestNews(response.data[0]);
      }
    };
    setAllDetails(false);
    fetchNews();
  }, []);

  useEffect(() => {
    if (AllDetails && detailsRef.current) {
      detailsRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [AllDetails]);

  useEffect(()=>{
    setAllDetails(false);
  },[latestNews])

  const onHeadLineClick=()=>{
    setAllDetails(true)
   

  }
 

  return (
    <div
      className={`d-flex justify-content-center align-items-center flex-column  ${style.mainDivBackgorundColor} news-container w-100 p-3`}
    >
    <h1>CRIMEREPO NEWS</h1>
      <div className="row w-100 m-3">
        <div className="col-md-8 latest-news border m-2 shadow p-2">
          {latestNews ? (
            <>
              <h2 onClick={()=>onHeadLineClick()}><strong>{latestNews.newsHeadline}</strong></h2>
              <p>{latestNews.newsSmallDescription}</p>
              <div className='d-flex justify-content-center'>
              <img src={latestNews.newsImage} alt="Latest News"  />
              </div>
             {AllDetails&& <div className="news-details m-3 p-2 text-start" ref={detailsRef}>
                <p>{new Date(latestNews.createdAt).toLocaleString()}</p>
                <p>{latestNews.newsDetails}</p>
                
              </div>}
            </>
          ):(
            <>
             <h2>No News Avaialble</h2>
            </>
          )}
        </div>
         <AllNews newsList={newsList} setLatestNews={setLatestNews} />
      </div>
    </div>
  );
};

export default News;