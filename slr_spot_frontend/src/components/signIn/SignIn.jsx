import React from 'react';
import './signIn.css';

const SignIn = () => {
  return (
    <form>
        <div className="slrspot___signIn-container">
            <h3>Sign In</h3>
            <p>New user? Create an account</p>

            <input type='email' className='slrspot__signIn-inputField' placeholder='Email' name='email' id='email' required/>
            <input type='password' className='slrspot__signIn-inputField' placeholder='Password' name='password' id='password' required/>

            <p>Forgot password?</p>
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