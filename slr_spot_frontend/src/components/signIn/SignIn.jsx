import React, { useState, useRef } from "react";
import './signIn.css';
import { Navigate, useNavigate  } from 'react-router-dom';
import { login } from "../../actions/auth";
import { useDispatch } from "react-redux";


const SignIn = () => {
  let navigate = useNavigate();
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

  // if (isLoggedIn) {
  //   return <Navigate to="/profile" />;
  // }

  return (
    <form onSubmit={handleLogin} ref={form}>
      <div className="slrspot___signIn-container">
        <h3>Sign In</h3>
        <p>New user? Create an account</p>

        <div className="form-group">
          <label htmlFor='email'>Email:</label>
          <input 
            type='email' 
            className='slrspot__signIn-inputField' 
            placeholder='Email' 
            name='email' 
            required
            onChange={onChangeUsername}
          />
        </div>

        <div className="form-group">
          <label htmlFor='password'>Password:</label>
          <input 
            type='password' 
            className='slrspot__signIn-inputField' 
            placeholder='Password' 
            name='password' 
            required
            onChange={onChangePassword}
          />
        </div>

        <p>Forgot password</p>
        <button type="submit" className='slrspot__signIn-submitBtn' disabled={loading}>
          {loading && (
            <span className="spinner-border spinner-border-sm">LOADING</span>
          )}
          <span>Login</span>
        </button>

        <div className='slrspot___signIn-auth_container'>
          <p>OR</p>
          <h2>Sign in with Google</h2>
          <h2>Sign in with Facebook</h2>
        </div>
      </div>
    </form>
  )
}

export default SignIn