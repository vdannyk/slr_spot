import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import PdfUploader from '../../../pdfUploader/PdfUploader';
import axiosInstance from '../../../../services/api';
import { AiFillCloseSquare } from "react-icons/ai";
import './fullTextField.css';
import { BeatLoader } from 'react-spinners';


const FullTextField = ({ study, isFullText, allowChanges, tab }) => {
  const navigate = useNavigate();
  const [fullTextFilename, setFulltextFilename] = useState();
  const [isLoaded, setIsLoaded] = useState(false);
  const [loadingError, setLoadingError] = useState();
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get('/studies/' + study.id + "/full-text/name")
    .then((response) => {
      setFulltextFilename(response.data);
    })
    .catch((error) => {
    });
  }, [isLoaded]);

  const handleRemove = () => {
    axiosInstance.delete('/studies/' + study.id + "/full-text")
      .then(() => {
        setIsLoaded(!isLoaded);
      })
      .catch((error) => {
      });
  }

  if (allowChanges) {
    return isFullText && (
      <div className='slrspot__fullTextField'>
        <label>full text:</label>
        { fullTextFilename
          ? 
          <>
            <button onClick={() => navigate('/reviews/' + reviewId + '/studies/' + study.id + '/full-text/' + tab)}>
              { fullTextFilename }
            </button> 
            <AiFillCloseSquare
              style={{ cursor: 'pointer' }}
              color='red'
              size={28}
              onClick={ handleRemove }/>
          </>
          : 
          <>
            <PdfUploader 
              studyId={study.id} 
              setIsLoaded={setIsLoaded}
              setLoadingError={setLoadingError} 
            /> 
            { loadingError && <p className='slrspot__input-error'>{loadingError}</p>}
          </>
        }
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