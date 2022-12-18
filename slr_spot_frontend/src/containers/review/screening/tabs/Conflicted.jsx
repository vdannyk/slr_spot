import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, StudyDiscussion, StudyHistory } from '../../../../components';
import { CONFLICTED } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import '../screening.css';


const Conflicted = (props) => {
  const [studies, setStudies] = useState([]);
  const [showHistory, setShowHistory] = useState(false);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);

  function getStudies() {
    var userId = currentUser.id;
    if (props.isFullText) {
      var status = 'FULL_TEXT';
      axiosInstance.get("/studies/conflicted", { params: {
        reviewId, userId, status
      }})
      .then((response) => {
        setStudies(response.data)
      })
      .catch(() => {
      });
    } else {
      var status = 'TITLE_ABSTRACT';
      axiosInstance.get("/studies/conflicted", { params: {
        reviewId, userId, status
      }})
      .then((response) => {
        setStudies(response.data)
      })
      .catch(() => {
      });
    }
  }

  useEffect(() => {
    getStudies()
  }, [props.isFullText]);

  return (
    <div className='slrspot__screening-studies'>
      { studies.map(study => (
        <ScreeningStudy 
          study={study} 
          isShowAbstracts={props.showAbstracts} 
          triggerHistory={setShowHistory} 
          triggerDiscussion={setShowDiscussion} 
          tab={CONFLICTED} 
          isFullText={props.isFullText} />
      ))}
      { showHistory && <StudyHistory triggerCancel={setShowHistory} /> }
      { showDiscussion && <StudyDiscussion triggerCancel={setShowDiscussion} /> }

    </div>
  )
}

export default Conflicted