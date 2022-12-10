import React, { useState, useEffect } from 'react';
import './screeningStudy.css';
import StudyTags from './studyTags/StudyTags';


const ScreeningStudy = ({study, isShowAbstracts}) => {
  const [showAbstract, setShowAbstract] = useState(isShowAbstracts);

  useEffect(() => {
    if (showAbstract !== isShowAbstracts) {
      setShowAbstract(!showAbstract)
    }
  }, [isShowAbstracts]);

  const handleShowAbstract = () => {
    setShowAbstract(!showAbstract);
  }

  return (
    <div className='slrspot__screeningStudy'>
      <h3>{ study.title }</h3>
      <button onClick={handleShowAbstract} className='slrspot__screeningStudy-showAbstract-button'>
        { showAbstract ? 'hide abstract' : 'show abstract'}
      </button>
      { showAbstract && <p><label>abstract:</label> { study.abstract }</p> }
      <p><label>authors:</label> { study.authors }</p>
      <p><label>journal:</label> { study.journal }</p>
      <p><label>publicationYear:</label> { study.publicationYear }</p>
      <p><label>doi:</label> { study.doi }</p>
      <p><label>URL:</label> { study.url }</p>
      <p><label>language:</label> { study.language }</p>
      <StudyTags />
      <div className='slrspot__screeningStudy-options'>
        <button>discussion</button>
        <button>history</button>
        <button>mark as duplicate</button>
      </div>
      <div className='slrspot__screeningStudy-decision'>
        <button className='slrspot__screeningStudy-decision-button'>exclude</button>
        <button className='slrspot__screeningStudy-decision-button'>unclear</button>
        <button className='slrspot__screeningStudy-decision-button'>include</button>
      </div>
    </div>
  )
}

export default ScreeningStudy