import React, { useState, useRef } from "react";
import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import './passwordRecovery.css'

const PasswordRecovery = () => {
  const form = useRef();
  const { resetToken } = useParams();
  const [isLoading, setLoading] = useState(true);
  const [successful, setSuccessful] = useState(false);
  const [changeSuccessful, setChangeSuccessful] = useState(false);
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    axiosInstance.get("/user/changepassword", { params: {
      resetToken
    }})
    .then(() => {
      setLoading(false);
      setSuccessful(true);
    })
    .catch(() => {
      setLoading(false);
      setSuccessful(false);
    });
  }, []);

  const onChangePassword = (e) => {
    const password = e.target.value;
    setPassword(password);
  };


  const handleResetPassword = () => {
    axiosInstance.post("/user/savePassword", {
      token: resetToken,
      newPassword: password
    })
    .then(() => {
      setChangeSuccessful(true);
      navigate("/");
    })
    .catch(() => {
      setChangeSuccessful(false);
    });
  };

  if (isLoading) {
    return (
      <div className="slrspot___passwordRecovery">
        <div className="slrspot___passwordRecovery-box">
          <h1>Loading...</h1>
        </div>
      </div>
    );
  }

  return (
    <div className="slrspot___passwordRecovery">
      <form onSubmit={handleResetPassword} ref={form}>
        {successful && (
        <div className="slrspot___passwordRecovery-box">
          <h1>Set new password</h1>
          <input 
            type='password' 
            className='slrspot__passwordRecovery-inputField' 
            placeholder='Password'
            name='password' 
            required
            onChange={onChangePassword}
          />
          <input 
            type='password' 
            className='slrspot__passwordRecovery-inputField' 
            placeholder='Confirm Password' 
          />

          <button type='submit' className='slrspot__passwordRecovery-submitBtn'>Reset password</button>
        </div> 
        )}
        {!successful && (
          <div className="slrspot___passwordRecovery-box">
            <h1>INVALID TOKEN</h1>
          </div>
        )}
      </form>
    </div>
  )
}

export default PasswordRecovery