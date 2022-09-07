import React, { useState } from 'react';
import './accessPopup.css';
import { SignIn, SignUp } from '../../components'

const AccessPopup = (props) => {

  return (props.trigger) ? (
    <div className='slrspot__accessPopup'>
        <div className="slrspot__accessPopup-box scale-up-center">
            <div className="slrspot__accessPopup-closeBtn_container">
              <button type="button" className='slrspot__accessPopup-closeBtn' onClick={() => props.setTrigger(false)}>
                CLOSE
              </button>
            </div>
            <div>
              { props.popup }
            </div>
        </div>
    </div>
  ) : "";
}

export default AccessPopup