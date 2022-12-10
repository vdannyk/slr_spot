import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { ScreeningCriteria, ScreeningMenu, ScreeningOptions, ScreeningStudy, FolderTree } from '../../../components';
import Check from 'react-bootstrap/FormCheck';
import './screening.css';

const testStudy = {
  "title": "Titleaas asdasd as asd asdasd aasdas sad as dasddasd as",
  "abstract": "Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores  Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores  Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores  Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores Lorem ipsum dolores",
  "authors": "authors, authors, authors, authors",
  "journal": "journal journal journal journal",
  "publicationYear": "1232",
  "doi": "10.21212/s2123123123",
  "url": "https://dupa.com",
  "language": "language"
}

const Screening = (props) => {
  const [isStudiesView, setIsStudiesView] = useState(true);
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [folders, setFolders] = useState([]);

  const [isToBeReviewed, setIsToBeReviewed] = useState(true);
  const [isReviewed, setIsReviewed] = useState(false);
  const [isConflicts, setIsConflicts] = useState(false);
  const [isAwaiting, setIsAwaiting] = useState(false);
  const [isExcluded, setIsExcluded] = useState(false);


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

  return (
    <div className='slrspot__screening'>
      <div className='slrspot__review-studiesDisplay-header'>
        <h1>{ headerContent() }</h1>
        { isStudiesView 
          ? <span onClick={ () => setIsStudiesView(false) }>Switch to folders view</span> 
          : <span onClick={ () => setIsStudiesView(true) }>Switch to studies view</span> }
      </div>

      <ScreeningMenu 
        isToBeReviewed={isToBeReviewed}
        setIsToBeReviewed={setIsToBeReviewed}
        isReviewed={isReviewed}
        setIsReviewed={setIsReviewed}
        isConflicts={isConflicts}
        setIsConflicts={setIsConflicts}
        isAwaiting={isAwaiting}
        setIsAwaiting={setIsAwaiting}
        isExcluded={isExcluded}
        setIsExcluded={setIsExcluded}/>

      { isStudiesView && (
        <>
          <div className='slrspot__screening-options'>
          <div className='slrspot__screening-options-container'>
            <div className='slrspot__screening-options-container-checks'>
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>show highlighst</label>
              </div>
              {/* <a>switch to review mode</a> */}
              <div className='slrspot__screening-options-check'>
                <Check onChange={ () => setShowAbstracts(!showAbstracts) } defaultChecked={ showAbstracts }/>
                <label>show abstracts</label>
              </div>
            </div>
            <div className='slrspot__screening-options-search'>
              <label>Search</label>
              <input></input>
            </div>
          </div>
        </div>

        {/* <ScreeningCriteria /> */}

        <div className='slrspot__screening-studies'>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
          <ScreeningStudy study={testStudy} isShowAbstracts={showAbstracts}/>
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