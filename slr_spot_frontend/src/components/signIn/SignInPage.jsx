import React, { useState } from "react";
import { AccessPopup } from '../../containers'

const SignInPage = () => {
  const [isAccessPopup, setIsAccessPopup] = useState(true);
  const [isSignInPopup, setIsSignInPopup] = useState(true);
  const [isSignUpPopup, setIsSignUpPopup] = useState(false);

  return (
    <div>
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
  )
}

export default SignInPage