import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { ScreeningCriteria, ScreeningMenu, ScreeningOptions, ScreeningStudy, FolderTree } from '../../../components';
import { useParams } from "react-router-dom";
import './screening.css';
import { TO_BE_REVIEWED } from '../../../constants/tabs';


const Screening = (props) => {
  const [isStudiesView, setIsStudiesView] = useState(true);
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [folders, setFolders] = useState([]);
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  const { userId } = 1;
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

  useEffect(() => {
    axiosInstance.get("/studies/to-review", { params: {
      reviewId
    }})
    .then((response) => {
      console.log(response.data)
      setStudies(response.data)
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

  return (
    <div className='slrspot__screening'>
      <div className='slrspot__review-studiesDisplay-header'>
        <h1>{ headerContent() }</h1>
        { isStudiesView 
          ? <span onClick={ () => setIsStudiesView(false) }>Switch to folders view</span> 
          : <span onClick={ () => setIsStudiesView(true) }>Switch to studies view</span> }
      </div>

      <ScreeningMenu 
        tab={tab}
        setTab={setTab}/>

      { isStudiesView && (
        <>
          <ScreeningOptions triggerChange={setShowAbstracts} showAbstracts={showAbstracts}/>

          {/* <ScreeningCriteria /> */}

          <div className='slrspot__screening-studies'>
            { studies.map(study => (
              <ScreeningStudy study={study} isShowAbstracts={showAbstracts}/>
            ))}
          </div>
        </>
      )}

      { !isStudiesView && (
        <div className='slrspot__screening-studies-folders'>
          <FolderTree folders={folders} isScreening={true}/>
        </div>
      )}
    </div>
  )
}

export default Screening