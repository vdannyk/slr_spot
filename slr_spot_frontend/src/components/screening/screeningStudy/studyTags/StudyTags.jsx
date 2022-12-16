import React from 'react';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import './studyTags.css';

const StudyTags = (props) => {
  return (
    <div className='slrspot__study-tags'>
      <div className='slrspot__study-tags-container'>
        <p className='slrspot__study-tags-tag'>tag1<AiFillMinusCircle className='slrspot__study-tags-removeIcon'/></p>
        <p className='slrspot__study-tags-tag'>tag2<AiFillMinusCircle className='slrspot__study-tags-removeIcon'/></p>
        <p className='slrspot__study-tags-tag'>
          add
          <AiFillPlusCircle className='slrspot__study-tags-addIcon'/>
        </p>
      </div>
    </div>
  )
}

export default StudyTags