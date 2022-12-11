import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { ScreeningMenu, ScreeningOptions, FolderTree } from '../../../components';
import './screening.css';
import { TO_BE_REVIEWED } from '../../../constants/tabs';
import ToBeReviewed from './tabs/ToBeReviewed';


const Screening = (props) => {
  const [isStudiesView, setIsStudiesView] = useState(true);
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [folders, setFolders] = useState([]);
  const [tab, setTab] = useState(TO_BE_REVIEWED)


  useEffect(() => {
    axiosInstance.get("/folders")
    .then((response) => {
      console.log(response.data);
      setFolders(response.data)
    })
    .catch(() => {
    });
  }, []);

  function headerContent() {
    if (props.isFulltext) {
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
        <ToBeReviewed showAbstracts={showAbstracts} />
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