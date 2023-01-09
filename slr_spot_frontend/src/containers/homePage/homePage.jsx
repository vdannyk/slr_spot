import React, { useState } from 'react';
import AccessPopup from '../access/AccessPopup';
import './homePage.css'

const HomePage = (props) => {
  const [isAccessPopup, setIsAccessPopup] = useState(props.isAccessPopup);
  const [isSignInPopup, setIsSignInPopup] = useState(props.isSignInPopup);
  const [isSignUpPopup, setIsSignUpPopup] = useState(props.isSignUpPopup);

  return (
    <div className="slrspot__homePage gradient__bg">

      <div className='slrspot__home-container'>

        <div className='slrspot__home-header'>
          <h1 className='gradient__text'>Move work forward</h1>
          <p>SLR Spot is the easiest way for teams to manage their work through systematic literature review</p>
          <button>get started</button>
        </div>


        {isAccessPopup && (
          <AccessPopup 
            trigger={isAccessPopup} 
            isSignIn={isSignInPopup}
            isSignUp={isSignUpPopup}
            setTrigger={setIsAccessPopup}
            setIsSignIn={setIsSignInPopup}
            setIsSignUp={setIsSignUpPopup} /> 
        )}
      </div>

    </div>
  );
}

export default HomePage