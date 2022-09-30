import React from 'react';
import './signUp.css';

const SignUp = () => {
  return (
    <form>
        <div className="slrspot___signUp-container">
            <h3>Sign Up</h3>

            <div className="form-group">
                <label>First Name:</label>
                <input type='text' className='slrspot__signUp-inputField' placeholder='First Name' />
            </div>

            <div className="form-group">
                <label>Last Name:</label>
                <input type='text' className='slrspot__signUp-inputField' placeholder='Last Name' />
            </div>

            <div className="form-group">
                <label>Email:</label>
                <input type='email' className='slrspot__signUp-inputField' placeholder='Email' />
            </div>

            <div className="form-group">
                <label>Password:</label>
                <input type='password' className='slrspot__signUp-inputField' placeholder='Password' />
            </div>

            <div className="form-group">
                <label>Confirm Password:</label>
                <input type='password' className='slrspot__signUp-inputField' placeholder='Confirm Password' />
            </div>

            <button type='submit' className='slrspot__signUp-submitBtn'>Register</button>
        </div>
    </form>
  )
}

export default SignUp