import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const AuthorizationCheck = (props) => {
  const location = useLocation();

  useEffect(() => {
    props.setIsNotAuthorized(false);
  },[location.pathname]);

  return (
    <div>
      dupa
    </div>
  )
}

export default AuthorizationCheck