import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { ScreeningMenu, ScreeningOptions, FolderTree } from '../../../components';
import './screening.css';
import { AWAITING, CONFLICTED, TO_BE_REVIEWED } from '../../../constants/tabs';
import ToBeReviewed from './tabs/ToBeReviewed';
import Conflicted from './tabs/Conflicted';
import Awaiting from './tabs/Awaiting';
import Excluded from './tabs/Excluded';
import { useParams } from 'react-router-dom';


const Screening = (props) => {
  const [isStudiesView, setIsStudiesView] = useState(true);
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [folders, setFolders] = useState([]);
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

  function headerContent() {
    if (props.isFullText) {
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
          isFullText={ props.isFullText } 
          reviewTags={ reviewTags } />
      )
    } else if (tab === CONFLICTED) {
      return (
        <Conflicted showAbstracts={showAbstracts} isFullText={props.isFullText} />
      )
    } else if (tab === AWAITING) {
      return (
        <Awaiting showAbstracts={showAbstracts} isFullText={props.isFullText} />
      )
    } else {
      return (
        <Excluded showAbstracts={showAbstracts} isFullText={props.isFullText} />
      )
    }
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
    </div>
  )
}

export default Screening