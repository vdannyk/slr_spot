import React, { useState } from 'react';
import AccessPopup from '../access/AccessPopup';
import { useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';
import './homePage.css'

const HomePage = (props) => {
  const [isAccessPopup, setIsAccessPopup] = useState(props.isAccessPopup);
  const [isSignInPopup, setIsSignInPopup] = useState(props.isSignInPopup);
  const [isSignUpPopup, setIsSignUpPopup] = useState(props.isSignUpPopup);
  const { isLoggedIn } = useSelector(state => state.auth);
  const navigate = useNavigate();

  const onLoginClick = () => {
    setIsAccessPopup(true);
    setIsSignInPopup(true);
  };

  return (
    <div className="slrspot__homePage gradient__bg">

      <div className='slrspot__home-container'>

        <div className='slrspot__home-header'>
          <h1>Speed up your systematic literature review</h1>
        </div>

        <div className='slrspot__home-content'>

          <div className='slrspot__home-features'>

            <div className='slrspot__home-feature'>
              <p>1. Create new review</p>
            </div>
            <div className='slrspot__home-feature'>
              <p>2. Add your teammates</p>
            </div>
            <div className='slrspot__home-feature'>
              <p>3. Import studies</p>
            </div>
            <div className='slrspot__home-feature'>
              <p>4. Set up your criteria, keywords and tags</p>
            </div>

          </div>

          <div className='slrspot__home-features'>

            <div className='slrspot__home-feature'>
              <p>5. Perform title&abstract and full-text screening</p>
            </div>
            <div className='slrspot__home-feature'>
              <p>6. Export results of your systematic literature review</p>
            </div>
            { isLoggedIn 
            ? <button onClick={ () => navigate('/reviews') }>Get started!</button> 
            : <button onClick={ onLoginClick }>Get started!</button>
            }
          </div>

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