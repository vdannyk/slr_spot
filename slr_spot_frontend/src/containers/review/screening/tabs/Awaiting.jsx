import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, StudyDiscussion, StudyHistory } from '../../../../components';
import { AWAITING } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { OWNER, MEMBER, COOWNER } from '../../../../constants/roles';
import '../screening.css';


const Awaiting = (props) => {
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const [refreshStudies, setRefreshStudies] = useState(false);
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);


  function getStudies() {
    var userId = currentUser.id;
    if (props.isFullText) {
      var status = 'FULL_TEXT';
      axiosInstance.get("/studies/state/" + AWAITING, { params: {
        reviewId, userId, status
      }})
      .then((response) => {
        setStudies(response.data.content)
      })
      .catch(() => {
      });
    } else {
      var status = 'TITLE_ABSTRACT';
      axiosInstance.get("/studies/state/" + AWAITING, { params: {
        reviewId, userId, status
      }})
      .then((response) => {
        setStudies(response.data.content)
      })
      .catch(() => {
      });
    }
  }

  useEffect(() => {
    getStudies()
  }, [props.isFullText, refreshStudies]);

  const handleStudiesUpdate = (id) => {
    setStudies(studies.filter(study => study.id !== id));
  }

  return (
    <div className='slrspot__screening-studies'>
      { studies.map(study => (
        <ScreeningStudy 
          study={study} 
          isShowAbstracts={props.showAbstracts} 
          triggerVote={ handleStudiesUpdate }
          triggerRefresh={ () => setRefreshStudies(!refreshStudies)}
          tab={AWAITING} 
          isFullText={props.isFullText}
          reviewTags={ props.reviewTags } 
          allowChanges={ allowChanges }
          showHighlights={ props.showHighlights } 
          highlights={ props.highlights } />
      ))}
    </div>
  )
}

export default Awaiting