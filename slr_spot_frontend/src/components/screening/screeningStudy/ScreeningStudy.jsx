import React, { useState, useEffect } from 'react';
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../../constants/tabs';
import './screeningStudy.css';
import StudyTags from './studyTags/StudyTags';


const ScreeningStudy = ({study, isShowAbstracts, triggerHistory, triggerDiscussion, tab, isFullText}) => {
  const [showAbstract, setShowAbstract] = useState(isShowAbstracts);

  useEffect(() => {
    console.log("dupa")
    console.log(isFullText);
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
      { isFullText && <p><label>full text:</label> { study.fullText }</p> }
      <StudyTags />
      <div className='slrspot__screeningStudy-options'>
        <button onClick={ () => triggerDiscussion(true)}>discussion</button>
        <button onClick={ () => triggerHistory(true) }>history</button>
        { !(tab === EXCLUDED) && <button>mark as duplicate</button>}
      </div>
      { tabSpecificContent() }
    </div>
  )
}

export default ScreeningStudy