import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import PdfUploader from '../../../pdfUploader/PdfUploader';
import EventBus from '../../../../common/EventBus';
import axiosInstance from '../../../../services/api';
import './fullTextField.css';


const FullTextField = ({ study, isFullText }) => {
  const navigate = useNavigate();
  const [fullTextFilename, setFulltextFilename] = useState();
  const [isLoaded, setIsLoaded] = useState(false);
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get('/studies/' + study.id + "/full-text/name")
    .then((response) => {
      setFulltextFilename(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, [isLoaded]);

  return isFullText && (
    <div className='slrspot__fullTextField'>
        <label>full text:</label>
        { fullTextFilename 
          ? <button onClick={() => navigate('/reviews/' + reviewId + '/studies/' + study.id + '/full-text')}>{ fullTextFilename }</button> 
          : <PdfUploader studyId={study.id} setIsLoaded={setIsLoaded}/> }
    </div>
  )
}

export default FullTextField