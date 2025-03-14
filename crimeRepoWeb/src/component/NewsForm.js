import React, { useState, useEffect, useContext } from 'react';
import { saveNews, updateNews, saveNewsImage } from '../service/NewsService';
import style from '../styles/CustomStyles.module.css';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import { STATUSMESSAGES } from '../utils/Messages';
import {toast,ToastContainer} from 'react-toastify'
import AuthContext from '../store/AuthContext';
import ValidateForm from './ValidateForms';
const NewsForm = ({ viewNewsForm, setViewNewsForm, selectedNews, fetchNews }) => {
  const EmptyImage = "/images/no-image-available.webp";
  const addNewsImage = "/images/addNewsImage.png";
  const context=useContext(AuthContext)
  const [formValues, setFormValues] = useState({
    newsHeadline: '',
    newsSmallDescription: '',
    newsDetails: '',
    newsImageUrl: EmptyImage,
  });
  const [newsImage, setNewsImage] = useState(null);
  const [isEditMode, setIsEditMode] = useState(false);
 const [valiadtionError,setValidationError]=useState({});


  useEffect(() => {
    if (selectedNews) {
      setIsEditMode(true);
      setFormValues({
        ...selectedNews,
        newsImage: selectedNews.newsImage || EmptyImage,
      });
    } else {
      setIsEditMode(false);
      setFormValues({
        newsHeadline: '',
        newsSmallDescription: '',
        newsDetails: '',
        newsImageUrl: EmptyImage,
      });
    }
  }, [selectedNews]);

  const handleOnInputChange = (event) => {
    const { name, value } = event.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  const handleImageChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      setNewsImage(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setFormValues({
          ...formValues,
          newsImage: reader.result,
        });
      };
      reader.readAsDataURL(file);
    }
  };

  const onNewsFormSubmit = async (e) => {
    e.preventDefault();
    let response;
    const validationErrors = ValidateForm(null,formValues);

    if (Object.keys(validationErrors).length > 0) {
      setValidationError(validationErrors);
      return;
    }
    
    if (isEditMode) {
      response = await updateNews(formValues);
    } else {
       
      response = await saveNews(context.user?.userId,formValues);
    }

    if (response.status) {
      if (newsImage) {
        const imageResponse = await saveNewsImage(response.data.newsId, newsImage);
        if (!imageResponse.status) {
          toast.error(imageResponse.message);
        }
      }
      setViewNewsForm(false);
      setNewsImage(null)
      Swal.fire(
      STATUSMESSAGES.SUCCESS,
        isEditMode ? 'News updated successfully.' : 'News added successfully.',
    STATUSMESSAGES.SUCCESS
      );
      
    } else {
      toast.error(response.message);
    }
    fetchNews();
  };

  return (
    <div
      className={`modal fade ${style.modalBackgroundFade} ${
        viewNewsForm ? 'show' : ''
      }`}
      style={{ display: viewNewsForm ? 'block' : 'none' }}
      id="news-modal"
      tabIndex="-1"
      aria-labelledby="news-modal-label"
      aria-hidden="true"
    >
      <div className={`modal-dialog modal-lg`} role="document">
        <div className={`modal-content ${style.modalBackground}`}>
          <div className={`modal-header ${style.modalHeader}`}>
            <h5 className="modal-title" id="news-modal-label">
              {isEditMode ? 'Edit News' : 'Add News'}
            </h5>
            <button
              type="button"
              className="close"
              onClick={() => setViewNewsForm(false)}
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body">
            <div className="col-md-12 mt-5 border border-dark shadow text-start">
              <form onSubmit={(e) => onNewsFormSubmit(e)} className='d-flex '>
              
              <div className="form-group m-3 ">
                  
                  <img
                    src={formValues.newsImage?formValues.newsImage:addNewsImage}
                    alt="News"
                    className={`img-fluid mt-3 ${style.investigatorEditNewsImage}`}
                   
                  />
                  <label htmlFor="news-image"></label>
                  <input
                    type="file"
                    name="newsImage"
                    className="form-control m-3"
                    id="news-image"
                    onChange={handleImageChange}
                    required
                  />
                </div>
                <div className='d-flex flex-column w-75 m-3'>
                <div className="form-group">
                  <label htmlFor="news-headline" className='m-2 fw-bold'>Headline</label>
                  <input
                    type="text"
                    name="newsHeadline"
                    className="form-control"
                    id="news-headline"
                    value={formValues.newsHeadline}
                    onChange={handleOnInputChange}
                    placeholder="Enter news headline"
                    required
                  />
            {valiadtionError?.newsHeadline && <span className="text-danger">{valiadtionError.newsHeadline}</span>}

                </div>
                <div className="form-group">
                  <label htmlFor="news-small-description" className='m-2 fw-bold'>Small Description</label>
                  <input
                    type="text"
                    name="newsSmallDescription"
                    className="form-control"
                    id="news-small-description"
                    value={formValues.newsSmallDescription}
                    onChange={handleOnInputChange}
                    placeholder="Enter small description"
                    required
                  />
               {valiadtionError?.newsSmallDescription && <span className="text-danger">{valiadtionError.newsSmallDescription}</span>}
               
                </div>
                <div className="form-group">
                  <label htmlFor="news-details" className='m-2 fw-bold'>Details</label>
                  <textarea
                    name="newsDetails"
                    className="form-control"
                    id="news-details"
                   
                    value={formValues.newsDetails}
                    onChange={handleOnInputChange}
                    placeholder="Enter news details"
                    rows="8"
                    required
                  ></textarea>
                 {valiadtionError?.newsDetails && <span className="text-danger">{valiadtionError.newsDetails}</span>}
                
                </div>
                
                <div className="d-flex justify-content-end gap-1 align-items-center">
                  <button
                    type="button"
                    className="btn btn-secondary m-2"
                    data-dismiss="modal"
                    onClick={() => setViewNewsForm(false)}
                  >
                    Close
                  </button>
                  <button type="submit" className="btn btn-primary m-2">
                    {isEditMode ? 'Update' : 'Submit'}
                  </button>
                </div>
                </div>
              </form>
            </div>
            <ToastContainer/>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NewsForm;