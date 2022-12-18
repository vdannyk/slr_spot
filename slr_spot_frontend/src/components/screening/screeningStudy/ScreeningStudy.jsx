import React, { useState, useEffect } from 'react';
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../../constants/tabs';
import './screeningStudy.css';
import StudyTags from './studyTags/StudyTags';
import { useNavigate, useParams } from "react-router-dom";
import ContentPopup from '../../popups/contentPopup/ContentPopup';
import StudyHistory from './studyHistory/StudyHistory';
import StudyDiscussion from './studyDiscussion/StudyDiscussion';
import { useSelector } from 'react-redux';
import axiosInstance from '../../../services/api';



const ScreeningStudy = ({study, isShowAbstracts, triggerVote, triggerRefresh, tab, isFullText, reviewTags}) => {
  const [showAbstract, setShowAbstract] = useState(isShowAbstracts);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const navigate = useNavigate();
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);

  useEffect(() => {
    if (showAbstract !== isShowAbstracts) {
      setShowAbstract(!showAbstract)
    }
  }, [isShowAbstracts]);

  const handleVote = (vote) => {
    axiosInstance.post("/studies/" + study.id + "/screening_decisions", {
      reviewId: reviewId,
      userId: currentUser.id,
      decision: vote
    })
    .then(() => {
      if (tab === AWAITING ) {
        triggerRefresh();
      } else {
        triggerVote(study.id);
      }
    })
    .catch(() => {
    });
  }

  const handleShowAbstract = () => {
    setShowAbstract(!showAbstract);
  }

  const ConflictedOptions = () => {
    // to do -> check role in useState
    const [showOptions, setShowOptions] = useState(false);

    return (
      <div className='slrspot__screeningStudy-decision'>
        { showOptions 
        ? <>
            <button 
              className='slrspot__screeningStudy-decision-button'
              onClick={ () => handleVote('EXCLUDE')}>
                exclude
            </button>
            <button 
              className='slrspot__screeningStudy-decision-button' 
              onClick={ () => handleVote('INCLUDE')}>
                include
            </button>
          </>
        : <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={() => setShowOptions(true)}>
              change decision
          </button>
        }
      </div>
    )
  }

  const AwaitingOptions = () => {
    const [showOptions, setShowOptions] = useState(false);
    const [currentVote, setCurrentVote] = useState('');

    useEffect(() => {
      var userId = currentUser.id;
      axiosInstance.get("/studies/" + study.id + "/screening_decision", { params: {
        userId
      }})
      .then((response) => {
        setCurrentVote(response.data);
      })
    }, []);

    return (
      <div className='slrspot__screeningStudy-awaiting'>
        <div className='slrspot__screeningStudy-votes'>
          <p>current vote: <span><b>{currentVote}</b></span></p>
        </div>
        { showOptions 
        ? <div className='slrspot__screeningStudy-awaiting-decision'>
            <button 
              className='slrspot__screeningStudy-decision-button'
              onClick={ () => handleVote('EXCLUDE')}>
                exclude
            </button>
            <button 
              className='slrspot__screeningStudy-decision-button' 
              onClick={ () => handleVote('UNCLEAR')}>
                unclear
            </button>
            <button 
              className='slrspot__screeningStudy-decision-button' 
              onClick={ () => handleVote('INCLUDE')}>
                include
            </button>
          </div>
        : <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={() => setShowOptions(true)}>
              change decision
          </button>
        }
      </div>
    )
  }

  function tabSpecificContent() {
    if (tab === TO_BE_REVIEWED) {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button 
            className='slrspot__screeningStudy-decision-button'
            onClick={ () => handleVote('EXCLUDE')}>
              exclude
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => handleVote('UNCLEAR')}>
              unclear
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => handleVote('INCLUDE')}>
              include
          </button>
        </div>
      )
    } else if (tab === CONFLICTED) {
      return (
        <ConflictedOptions />
      )
    } else if (tab === AWAITING) {
      return (
        <AwaitingOptions />
      )
    } else {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button className='slrspot__screeningStudy-decision-button'>screen again</button>
        </div>
      )
    }
  }

  const FullTextField = () => {
    return isFullText && (
      <p><label>full text:</label> { study.fullText }
        <button className='pdf' onClick={() => navigate('/reviews/2/screening/study/full-text')}>test.pdf</button>
      </p>
    )
  }

  return (
    <div className='slrspot__screeningStudy'>
      <h3>{ study.title }</h3>
      <button onClick={handleShowAbstract} className='slrspot__screeningStudy-showAbstract-button'>
        { showAbstract ? 'hide abstract' : 'show abstract'}
      </button>
      { showAbstract && <p><label>abstract:</label> { study.documentAbstract }</p> }
      <p><label>authors:</label> { study.authors }</p>
      <p><label>journal:</label> { study.journalTitle }</p>
      <p><label>publicationYear:</label> { study.publicationYear }</p>
      <p><label>doi:</label> { study.doi }</p>
      <p><label>URL:</label> { study.url }</p>
      <p><label>language:</label> { study.language }</p>
      <FullTextField />
      
      <StudyTags 
        studyId={ study.id } 
        reviewTags={ reviewTags } />
        
      <div className='slrspot__screeningStudy-options'>
        <button onClick={ () => setShowDiscussion(true)}>discussion</button>
        <button onClick={ () => setShowHistory(true) }>history</button>
        { !(tab === EXCLUDED) && <button>mark as duplicate</button>}
      </div>
      { tabSpecificContent() }
      { showDiscussion && 
        <ContentPopup content={<StudyDiscussion studyId={ study.id } />} triggerExit={() => setShowDiscussion(false)}/> }
      { showHistory && 
        <ContentPopup content={<StudyHistory studyId={ study.id } />} triggerExit={() => setShowHistory(false)} /> }

    </div>
  )
}

export default ScreeningStudy