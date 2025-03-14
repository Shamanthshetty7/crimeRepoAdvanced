import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { getAllNews } from '../service/NewsService';
import style from "../styles/CustomStyles.module.css";

const NewsAdvertisement = () => {
  const [advertisements, setAdvertisements] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    const fetchNews = async () => {
      const response = await getAllNews();
      if (response.status) {
        setAdvertisements(response.data.slice(0, 10)); // Limit to 10 ads
      }
    };
    fetchNews();
  }, []);

  useEffect(() => {
    const intervalId = setInterval(() => {
   
      setCurrentIndex((prevIndex) => {
        if (prevIndex + 2 < advertisements.length) {
          return prevIndex + 2;
        } else {
          return 0; 
        }
      });
    }, 3000);

    return () => clearInterval(intervalId); 
  }, [advertisements]);

  const currentAds = advertisements.slice(currentIndex, currentIndex + 2);

  return (
    <div className={`container mt-4`}>
      <div className="d-flex flex-column">
        {currentAds.length === 0 ? (
          <div>
            <p>No Advertisement Available</p>
          </div>
        ) : (
          currentAds.map((ad) => (
            <div key={ad.newsId} className={`mb-4 ${style.newsContainerHeight}`}>
              <div className="card h-100">
                <img src={ad.newsImage} className={`card-img-top ${style.fixedAdvImageSize}`} alt={ad.newsHeadline} />
                <div className="card-body">
                  <p className="card-title">{ad.newsHeadline}</p>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default NewsAdvertisement;
