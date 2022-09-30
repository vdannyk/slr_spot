import React, { useState, useRef } from "react";
import axios from 'axios';
import './signUp.css';

const SignUp = () => {
  const form = useRef();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

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
    var bodyFormData = new FormData();
    bodyFormData.append("firstName", firstName);
    bodyFormData.append("lastName", lastName);
    bodyFormData.append("username", username);
    bodyFormData.append("password", password);
    
    return axios.post("http://localhost:8080/api/user/save", {
      firstName: firstName,
      lastName: lastName,
      email: username,
      password: password,
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
      
  return (
    <form onSubmit={handleRegister} ref={form}>
      <div className="slrspot___signUp-container">
        <h3>Sign Up</h3>

        <div className="form-group">
          <label>First Name:</label>
          <input 
            type='text' 
            className='slrspot__signUp-inputField' 
            placeholder='First Name'
            name='firstName' 
            required
            onChange={onChangeFirstName}
          />
        </div>

        <div className="form-group">
          <label>Last Name:</label>
          <input 
            type='text' 
            className='slrspot__signUp-inputField' 
            placeholder='Last Name'
            name='lastName' 
            required
            onChange={onChangeLastName}
          />
        </div>

        <div className="form-group">
          <label>Email:</label>
          <input 
            type='email' 
            className='slrspot__signUp-inputField' 
            placeholder='Email'
            name='email' 
            required
            onChange={onChangeUsername}
          />
        </div>

        <div className="form-group">
          <label>Password:</label>
          <input 
            type='password' 
            className='slrspot__signUp-inputField' 
            placeholder='Password'
            name='password' 
            required
            onChange={onChangePassword}
          />
        </div>

        <div className="form-group">
          <label>Confirm Password:</label>
          <input 
            type='password' 
            className='slrspot__signUp-inputField' 
            placeholder='Confirm Password' 
          />
        </div>

        <button type='submit' className='slrspot__signUp-submitBtn'>Register</button>
      </div>
    </form>
  )
}

export default SignUp