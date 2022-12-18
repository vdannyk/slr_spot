import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, StudyDiscussion, StudyHistory } from '../../../../components';
import { AWAITING } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import '../screening.css';


const Awaiting = (props) => {
  const [studies, setStudies] = useState([]);
  const [showHistory, setShowHistory] = useState(false);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);

  useEffect(() => {
    var userId = currentUser.id;
    axiosInstance.get("/studies/awaiting", { params: {
      reviewId, userId
    }})
    .then((response) => {
      setStudies(response.data)
    })
    .catch(() => {
    });
  }, []);

  return (
    <div className='slrspot__screening-studies'>
      { studies.map(study => (
        <ScreeningStudy 
          study={study} 
          isShowAbstracts={props.showAbstracts} 
          triggerHistory={setShowHistory} 
          triggerDiscussion={setShowDiscussion} 
          tab={AWAITING} 
          isFullText={props.isFullText} />
      ))}
      { showHistory && <StudyHistory triggerCancel={setShowHistory} /> }
      { showDiscussion && <StudyDiscussion triggerCancel={setShowDiscussion} /> }

    </div>
  )
}

export default Awaiting