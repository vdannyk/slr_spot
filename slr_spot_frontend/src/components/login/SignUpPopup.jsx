import React, { useEffect } from 'react';
import './signUpPopup.css';

const SignUpPopup = (props) => {

  return (props.trigger) ? (
    <div id='unblurred' className='slrspot__signup'>
        <div id='unblurred' className="slrspot__signup-inner">
            <p>BUTTON TRIGGERED POPUP</p>
            <button className='close-btn' onClick={() => props.setTrigger(false)}>CLOSE</button>
            { props.children }
        </div>
    </div>
  ) : "";
}

export default SignUpPopup