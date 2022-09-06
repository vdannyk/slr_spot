import React from 'react'

const SignUp = () => {
  return (
    <form>
        <div className="form-inner">
            <h3>Sign Up</h3>

            <div className="form-group">
                <label>First Name:</label>
                <input type='text' className='form-control' placeholder='First Name' />
            </div>

            <div className="form-group">
                <label>Last Name:</label>
                <input type='text' className='form-control' placeholder='Last Name' />
            </div>

            <div className="form-group">
                <label>Email:</label>
                <input type='email' className='form-control' placeholder='Email' />
            </div>

            <div className="form-group">
                <label>Password:</label>
                <input type='password' className='form-control' placeholder='Password' />
            </div>

            <div className="form-group">
                <label>Confirm Password:</label>
                <input type='password' className='form-control' placeholder='Confirm Password' />
            </div>

            <button type='submit'>Submit</button>
        </div>
    </form>
  )
}

export default SignUp