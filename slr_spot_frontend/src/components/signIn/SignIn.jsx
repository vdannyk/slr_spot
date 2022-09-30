import React, { useState, useRef } from "react";
import axios from 'axios';
import './signIn.css';

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

const SignIn = () => {
  const form = useRef();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

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
    var bodyFormData = new FormData();
    bodyFormData.append("username", username);
    bodyFormData.append("password", password);
    
    return axios({
      method: "post",
      url: "http://localhost:8080/api/auth/signin",
      data: bodyFormData,
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(function (response) {
      // handle success
      console.log(response);
    })
    .catch(function (response) {
      // handle error
      console.log(response);
    })
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
                  id='email' 
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
                  id='password' 
                  required
                  onChange={onChangePassword}
                />
            </div>

            <p>Forgot password</p>
            <button type="submit" className='slrspot__signIn-submitBtn'>Login</button>

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