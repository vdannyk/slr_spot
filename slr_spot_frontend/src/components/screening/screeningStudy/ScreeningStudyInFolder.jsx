import React, { useState, useEffect } from 'react';
import StudyHistory from './studyHistory/StudyHistory';
import StudyDiscussion from './studyDiscussion/StudyDiscussion';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import axiosInstance from '../../../services/api';


const ScreeningStudyInFolder = (props) => {
  const [showAbstract, setShowAbstract] = useState(false);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);

  return (
    <div>
      {props.study.title}
    </div>
  )
}

export default ScreeningStudyInFolder