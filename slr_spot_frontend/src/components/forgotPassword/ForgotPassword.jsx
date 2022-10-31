import React, { useState } from 'react'
import axiosInstance from "../../services/api";
import { useForm } from "react-hook-form";
import './forgotPassword.css';

const ForgotPassword = (props) => {
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isSuccessful, setIsSuccessful] = useState(false);

  const onSubmit = (formData) => {
    const email = formData.email;
    return axiosInstance.post(`/users/resetPassword`, null, { params: {
      email
    }})
    .then(function (response) {
      // handle success
      setIsSuccessful(true);
      console.log(response);
    })
    .catch(function (response) {
      // handle error
      setIsSuccessful(false);
      console.log(response);
    });
  };

  const handleBackToLoginClick = (props) => {
    props.signInTrigger(true);
    props.forgotPasswordTrigger(false);
  };

  return (
    <div className="slrspot___forgotPassword-container">
      { !isSuccessful && (
      <form onSubmit={handleSubmit(onSubmit)}>
          <h1>RESET PASSWORD</h1>
          <p>Enter the email address associated with your account and we'll send you a link to reset your password</p>
          <input  
            {...register("email", { 
              required: true, 
              pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/  
            })}
            placeholder='Email' 
            name='email' 
          />
          {errors.email && errors.email.type === "pattern" &&  
            <p className="slrspot__signIn-error">Invalid email</p>
          }
          {errors.email && errors.email.type=== "required" && 
            <p className="slrspot__signIn-error">This field is required</p>
          }

          <button type="submit" className='slrspot__forgotPassword-submitBtn'>
            Submit
          </button>

          <div>
            <a onClick={() => handleBackToLoginClick(props) }>Back to login</a>
          </div>
      </form>
      )}
      { isSuccessful && (
        <div>
          <h1>Reset link send</h1>
          <p>Check your email and reset password</p>
          <div>
            <button onClick={() => handleBackToLoginClick(props) }>Back to login</button>
          </div>
        </div>
      )}
    </div>
  )
}

export default ForgotPassword