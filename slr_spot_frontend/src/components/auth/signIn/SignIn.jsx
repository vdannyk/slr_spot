import React, { useState } from "react";
import './signIn.css';
import { login } from "../../../actions/auth";
import { useDispatch, useSelector } from "react-redux";
import { useForm } from "react-hook-form";
import { BeatLoader } from "react-spinners";


const SignIn = (props) => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const { message } = useSelector(state => state.message);

  const dispatch = useDispatch();

  const onSubmit = (formData) => {
    setLoading(true);

    dispatch(login(formData.email, formData.password))
      .then(() => {
        window.location.reload();
      })
      .catch(() => {
        setLoading(false);
      });
  };

  const handleSignUpClick = (props) => {
    props.signInTrigger(false);
    props.signUpTrigger(true);
  };

  const handleForgotPasswordClick = (props) => {
    props.signInTrigger(false);
    props.forgotPasswordTrigger(true);
  };

  return (
    <div className="slrspot___signIn">
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="slrspot___signIn-container">
          <div className="slrspot__signIn-header">
            <h1>SIGN IN</h1>
            <p>Enter your details to get sign in to your account</p>
            {message && (
              <div style={{color: 'red'}}>
                <p>{message}</p>
              </div>
            )}
          </div>
          <div className="slrspot___signIn-fields">
            <div className="slrspot__signIn-field">
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
            </div>
            
            <div className="slrspot__signIn-field">
              <input 
                {...register("password", { required: true })}
                type='password'
                placeholder='Password' 
                name='password'
              />
              {errors.password && 
                <p className="slrspot__signIn-error">This field is required</p>
              }
              <div className="slrspot___signIn-forgotPassword">
              <a onClick={() => handleForgotPasswordClick(props) }>Forgot your password?</a>
              </div>
            </div>
          </div>

          <div className="slrspot___signIn-submit">
            <button type="submit" className='slrspot__signIn-submitBtn' disabled={loading}>
              Sign In
            </button>
            { loading && (<BeatLoader color="#AE67FA" />)}
          </div>

          <div className="slrspot___signIn-newAccount">
            <span>Don't have an account?</span>
            <a onClick={() => handleSignUpClick(props) }>Sign Up</a>
          </div>
        </div>
      </form>
      <div className="slrspot__accessPopup-overlay-container">
        <div className="slrspot__accessPopup-overlay">
          <h2>Welcome Back</h2>
          <p>Good to see you again. You can sign in to access with your existing account.</p>
        </div>
      </div>
    </div>
  )
}

export default SignIn