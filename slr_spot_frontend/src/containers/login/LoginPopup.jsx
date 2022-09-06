import React, { useEffect } from 'react';
import './loginPopup.css';
import { SignIn, SignUp } from '../../components'

const LoginPopup = (props) => {

  return (props.trigger) ? (
    <div className='slrspot__loginPopup'>
        <div className="slrspot__loginPopup-inner scale-up-center">
            <SignUp />
            <button className='slrspot__loginPopup-closeBtn' onClick={() => props.setTrigger(false)}>
              CLOSE
            </button>
        </div>
    </div>
  ) : "";
}

export default LoginPopup