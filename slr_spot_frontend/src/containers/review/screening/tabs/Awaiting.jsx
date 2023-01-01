import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, ScreeningOptions } from '../../../../components';
import { AWAITING } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { OWNER, MEMBER, COOWNER } from '../../../../constants/roles';
import { FULL_TEXT, TITLE_ABSTRACT } from '../../../../constants/studyStatuses';
import { EVERYTHING_SEARCH } from '../../../../constants/searchTypes';
import '../screening.css';


const Awaiting = (props) => {
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [showTeamHighlights, setShowTeamHighlights] = useState(false);
  const [showPersonalHighlights, setShowPersonalHighlights] = useState(false);
  const [showHighlights, setShowHighlights] = useState(showTeamHighlights || showPersonalHighlights);

  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const [refreshStudies, setRefreshStudies] = useState(false);
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  const [searchType, setSearchType] = useState(EVERYTHING_SEARCH);


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

  const handleSearch = (searchValue) => {
    var userId = currentUser.id;
    var status = props.isFullText ? FULL_TEXT : TITLE_ABSTRACT;
    if (searchValue.trim().length > 0) {
      axiosInstance.get("/studies/state/" + AWAITING + "/search", { params: {
        reviewId, userId, status, searchType, searchValue 
      }})
      .then((response) => {
        setStudies(response.data.content)
      })
      .catch(() => {
      });
    } else {
      getStudies();
    }
  }

  useEffect(() => {
    setShowHighlights(showTeamHighlights || showPersonalHighlights);
  }, [showTeamHighlights, showPersonalHighlights]);

  return (
    <div className='slrspot__screening-studies'>
      <ScreeningOptions 
        triggerShowAbstractsChange={setShowAbstracts}
        showAbstracts={showAbstracts} 
        triggerShowTeamHighlights={setShowTeamHighlights}
        showTeamHighlights={showTeamHighlights} 
        triggerShowPersonalHighlights={setShowPersonalHighlights}
        showPersonalHighlights={showPersonalHighlights} 
        handleSearch={ handleSearch }
        setSearchType={ setSearchType }/>

      { studies.map(study => (
        <ScreeningStudy 
          study={study} 
          isShowAbstracts={ showAbstracts } 
          triggerVote={ handleStudiesUpdate }
          triggerRefresh={ () => setRefreshStudies(!refreshStudies)}
          tab={AWAITING} 
          isFullText={props.isFullText}
          reviewTags={ props.reviewTags } 
          allowChanges={ allowChanges }
          showHighlights={ showHighlights } 
          highlights={ showTeamHighlights ? props.teamHighlights : props.personalHighlights } />
      ))}
    </div>
  )
}

export default Awaiting