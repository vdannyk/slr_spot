import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register } from "../../actions/auth";
import './signUp.css';

const SignUp = (props) => {
  const form = useRef();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [successful, setSuccessful] = useState(false);

  const { message } = useSelector(state => state.message);

  const dispatch = useDispatch();

  const onChangeFirstName = (e) => {
    const firstName = e.target.value;
    setFirstName(firstName);
  };

  const onChangeLastName = (e) => {
    const lastName = e.target.value;
    setLastName(lastName);
  };

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const handleRegister = (e) => {
    e.preventDefault();

    setSuccessful(false);

    dispatch(register(firstName, lastName, username, password))
      .then(() => {
        setSuccessful(true);
        // window.location.reload();
      })
      .catch(() => {
        setSuccessful(false);
      });
  };

  const handleSignInClick = (props) => {
    props.signInTrigger(true);
    props.signUpTrigger(false);
  };
      
  return (
    <div className='slrspot___signUp'>
    <form onSubmit={handleRegister} ref={form}>
      {!successful && (
        <div className='slrspot___signUp-container'>
          <h1>Create Account</h1>
          <input 
            type='text' 
            className='slrspot__signUp-inputField' 
            placeholder='First Name'
            name='firstName' 
            required
            onChange={onChangeFirstName}
          />
          <input 
            type='text' 
            className='slrspot__signUp-inputField' 
            placeholder='Last Name'
            name='lastName' 
            required
            onChange={onChangeLastName}
          />
          <input 
            type='email' 
            className='slrspot__signUp-inputField' 
            placeholder='Email'
            name='email' 
            required
            onChange={onChangeUsername}
          />
          <input 
            type='password' 
            className='slrspot__signUp-inputField' 
            placeholder='Password'
            name='password' 
            required
            onChange={onChangePassword}
          />
          <input 
            type='password' 
            className='slrspot__signUp-inputField' 
            placeholder='Confirm Password' 
          />

          <button type='submit' className='slrspot__signUp-submitBtn'>Register</button>
          {/* <span>-- Or Sign in with --</span>
          <div className='slrspot___signIn-auth_container'>
            <h2>GOOGLE</h2>
            <h2>FACEBOOK</h2>
          </div> */}
          <div>
            <span>Already have an account?</span>
            <a onClick={() => handleSignInClick(props) }>Sign In</a>
          </div>
        </div>
      )}
    </form>
      <div class="slrspot__accessPopup-overlay-container">
        <div class="slrspot__accessPopup-overlay">
        </div>
      </div>
    </div>
  )
}

export default SignUp