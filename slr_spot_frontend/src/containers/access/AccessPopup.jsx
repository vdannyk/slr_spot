import React from 'react';
import './accessPopup.css';

const AccessPopup = (props) => {

  return (props.trigger) ? (
    <div className='slrspot__accessPopup'>
        <div className="slrspot__accessPopup-box scale-up-center">
            <div className="slrspot__accessPopup-closeBtn_container">
              <p className='slrspot__accessPopup-closeBtn' onClick={() => props.setTrigger(false)}>
                &#x2715;
              </p>
            </div>
            { props.popup }
        </div>
    </div>
  ) : "";
}

export default AccessPopup