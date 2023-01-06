import React, { useEffect, useState } from 'react'
import PdfReader from '../../components/pdfReader/PdfReader';
import OptionHeader from '../../components/screening/optionHeader/OptionHeader';
import axiosInstance from '../../services/api';
import { useNavigate, useParams } from 'react-router-dom';
import EventBus from '../../common/EventBus';
import './fullTextStudy.css';
import { ScreeningStudyFullText } from '../../components';
import { useSelector } from 'react-redux';
import Check from 'react-bootstrap/FormCheck';


const FullTextStudy = () => {
  const { reviewId, studyId, state } = useParams();
  const [pdfUrl, setPdfUrl] = useState(null);
  const [highlights, setHighlights] = useState([]);
  const [teamHighlights, setTeamHighlights] = useState([]);
  const [personalHighlights, setPersonalHighlights] = useState([]);

  const { user: currentUser } = useSelector((state) => state.auth);

  const navigate = useNavigate();

  const [showTeamHighlights, setShowTeamHighlights] = useState(false);
  const [showPersonalHighlights, setShowPersonalHighlights] = useState(false);
  const [showHighlights, setShowHighlights] = useState(false);

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

  useEffect(() => {
    axiosInstance.get("/keywords", { params: {
      reviewId
    }})
    .then((response) => {
      setTeamHighlights(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });

    var userId = currentUser.id;
    axiosInstance.get("/keywords/personal", { params: {
      reviewId, userId
    }})
      .then((response) => {
        setPersonalHighlights(response.data);
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          EventBus.dispatch('expirationLogout');
        }
    });
  }, []);

  const handleShowTeamHighlightsChange = () => {
    if (showTeamHighlights) {
      setShowTeamHighlights(false);
    } else {
      setShowTeamHighlights(true);
      setShowPersonalHighlights(false);
    }
  }

  const handleShowPersonalHighlightsChange = () => {
    if (showPersonalHighlights) {
      setShowPersonalHighlights(false);
    } else {
      setShowPersonalHighlights(true);
      setShowTeamHighlights(false);
    }
  }

  return (
    <div className='slrspot__fullTextStudy'>

      <OptionHeader content='Study full text' backward={ () => navigate(-1) }/>

      <div className='slrspot__fullTextStudy-container'>

        <div className='slrspot__screening-options'>
          <div className='slrspot__screening-options-container'>
            <div className='slrspot__screeningStudy-options-container-checks'>
              <div className='slrspot__screeningStudy-options-check'>
                <Check 
                  onChange={ handleShowTeamHighlightsChange } 
                  checked={ showTeamHighlights } />
                <label>team highlights</label>
              </div>
              <div className='slrspot__screeningStudy-options-check'>
                <Check
                  onChange={ handleShowPersonalHighlightsChange } 
                  checked={ showPersonalHighlights } />
                <label>personal highlights</label>
              </div>
            </div>
          </div>
        </div>

        <ScreeningStudyFullText 
          studyId={ studyId }
          reviewId={ reviewId }
          tab={ state }
          showHighlights={ showTeamHighlights || showPersonalHighlights }
          highlights={ showTeamHighlights ? teamHighlights : personalHighlights }
        />

        <div className='all-page-container'>
          <PdfReader pdfUrl={pdfUrl}/>
        </div>

      </div>

    </div>
  )
}

export default FullTextStudy