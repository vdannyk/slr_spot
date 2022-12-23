import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, StudyDiscussion, StudyHistory } from '../../../../components';
import { EXCLUDED } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { OWNER, MEMBER, COOWNER } from '../../../../constants/roles';
import '../screening.css';


const Excluded = (props) => {
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);


  function getStudies() {
    if (props.isFullText) {
      var status = 'FULL_TEXT';
      axiosInstance.get("/studies/excluded", { params: {
        reviewId, status
      }})
      .then((response) => {
        setStudies(response.data)
      })
      .catch(() => {
      });
    } else {
      var status = 'TITLE_ABSTRACT';
      axiosInstance.get("/studies/excluded", { params: {
        reviewId, status
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
          tab={EXCLUDED} 
          isFullText={props.isFullText} 
          allowChanges={ allowChanges } />
      ))}

    </div>
  )
}

export default Excluded