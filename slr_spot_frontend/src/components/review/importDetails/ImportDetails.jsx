import React from 'react';
import './importDetails.css';

const ImportDetails = (props) => {


  return (
    <div className='slrspot__importDetails'>
      <div className='slrspot__importDetails-container'>
        <div className='slrspot__importDetails-container-fields'>
          <label className='slrspot__importDetails-label'>IMPORT DATE</label>
          <p>{ props.importDetails.date }</p>
          <label className='slrspot__importDetails-label'>PERFORMED BY</label>
          <p>{ props.importDetails.performedBy }</p>
          <label className='slrspot__importDetails-label'>SEARCH VALUE</label>
          <p>{ props.importDetails.searchValue ? props.importDetails.searchValue : '-' }</p>
          <label className='slrspot__importDetails-label'>ADDITIONAL INFORMATIONS</label>
          <p>{ props.importDetails.additionalInformation ? props.importDetails.additionalInformation : '-' }</p>
          <label className='slrspot__importDetails-label'>SOURCE</label>
          <p>{ props.importDetails.source }</p>
          <label className='slrspot__importDetails-label'>STUDIES ADDED</label>
          <p>{ props.importDetails.numOfImportedStudies }</p>
          <label className='slrspot__importDetails-label'>DUPLICATES REMOVED</label>
          <p>{ props.importDetails.numOfRemovedDuplicates }</p>
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