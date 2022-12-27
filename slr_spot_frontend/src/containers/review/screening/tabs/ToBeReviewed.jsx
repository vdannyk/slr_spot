import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, StudyDiscussion, StudyHistory,ContentPopup } from '../../../../components';
import { TO_BE_REVIEWED } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { OWNER, MEMBER, COOWNER } from '../../../../constants/roles';
import '../screening.css';

const ToBeReviewed = (props) => {
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  function getStudies() {
    var userId = currentUser.id;
    if (props.isFullText) {
      var status = 'FULL_TEXT';
      axiosInstance.get("/studies/to-review", { params: {
        reviewId, userId, status
      }})
      .then((response) => {
        setStudies(response.data)
      })
      .catch(() => {
      });
    } else {
      var status = 'TITLE_ABSTRACT';
      axiosInstance.get("/studies/to-review", { params: {
        reviewId, userId, status
      }})
      .then((response) => {
        setStudies(response.data)
      })
      .catch(() => {
      });
    }
  }

  const handleStudiesUpdate = (id) => {
    setStudies(studies.filter(study => study.id !== id));
  }

  useEffect(() => {
    getStudies()
  }, [props.isFullText]);

  return (
    <div className='slrspot__screening-studies'>
      { studies.map(study => (
        <ScreeningStudy 
          study={ study } 
          isShowAbstracts={ props.showAbstracts } 
          triggerVote={ handleStudiesUpdate } 
          tab={ TO_BE_REVIEWED }
          isFullText={ props.isFullText } 
          reviewTags={ props.reviewTags } 
          allowChanges={ allowChanges } 
          showHighlights={ props.showHighlights } 
          highlights={ props.highlights } />
      ))}

    </div>
  )
}

export default ToBeReviewed