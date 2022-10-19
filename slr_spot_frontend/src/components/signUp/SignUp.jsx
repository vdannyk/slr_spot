import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register as registerAccount } from "../../actions/auth";
import { useForm } from "react-hook-form";
import { IoMdCheckmarkCircleOutline } from "react-icons/io";
import { BeatLoader } from "react-spinners";
import './signUp.css';


const SignUp = (props) => {
  const {register, handleSubmit, watch, formState: { errors }} = useForm();
  const [isSuccessful, setIsSuccessful] = useState(false);
  const { message } = useSelector(state => state.message);
  const dispatch = useDispatch();
  const [loading, setLoading] = useState(false);

  const onSubmit = (formData) => {
    setLoading(true);
    setIsSuccessful(false);

    dispatch(registerAccount(formData.firstName, 
                              formData.lastName, 
                              formData.email, 
                              formData.password))
      .then(() => {
        setIsSuccessful(true);
        setLoading(true);
      })
      .catch(() => {
        setIsSuccessful(false);
        setLoading(false);
      });
  };

  const handleSignInClick = (props) => {
    props.signInTrigger(true);
    props.signUpTrigger(false);
  };
      
  return (
    <div className='slrspot___signUp'>
      <form onSubmit={handleSubmit(onSubmit)}>
        {!isSuccessful && (
          <div className='slrspot___signUp-container'>
            <div className="slrspot__signUp-header">
              <h1>Create Account</h1>
              {message && (
                <div style={{color: 'red'}}>
                  <p>{message}</p>
                </div>
              )}
            </div>

            <div className="slrspot___signUp-fields">
              <div className="slrspot__signUp-field">
                <input 
                  {...register("firstName", { 
                    required: true,
                    validate: () => {
                      return watch('firstName').length <= 50;}
                  })}
                  type='text' 
                  placeholder='First Name'
                  name='firstName' 
                />
                {errors.firstName && errors.firstName.type === "required" && 
                  <p className="slrspot__signIn-error">This field is required</p>
                }
                {errors.firstName && errors.firstName.type === "validate" &&  
                  <p className="slrspot__signIn-error">Input is too long</p>
                }
              </div>
              <div className="slrspot__signUp-field">
                <input
                  {...register("lastName", { 
                    required: true,
                    validate: () => {
                      return watch('lastName').length <= 50;}
                  })}
                  type='text' 
                  placeholder='Last Name'
                  name='lastName' 
                />
                {errors.lastName && errors.lastName.type === "required" &&  
                  <p className="slrspot__signIn-error">This field is required</p>
                }
                {errors.lastName && errors.lastName.type === "validate" &&  
                  <p className="slrspot__signIn-error">Input is too long</p>
                }
              </div>
              <div className="slrspot__signUp-field">
                <input
                  {...register("email", { 
                    required: true, 
                    pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                    validate: () => {
                      return watch('email').length <= 65;}  
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
                {errors.email && errors.email.type === "validate" &&  
                  <p className="slrspot__signIn-error">Input is too long</p>
                }
              </div>
              <div className="slrspot__signUp-field">
                <input
                  {...register("password", { 
                    required: true,
                  })}
                  type='password' 
                  placeholder='Password'
                  name='password'   
                />
                {errors.password && 
                  <p className="slrspot__signIn-error">This field is required</p>
                }
              </div>
              <div className="slrspot__signUp-field">
                <input 
                  {...register("confirmPassword", { 
                    required: true, 
                    validate: (value) => {
                      return watch('password') == value;}
                  })}
                  type='password' 
                  placeholder='Confirm Password'
                  name='confirmPassword'
                />
                {errors.confirmPassword && errors.confirmPassword.type === "validate" &&  
                  <p className="slrspot__signIn-error">Passwords do not match</p>
                }
                {errors.confirmPassword && errors.confirmPassword.type=== "required" && 
                  <p className="slrspot__signIn-error">This field is required</p>
                }
              </div>
            </div>
            <div className="slrspot___signUp-submit">
                <button type='submit'>Register</button>
                { loading && (<BeatLoader color="#AE67FA" />)}
              </div>
              <div className="slrspot___signUp-haveAccount">
                <span>Already have an account?</span>
                <a onClick={() => handleSignInClick(props) }>Sign In</a>
              </div>
          </div>
        )}
      </form>
      {isSuccessful && (
        <div className='slrspot___signUp-container'>
          <div className="slrspot___signUp-success">
          <IoMdCheckmarkCircleOutline size={150} color='#2ae158'></IoMdCheckmarkCircleOutline>
          <h1 style={{color: '#2ae158'}}>Success!</h1>
          <p>Your account has been created.</p>
          <p>To activate your account check your email and confirm your registration.</p>
          <button onClick={() => handleSignInClick(props) }>Sign In</button>
          </div>
        </div>
      )}
      <div className="slrspot__accessPopup-overlay-container">
        <div className="slrspot__accessPopup-overlay">
          <h2>Welcome</h2>
          <p>Get started with your free account</p>
        </div>
      </div>
    </div>
  )
}

export default SignUp