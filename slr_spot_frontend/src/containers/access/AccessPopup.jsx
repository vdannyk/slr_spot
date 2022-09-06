import React, { useState } from 'react';
import './accessPopup.css';
import { SignIn, SignUp } from '../../components'

const AccessPopup = (props) => {

  return (props.trigger) ? (
    <div className='slrspot__accessPopup'>
        <div className="slrspot__accessPopup-inner scale-up-center">
            { props.popup }
            <button className='slrspot__accessPopup-closeBtn' onClick={() => props.setTrigger(false)}>
              CLOSE
            </button>
        </div>
    </div>
  ) : "";
}

export default AccessPopup