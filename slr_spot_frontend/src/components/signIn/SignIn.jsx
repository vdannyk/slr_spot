import React from 'react'

const SignIn = () => {
  return (
    <form>
        <div className="form-inner">
            <h3>Sign In</h3>
            <p>New user? Create an account</p>

            <div className="form-group">
                <label htmlFor='email'>Email:</label>
                <input type='email' name='email' id='email'/>
            </div>

            <div className="form-group">
                <label htmlFor='password'>Password:</label>
                <input type='password' name='password' id='password'/>
            </div>

            <p>Forgot password</p>
            <button type="submit">Login</button>

            <p>OR</p>
            <h2>Sign in with Google</h2>
            <h2>Sign in with Facebook</h2>
        </div>
    </form>
  )
}

export default SignIn