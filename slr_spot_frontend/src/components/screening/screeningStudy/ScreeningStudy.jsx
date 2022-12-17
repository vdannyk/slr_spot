import React, { useState, useEffect } from 'react';
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../../constants/tabs';
import './screeningStudy.css';
import StudyTags from './studyTags/StudyTags';
import { useNavigate, useParams } from "react-router-dom";
import ContentPopup from '../../popups/contentPopup/ContentPopup';
import StudyHistory from './studyHistory/StudyHistory';
import StudyDiscussion from './studyDiscussion/StudyDiscussion';


const ScreeningStudy = ({study, isShowAbstracts, triggerHistory, triggerDiscussion, tab, isFullText, reviewTags}) => {
  const [showAbstract, setShowAbstract] = useState(isShowAbstracts);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (showAbstract !== isShowAbstracts) {
      setShowAbstract(!showAbstract)
    }
  }, [isShowAbstracts]);

  const handleShowAbstract = () => {
    setShowAbstract(!showAbstract);
  }

  function tabSpecificContent() {
    if (tab === TO_BE_REVIEWED) {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button className='slrspot__screeningStudy-decision-button'>exclude</button>
          <button className='slrspot__screeningStudy-decision-button'>unclear</button>
          <button className='slrspot__screeningStudy-decision-button'>include</button>
        </div>
      )
    } else if (tab === CONFLICTED) {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button className='slrspot__screeningStudy-decision-button'>exclude</button>
          <button className='slrspot__screeningStudy-decision-button'>include</button>
        </div>
      )
    } else if (tab === AWAITING) {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button className='slrspot__screeningStudy-decision-button'>change decision</button>
        </div>
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
        <ContentPopup content={<StudyDiscussion />} triggerExit={() => setShowDiscussion(false)}/> }
      { showHistory && 
        <ContentPopup content={<StudyHistory />} triggerExit={() => setShowHistory(false)} /> }

    </div>
  )
}

export default ScreeningStudy