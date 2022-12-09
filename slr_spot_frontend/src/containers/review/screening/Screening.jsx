import React, { useState } from 'react';
import { ScreeningCriteria, ScreeningMenu, ScreeningOptions, ScreeningStudy } from '../../../components';
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
  const [showAbstracts, setShowAbstracts] = useState(true);

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
      <div className='slrspot__review-header'>
        { headerContent() }
      </div >

      <ScreeningMenu />

      <div className='slrspot__screening-options'>
        <div className='slrspot__screening-options-container'>
          <div className='slrspot__screening-options-container-checks'>
            <div className='slrspot__screening-options-check'>
              <Check />
              <label>Show highlighst</label>
            </div>
            {/* <a>switch to review mode</a> */}
            <div className='slrspot__screening-options-check'>
              <Check onChange={ () => setShowAbstracts(!showAbstracts) } defaultChecked={ showAbstracts }/>
              <label>show abstracts</label>
            </div>
          </div>
          <div>
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
    </div>
  )
}

export default Screening