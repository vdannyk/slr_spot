import React from 'react';
import './importDetails.css';

const ImportDetails = (props) => {


  return (
    <div className='slrspot__importDetails'>
      <div className='slrspot__importDetails-container'>
        <div className='slrspot__importDetails-container-fields'>
          <label className='slrspot__importDetails-label'>IMPORT DATE</label>
          <p>{ props.importDetails.studyImport.date }</p>
          <label className='slrspot__importDetails-label'>PERFORMED BY</label>
          <p>{ props.importDetails.studyImport.performedBy }</p>
          <label className='slrspot__importDetails-label'>SEARCH VALUE</label>
          <p>{ props.importDetails.studyImport.searchValue ? props.importDetails.studyImport.searchValue : '-' }</p>
          <label className='slrspot__importDetails-label'>ADDITIONAL INFORMATIONS</label>
          <p>{ props.importDetails.studyImport.additionalInformation ? props.importDetails.studyImport.additionalInformation : '-' }</p>
          <label className='slrspot__importDetails-label'>SOURCE</label>
          <p>{ props.importDetails.studyImport.source }</p>
          <label className='slrspot__importDetails-label'>STUDIES ADDED</label>
          <p>{ props.importDetails.studiesAdded }</p>
          <label className='slrspot__importDetails-label'>DUPLICATES REMOVED</label>
          <p>{ props.importDetails.studyImport.numOfRemovedDuplicates }</p>
        </div>
        <div className='slrspot__importDetails-container-buttons'>
          <button 
            className='slrspot__importDetails-exitButton' 
            onClick={() => props.triggerCancel(false)}>back</button>
        </div>
      </div>
    </div>
  )
}

export default ImportDetails