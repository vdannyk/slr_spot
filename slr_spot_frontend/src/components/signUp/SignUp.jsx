import React from 'react';
import './signUp.css';

const SignUp = () => {
  return (
    <form>
        <div className="slrspot___signUp-container">
            <h3>Sign Up</h3>

            <input type='text' className='slrspot__signUp-inputField' placeholder='First Name' />

            <input type='text' className='slrspot__signUp-inputField' placeholder='Last Name' />

            <input type='email' className='slrspot__signUp-inputField' placeholder='Email' />

            <input type='password' className='slrspot__signUp-inputField' placeholder='Password' />

            <input type='password' className='slrspot__signUp-inputField' placeholder='Confirm Password' />

            <button type='submit' className='slrspot__signUp-submitBtn'>Register</button>
        </div>
    </form>
  )
}

export default SignUp