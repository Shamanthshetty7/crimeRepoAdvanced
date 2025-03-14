import React, { Fragment, useState } from 'react';


export const AllNews = ({ newsList, setLatestNews}) => {
  const [currentPage, setCurrentPage] = useState(1);
  const newsPerPage = 5; 

  const totalPages = Math.ceil(newsList.length / newsPerPage);

  const indexOfLastNews = currentPage * newsPerPage;
  const indexOfFirstNews = indexOfLastNews - newsPerPage;
  const currentNews = newsList.slice(indexOfFirstNews, indexOfLastNews);

  const handleHeadlineClick = (news) => {
    setLatestNews(news);
  };

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  return (
    <Fragment>
      <div className="col-md news-list m-2 border-dark shadow">
        <div>
          <h3>LATEST NEWS</h3>
          {newsList.length === 0 && <h2>No Latest News available</h2>}
          <ul className="timeline mt-0">
            {currentNews.map((news) => (
              <li key={news.newsId} className="timeline-list">
                <div className="news-item  p-2  text-start" onClick={() => handleHeadlineClick(news)}>
                  <h5><b>{news.newsHeadline.toUpperCase()}</b></h5>
                  <p>{new Date(news.createdAt).toLocaleString()}</p>
                  <hr></hr>
                </div>
              </li>
            ))}
          </ul>
        </div>
        <div className="pagination m-3">
          {Array.from({ length: totalPages }, (_, index) => (
            <button
              key={index + 1}
              className={`btn ${currentPage === index + 1 ? 'btn-primary' : 'btn-secondary'} m-1`}
              onClick={() => handlePageChange(index + 1)}
            >
              {index + 1}
            </button>
          ))}
        </div>
      </div>
    </Fragment>
  );
};