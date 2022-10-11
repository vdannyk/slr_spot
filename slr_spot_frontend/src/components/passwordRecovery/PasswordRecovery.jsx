import React, { useState, useRef } from "react";

const PasswordRecovery = () => {
  const form = useRef();

  const [password, setPassword] = useState("");

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };


  const handleResetPassword = (e) => {
  };

  return (
    <div>
      <form onSubmit={handleResetPassword} ref={form}>
        <div className="slrspot___passwordRecovery-container">
          <h1>Create Account</h1>
          <input 
            type='password' 
            className='slrspot__passwordRecovery-inputField' 
            placeholder='Password'
            name='password' 
            required
            onChange={onChangePassword}
          />
          <input 
            type='password' 
            className='slrspot__passwordRecovery-inputField' 
            placeholder='Confirm Password' 
          />

          <button type='submit' className='slrspot__passwordRecovery-submitBtn'>Reset password</button>
        </div>
      </form>
      <div class="slrspot__accessPopup-overlay-container">
        <div class="slrspot__accessPopup-overlay">
        </div>
      </div>
    </div>
  )
}

export default PasswordRecovery