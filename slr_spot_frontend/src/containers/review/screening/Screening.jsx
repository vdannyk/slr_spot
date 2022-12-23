import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { ScreeningMenu, ScreeningOptions, FolderTree, ContentPopup, CriteriaButton } from '../../../components';
import './screening.css';
import { AWAITING, CONFLICTED, TO_BE_REVIEWED } from '../../../constants/tabs';
import ToBeReviewed from './tabs/ToBeReviewed';
import Conflicted from './tabs/Conflicted';
import Awaiting from './tabs/Awaiting';
import Excluded from './tabs/Excluded';
import { useParams } from 'react-router-dom';
import EventBus from '../../../common/EventBus';
import { INCLUSION_TYPE, EXCLUSION_TYPE } from '../../../components/screening/criteria/CriteriaTypes';


const Screening = (props) => {
  const [isStudiesView, setIsStudiesView] = useState(true);
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [showCriteria, setShowCriteria] = useState(false);
  const [folders, setFolders] = useState([]);
  const [criteria, setCriteria] = useState([]);
  const [tab, setTab] = useState(TO_BE_REVIEWED)
  const [reviewTags, setReviewTags] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get("/folders")
    .then((response) => {
      setFolders(response.data)
    })
    .catch(() => {
    });
  }, []);

  useEffect(() => {
    axiosInstance.get("/tags", { params: {
      reviewId
    }})
    .then((response) => {
      setReviewTags(response.data)
    })
    .catch(() => {
    });
  }, []);

  useEffect(() => {
    axiosInstance.get("/criteria", { params: {
      reviewId
    }})
    .then((response) => {
      setCriteria(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  function headerContent() {
    if (props.state.isFullText) {
      return (
        <h1>Full text screening</h1>
      )
    } else {
      return (
        <h1>Title and abstract screening</h1>
      )
    }
  }

  function tabContent() {
    if (tab === TO_BE_REVIEWED) {
      return (
        <ToBeReviewed 
          showAbstracts={ showAbstracts } 
          isFullText={ props.state.isFullText } 
          reviewTags={ reviewTags } 
          userRole={props.userRole}/>
      )
    } else if (tab === CONFLICTED) {
      return (
        <Conflicted 
          showAbstracts={showAbstracts} 
          isFullText={props.state.isFullText} 
          allowChanges={props.allowChanges}
          userRole={props.userRole}/>
      )
    } else if (tab === AWAITING) {
      return (
        <Awaiting 
          showAbstracts={showAbstracts} 
          isFullText={props.state.isFullText} 
          allowChanges={props.allowChanges}
          userRole={props.userRole}/>
      )
    } else {
      return (
        <Excluded 
          showAbstracts={showAbstracts}
          isFullText={props.state.isFullText} 
          allowChanges={props.allowChanges}
          userRole={props.userRole}/>
      )
    }
  }

  const CriteriaPopupContent = () => {
    const inclusionCriteria = criteria.filter((criterion) => criterion.type === INCLUSION_TYPE)
      .map((criterion) => 
        <p key={criterion.name}>
          {criterion.name}
        </p>
    );

    const exclusionCriteria = criteria.filter((criterion) => criterion.type === EXCLUSION_TYPE)
      .map((criterion) => 
        <p key={criterion.name}>
          {criterion.name}
        </p>
    );

    return (
      <div style={{ wordBreak: 'break-all'}}>
        <h2>Inclusion Criteria</h2>
        {inclusionCriteria}
        <h2>Exclusion Criteria</h2>
        {exclusionCriteria}
      </div>
    )
  }

  return (
    <div className='slrspot__screening'>
      <div className='slrspot__review-studiesDisplay-header'>
        { headerContent() }
        { isStudiesView 
          ? <span onClick={ () => setIsStudiesView(false) }>Switch to folders view</span> 
          : <span onClick={ () => setIsStudiesView(true) }>Switch to studies view</span> }
      </div>

      <ScreeningMenu 
        tab={tab}
        setTab={setTab}/>

      { isStudiesView ? (
        <>
          <ScreeningOptions triggerChange={setShowAbstracts} showAbstracts={showAbstracts}/>

          { tabContent() }

        </>
      ) : (
        <div className='slrspot__screening-studies-folders'>
          <FolderTree folders={folders} isScreening={true}/>
        </div>
      )}

      <CriteriaButton triggerCriteria={() => setShowCriteria(true)}/>
      { showCriteria && (
        <ContentPopup triggerExit={() => setShowCriteria(false)} content={<CriteriaPopupContent />} />
      )}
    </div>
  )
}

export default Screening