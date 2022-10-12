import React, { useState, useRef } from 'react'
import axiosInstance from "../../services/api";
import './forgotPassword.css';

const ForgotPassword = (props) => {
  const form = useRef();
  const [email, setEmail] = useState("");

  const onChangeEmail = (e) => {
    const email = e.target.value;
    setEmail(email);
  };

  const handleForgotPassword = () => {
    return axiosInstance.post(`/user/resetpassword`, null, { params: {
      email
    }})
    .then(function (response) {
      // handle success
      console.log(response);
    })
    .catch(function (response) {
      // handle error
      console.log(response);
    });
  };

  const handleBackToLoginClick = (props) => {
    props.signInTrigger(true);
    props.forgotPasswordTrigger(false);
  };

  return (
    <div>
      <form onSubmit={handleForgotPassword} ref={form}>
        <div className="slrspot___forgotPassword-container">
          <h1>RESET PASSWORD</h1>
          <p>Enter the email address associated with your account and we'll send you a link to reset your password</p>
          <input 
            type='email' 
            className='slrspot__forgotPassword-inputField' 
            placeholder='Email' 
            name='email' 
            required
            onChange={onChangeEmail}
          />

          <button type="submit" className='slrspot__forgotPassword-submitBtn'>
            Submit
          </button>

          <div>
            <a onClick={() => handleBackToLoginClick(props) }>Back to login</a>
          </div>
        </div>
      </form>
    </div>
  )
}

export default ForgotPassword