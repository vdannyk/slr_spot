import React, { useState, useRef } from "react";
import './signIn.css';
import { login } from "../../actions/auth";
import { useDispatch } from "react-redux";


const SignIn = (props) => {
  const form = useRef();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const dispatch = useDispatch();

  const onChangeUsername = (e) => {
    const username = e.target.value;
    setUsername(username);
  };

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };

  const handleLogin = (e) => {
    e.preventDefault();
    setLoading(true);

    dispatch(login(username, password))
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

  // if (isLoggedIn) {
  //   return <Navigate to="/profile" />;
  // }

  return (
    <div>
      <form onSubmit={handleLogin} ref={form}>
        <div className="slrspot___signIn-container">
          <h1>SIGN IN</h1>
          <p>Enter your details to get sign in to your account</p>
          <input 
            type='email' 
            className='slrspot__signIn-inputField' 
            placeholder='Email' 
            name='email' 
            required
            onChange={onChangeUsername}
          />
          <input 
            type='password' 
            className='slrspot__signIn-inputField' 
            placeholder='Password' 
            name='password' 
            required
            onChange={onChangePassword}
          />

          <a href="#">Forgot your password?</a>
          <button type="submit" className='slrspot__signIn-submitBtn' disabled={loading}>
            Sign In
            {/* {loading && (
              <span className="spinner-border spinner-border-sm">LOADING</span>
            )} */}
          </button>

          {/* <span>-- Or Sign in with --</span>
          <div className='slrspot___signIn-auth_container'>
            <h2>GOOGLE</h2>
            <h2>FACEBOOK</h2>
          </div> */}
          <div>
            <span>Don't have an account?</span>
            <a onClick={() => handleSignUpClick(props) }>Sign Up</a>
          </div>
        </div>
      </form>
      <div class="slrspot__accessPopup-overlay-container">
        <div class="slrspot__accessPopup-overlay">
        </div>
      </div>
    </div>
  )
}

export default SignIn