import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { ContentPopup, StudyDiscussion, StudyHistory } from '../../../components';
import Check from 'react-bootstrap/FormCheck';


const ResultStudy = ({study, selected, handleSelect}) => {
  const [showAbstract, setShowAbstract] = useState(false);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const { reviewId } = useParams();

  const handleShowAbstract = () => {
    setShowAbstract(!showAbstract);
  }

  return (
    <div className='slrspot__screeningStudy'>
      <h3 style={ {display: 'flex'} }>
        <Check 
          type='checkbox'
          checked={ selected.includes(study) }
          onChange={ handleSelect }/>
        { study.title }
      </h3>
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
      <button>full text</button>
      
      {/* <StudyTags 
        studyId={ study.id } 
        reviewTags={ reviewTags } /> */}
        
      <div className='slrspot__screeningStudy-options'>
        <button onClick={ () => setShowDiscussion(true)}>discussion</button>
        <button onClick={ () => setShowHistory(true) }>history</button>
        <button>Restore to full text</button>
      </div>
      { showDiscussion && 
        <ContentPopup content={<StudyDiscussion studyId={ study.id } />} triggerExit={() => setShowDiscussion(false)}/> }
      { showHistory && 
        <ContentPopup content={<StudyHistory studyId={ study.id } />} triggerExit={() => setShowHistory(false)} /> }

    </div>
  )
}

export default ResultStudy