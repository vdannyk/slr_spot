import React, { useState, useCallback, useEffect } from 'react'
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
        ExpirationLogout {counter}
      </div>
    </div>
  )
}

export default ExpirationLogout