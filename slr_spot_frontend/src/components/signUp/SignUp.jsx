import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register as registerAccount } from "../../actions/auth";
import { useForm } from "react-hook-form";
import './signUp.css';
import { BeatLoader } from "react-spinners";


const SignUp = (props) => {
  const {register, handleSubmit, watch, formState: { errors }} = useForm();
  const [successful, setSuccessful] = useState(false);
  const { message } = useSelector(state => state.message);
  const dispatch = useDispatch();
  const [loading, setLoading] = useState(false);

  const onSubmit = (formData) => {
    setLoading(true);
    setSuccessful(false);

    dispatch(registerAccount(formData.firstName, 
                              formData.lastName, 
                              formData.email, 
                              formData.password))
      .then(() => {
        setSuccessful(true);
        setLoading(true);
        window.location.reload();
      })
      .catch(() => {
        setSuccessful(false);
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
      {!successful && (
        <div className='slrspot___signUp-container'>
          <div className="slrspot___signUp-fields">
            <h1>Create Account</h1>
            {message && (
              <div className="form-group">
                <div className="alert alert-danger" style={{color: 'red'}}>
                  {message}
                </div>
              </div>
            )}
            <input 
              {...register("firstName", { required: true })}
              type='text' 
              placeholder='First Name'
              name='firstName' 
            />
            {errors.firstName && 
              <p className="slrspot__signIn-error">This field is required</p>
            }
            <input
              {...register("lastName", { required: true })}
              type='text' 
              placeholder='Last Name'
              name='lastName' 
            />
            {errors.lastName && 
              <p className="slrspot__signIn-error">This field is required</p>
            }
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

            <button type='submit'>Register</button>
            { loading && (<BeatLoader color="#AE67FA" />)}
            <div className="slrspot___signIn-haveAccount">
              <span>Already have an account?</span>
              <a onClick={() => handleSignInClick(props) }>Sign In</a>
            </div>
          </div>
        </div>
      )}
    </form>
      <div class="slrspot__accessPopup-overlay-container">
        <div class="slrspot__accessPopup-overlay">
          <h2>Welcome</h2>
          <p>Get started with your free account</p>
        </div>
      </div>
    </div>
  )
}

export default SignUp