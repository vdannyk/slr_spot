import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import PdfUploader from '../../../pdfUploader/PdfUploader';
import EventBus from '../../../../common/EventBus';
import axiosInstance from '../../../../services/api';
import './fullTextField.css';


const FullTextField = ({ study, isFullText, allowChanges, tab }) => {
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

  if (allowChanges) {
    return isFullText && (
      <div className='slrspot__fullTextField'>
        <label>full text:</label>
        { fullTextFilename
          ? <button onClick={() => navigate('/reviews/' + reviewId + '/studies/' + study.id + '/full-text/' + tab)}>{ fullTextFilename }</button> 
          : <PdfUploader studyId={study.id} setIsLoaded={setIsLoaded}/> }
      </div>
    )
  } else {
    return isFullText && (
      <div className='slrspot__fullTextField'>
        <label>full text:</label>
        { fullTextFilename 
            ? <button onClick={() => navigate('/reviews/' + reviewId + '/studies/' + study.id + '/full-text/' + tab)}>{ fullTextFilename }</button> 
            : <p>not available</p> }
      </div> 
    )
  }

}

export default FullTextField