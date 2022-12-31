import React, { useState } from 'react';
import { AiFillFileText } from 'react-icons/ai';
import './studyFolderItem.css';


const StudyFolderItem = (props) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const shortenText = (text) => {
    if (isExpanded) {
      return <p>{text}</p>
    }
    if (text.length > 40) {
      return (
        <p>{text.substring(0, 40) + '...'}</p>
      )
    } else {
      return (
        <p>{text}</p>
      )
    }
  }

  return (
    <div className='slrspot__studyFolder' onClick={ () => props.handleShowStudy(props.study)}>
      <p>
        <AiFillFileText style={{ "marginRight": '5px'}} />
      </p>
      <b>{shortenText(props.study.title)}</b>
      {shortenText(props.study.authors)}
      <p>{props.study.publicationYear}</p>

      { props.isScreening 
        ? <p className='slrspot__studyFolder-details'>screen</p> 
        : <p onClick={ () => setIsExpanded(!isExpanded) } className='slrspot__studyFolder-details'>expand</p>}

    </div>
  )
}

export default StudyFolderItem