import React from 'react';
import './confirmationPopup.css';

const ConfirmationPopup = (props) => {
  return (
    <div className='slrspot__confirmationPopup'>
      <div className='slrspot__confirmationPopup-container'>
        <div className='slrspot__confirmationPopup-container-content'>
          <h2>{props.title}</h2>
          {props.message}
        </div>
        <div className='slrspot__confirmationPopup-container-buttons'>
          <button className='slrspot__confirmationPopup-confirmButton' onClick={props.triggerConfirm}>
            confirm
          </button>
          <button 
            className='slrspot__confirmationPopup-cancelButton' 
            onClick={() => props.triggerCancel(false)}>cancel</button>
        </div>
      </div>
    </div>
  )
}

export default ConfirmationPopup