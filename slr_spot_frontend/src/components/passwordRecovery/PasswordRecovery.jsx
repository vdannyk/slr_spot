import React, { useState, useRef } from "react";
import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { IoMdCheckmarkCircleOutline } from "react-icons/io";
import { RiErrorWarningLine } from "react-icons/ri";
import './passwordRecovery.css'

const PasswordRecovery = () => {
  const {register, handleSubmit, watch, formState: { errors }} = useForm();
  const { resetToken } = useParams();
  const [isLoading, setLoading] = useState(true);
  const [isSuccessful, setIsSuccessful] = useState(false);
  const [isChangeSuccessful, setIsChangeSuccessful] = useState(false);
  const navigate = useNavigate();

  const onHomeClick = () => {
    navigate('/');
  };

  const onLoginClick = () => {
    navigate('/signin');
  };

  useEffect(() => {
    axiosInstance.get("/password/forgot", { params: {
      resetToken
    }})
    .then(() => {
      setLoading(false);
      setIsSuccessful(true);
    })
    .catch(() => {
      setLoading(false);
      setIsSuccessful(false);
    });
  }, []);

  const onSubmit = (formData) => {
    axiosInstance.post("/password/reset", {
      token: resetToken,
      newPassword: formData.password
    })
    .then(() => {
      setIsChangeSuccessful(true);
    })
    .catch(() => {
      setIsChangeSuccessful(false);
    });
  };

  if (isChangeSuccessful) {
    return (
      <div className="slrspot___passwordRecovery">
        <div className="slrspot___passwordRecovery-box">
          <IoMdCheckmarkCircleOutline size={150} color='#2ae158'></IoMdCheckmarkCircleOutline>
          <h1>Success!</h1>
          <p>You have successfuly changed account password.</p>
          <p>Please login to your account again.</p>
          <button onClick={onLoginClick}>Log in</button>
        </div>
      </div>
    );
  }

  if (!isSuccessful) {
    return (
      <div className="slrspot___passwordRecovery">
        <div className="slrspot___passwordRecovery-box">
          <RiErrorWarningLine size={150} color='red' style={{ "margin-top": '50px'}}></RiErrorWarningLine>
          <h1>Reset password failed!</h1>
          <p>We were unable to procces reset token.</p>
          <button style={{ background:'red'}} onClick={onHomeClick}>Home</button>
        </div>
      </div>
    );
  }

  return (
    <div className="slrspot___passwordRecovery">
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="slrspot___passwordRecovery-box">
          <h1>Set new password</h1>
          <input
            {...register("password", { 
              required: true,
            })}
            type='password' 
            placeholder='Password'
            name='password'   
          />
          {errors.password && 
            <p className="slrspot__signIn-error">This field is required</p>
          }
          <input 
            {...register("confirmPassword", { 
              required: true, 
              validate: (value) => {
                return watch('password') == value;}
            })}
            type='password' 
            placeholder='Confirm Password'
            name='confirmPassword'
          />
          {errors.confirmPassword && errors.confirmPassword.type === "validate" &&  
            <p className="slrspot__signIn-error">Passwords do not match</p>
          }
          {errors.confirmPassword && errors.confirmPassword.type=== "required" && 
            <p className="slrspot__signIn-error">This field is required</p>
          }
          <button type='submit' className='slrspot__passwordRecovery-submitBtn'>Reset password</button>
        </div> 
      </form>
    </div>
  )
}

export default PasswordRecovery