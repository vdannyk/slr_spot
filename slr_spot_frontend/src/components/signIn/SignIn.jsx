import React, { useState, useRef } from "react";
// import { useDispatch, useSelector } from "react-redux";
import { Navigate, useNavigate  } from 'react-router-dom';

import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import { login } from "../../actions/auth";

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
  let navigate = useNavigate();

  const form = useRef();
  const checkBtn = useRef();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  // const { isLoggedIn } = useSelector(state => state.auth);
  // const { message } = useSelector(state => state.message);

  // const dispatch = useDispatch();

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

    form.current.validateAll();

    login(username, password);
    setLoading(false);
    // if (checkBtn.current.context._errors.length === 0) {
    //   dispatch(login(username, password))
    //     .then(() => {
    //       navigate("/profile");
    //       window.location.reload();
    //     })
    //     .catch(() => {
    //       setLoading(false);
    //     });
    // } else {
    //   setLoading(false);
    // }
  };

  // if (isLoggedIn) {
  //   return <Navigate to="/profile" />;
  // }

  return (
    <Form onSubmit={handleLogin} ref={form}>
        <div className="form-inner">
            <h3>Sign In</h3>
            <p>New user? Create an account</p>

            <div className="form-group">
                <label htmlFor='email'>Email:</label>
                <Input 
                  type='text' 
                  className="form-control" 
                  name='email' 
                  value={username} 
                  onChange={onChangeUsername} 
                  validations={[required]}
                />
            </div>

            <div className="form-group">
                <label htmlFor='password'>Password:</label>
                <Input 
                  type='password' 
                  className='form-control' 
                  name='password' 
                  value={password} 
                  onChange={onChangePassword} 
                  validations={[required]}
                />
            </div>

            <p>Forgot password</p>
            <button type="submit">Login</button>

            <p>OR</p>
            <h2>Sign in with Google</h2>
            <h2>Sign in with Facebook</h2>
        </div>
    </Form>
  )
}

export default SignIn