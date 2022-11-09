import React, { useState, useCallback, useEffect } from 'react'
import { logout } from "../../actions/auth";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";


import './expirationLogout.css'

const ExpirationLogout = () => {
  const [logoutNow, setLogoutNow] = useState(false);
  const [counter, setCounter] = useState(5);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const logOut = useCallback(() => {
    dispatch(logout())
    .then(() => {
      navigate('/');
    });
  }, [dispatch]);

  React.useEffect(() => {
    const timer =
      counter > 0 && setInterval(() => setCounter(counter - 1), 1000);
    return () => clearInterval(timer);
  }, [counter]);

  if (counter <= 0) {
    logOut()
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