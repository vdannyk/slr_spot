import React, { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { register } from "../../actions/auth";
import './signUp.css';

const SignUp = () => {
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
      
  return (
    <div>
      <form onSubmit={handleRegister} ref={form}>
        {!successful && (
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
        )}
      </form>
      {message && (
        <div className="form-group">
            {message}
        </div>
      )}
    </div>
  )
}

export default SignUp