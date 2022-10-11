import React, { useState } from 'react';
import { SignIn, SignUp } from '../../components';
import ForgotPassword from '../../components/forgotPassword/ForgotPassword';
import './accessPopup.css';

const AccessPopup = (props) => {
  const [isForgotPassword, setIsForgotPassword] = useState(false);

  const handleCloseClick = (props) => {
    props.setTrigger(false);
    props.setIsSignIn(false);
    props.setIsSignUp(false);
  };

  return (
    <div className='slrspot__accessPopup'>
        <div className="slrspot__accessPopup-box scale-up-center">
            <div className="slrspot__accessPopup-closeBtn_container">
              <p className='slrspot__accessPopup-closeBtn' onClick={() => handleCloseClick(props)}>
                &#x2715;
              </p>
            </div>
            { props.isSignIn && (
              <SignIn 
                signInTrigger={props.setIsSignIn}
                signUpTrigger={props.setIsSignUp}
                forgotPasswordTrigger={setIsForgotPassword}
              />
            )}
            { props.isSignUp && (
              <SignUp 
                signInTrigger={props.setIsSignIn}
                signUpTrigger={props.setIsSignUp}
              />
            )}
            { isForgotPassword && (
              <ForgotPassword 
                forgotPasswordTrigger={setIsForgotPassword}
                signInTrigger={props.setIsSignIn}
              />
            )}
        </div>
    </div>
  );
}

export default AccessPopup