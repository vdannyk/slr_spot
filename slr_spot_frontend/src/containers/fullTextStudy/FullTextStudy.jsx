import React, { useEffect, useState } from 'react'
import PdfReader from '../../components/pdfReader/PdfReader';
import OptionHeader from '../../components/screening/optionHeader/OptionHeader';
import axiosInstance from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';
import EventBus from '../../common/EventBus';
import './fullTextStudy.css';
import { ScreeningStudyFullText } from '../../components';
import { TO_BE_REVIEWED } from '../../constants/tabs';

const FullTextStudy = () => {
  const { reviewId, studyId, state } = useParams();
  const [pdfUrl, setPdfUrl] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    axiosInstance.get('/studies/' + studyId + "/full-text", {
      responseType: 'blob'
    })
    .then((response) => {
      const url = URL.createObjectURL(new Blob([response.data]));
      setPdfUrl(url);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  return (
    <div className='slrspot__fullTextStudy'>

      <OptionHeader content='Study full text' backward={ () => navigate(-1) }/>

      <div className='slrspot__fullTextStudy-container'>

        <ScreeningStudyFullText 
          studyId={ studyId }
          reviewId={ reviewId }
          tab={ state }
        />

        <div className='all-page-container'>
          <PdfReader pdfUrl={pdfUrl}/>
        </div>

      </div>

    </div>
  )
}

export default FullTextStudy