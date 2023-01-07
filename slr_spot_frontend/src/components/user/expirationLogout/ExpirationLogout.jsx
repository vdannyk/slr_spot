import React, { useState } from 'react'
import { useNavigate } from "react-router-dom";
import './expirationLogout.css'


const ExpirationLogout = (props) => {
  const [counter, setCounter] = useState(5);
  const navigate = useNavigate();

  React.useEffect(() => {
    const timer =
      counter > 0 && setInterval(() => setCounter(counter - 1), 1000);
    return () => clearInterval(timer);
  }, [counter]);

  if (counter <= 0) {
    navigate('/');
    props.trigger(false);
  }

  return (
    <div className='slrspot__expirationLogout'>
      <div className='slrspot__expirationLogout-container'>
        <p>You have to sign in again after access token expiration.</p>
        <p>You will be redirected to the home page automatically in: {counter}</p>
        <button onClick={ () => navigate('/') }>Redirect now</button>
      </div>
    </div>
  )
}

export default ExpirationLogout