import React, { useState } from 'react'
import { CgProfile } from "react-icons/cg";
import { useDispatch, useSelector } from "react-redux";
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { refreshToken } from "../../actions/auth";
import TokenService from "../../services/token.service";
import './profile.css'


const Profile = () => {
  const [isNameChangeSuccessful, setIsNameChangeSuccessful] = useState(false);
  const [isEmailChangeSuccessful, setIsEmailChangeSuccessful] = useState(false);
  const [isPasswordChangeSuccessful, setIsPasswordChangeSuccessful] = useState(false);
  const dispatch = useDispatch();
  const { user: currentUser } = useSelector((state) => state.auth);
  const {register, handleSubmit, watch, formState: { errors }} = useForm();
  const {
    register: registerName, 
    handleSubmit: handleSubmitName, 
    watch: watchName, 
    formState: { errors: errorsName }
  } = useForm();
  const {
    register: registerPassword, 
    handleSubmit: handleSubmitPassword, 
    watch: watchPassword, 
    formState: { errors: errorsPassword }
  } = useForm();
  

  const onEmailSubmit = (formData) => {
    console.log(formData);
    axiosInstance.post("/users/email/update", {
      email: formData.email
    })
    .then(() => {
      setIsEmailChangeSuccessful(true);
    })
    .catch((response) => {
      console.log(response);
    });
  };

  const onNameSubmit = (formData) => {
    axiosInstance.post("/users/name/update", {
      firstName: formData.firstName,
      lastName: formData.lastName
    })
    .then(() => {
      setIsNameChangeSuccessful(true);
      TokenService.updateUser(formData);
      window.location.reload();
    })
    .catch((response) => {
      console.log(response);
    });
  };

  const onPasswordSubmit = (formData) => {
    axiosInstance.post("/users/password/update", {
      oldPassword: formData.oldPassword,
      newPassword: formData.newPassword,
      confirmPassword: formData.confirmPassword
    })
    .then(() => {
      setIsPasswordChangeSuccessful(true);
    })
    .catch((response) => {
      console.log(response);
    });
  };

  return (
    <div className='slrspot__profile'>

      <div className='slrspot__profile-header'>
        <h1>Settings</h1>
      </div>

      <div className='slrspot__profile-container'>

        <div className='slrspot__profile-menu'>
          <p>Profile</p>
        </div>
        
        <div className='slrspot__profile-profileSettings'>

          <div className='slrspot__profile-baseInfo'>
            <div className='slrspot__profile-avatar' hidden>
              <p>Your Avatar</p>
              <CgProfile size={100}></CgProfile>
              <button>Change avatar</button>
            </div>
            
            <form onSubmit={handleSubmitName(onNameSubmit)}>
              <div className='slrspot__profile-name'>
                <label>First name</label>
                <input 
                  {...registerName("firstName", { 
                    required: true,
                    validate: () => {
                      return watchName('firstName').length <= 50;}
                  })}
                  type='text' 
                  placeholder={currentUser.firstName}
                  name='firstName' 
                />
                {errorsName.firstName && errorsName.firstName.type === "required" && 
                  <p className="slrspot__profile-error">This field is required</p>
                }
                {errorsName.firstName && errorsName.firstName.type === "validate" &&  
                  <p className="slrspot__profile-error">Input is too long</p>
                }
                <label>Last name</label>
                <input
                  {...registerName("lastName", { 
                    required: true,
                    validate: () => {
                      return watchName('lastName').length <= 50;}
                  })}
                  type='text' 
                  placeholder={currentUser.lastName}
                  name='lastName' 
                />
                {errorsName.lastName && errorsName.lastName.type === "required" &&  
                  <p className="slrspot__profile-error">This field is required</p>
                }
                {errorsName.lastName && errorsName.lastName.type === "validate" &&  
                  <p className="slrspot__profile-error">Input is too long</p>
                }
                {isNameChangeSuccessful && (
                  <p className="slrspot__profile-error" style={{color:'green'}}>Success</p>
                )}
                <button type='submit'>Save</button>
              </div>
            </form>
          </div>

          <h2>Change your email</h2>
          <form onSubmit={handleSubmit(onEmailSubmit)}>
            <div className='slrspot__profile-email'>
              <label>Email</label>
              <input
                {...register("email", { 
                  required: true, 
                  pattern: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                  validate: () => {
                    return watch('email').length <= 65;}  
                })}
                placeholder={currentUser.username}
                name='email'
              />
              {errors.email && errors.email.type === "pattern" &&  
                <p className="slrspot__profile-error">Invalid email</p>
              }
              {errors.email && errors.email.type=== "required" && 
                <p className="slrspot__profile-error">This field is required</p>
              }
              {errors.email && errors.email.type === "validate" &&  
                <p className="slrspot__profile-error">Input is too long</p>
              }
              {isEmailChangeSuccessful && (
                <p className="slrspot__profile-error" style={{color:'green'}}>Activation link send</p>
              )}
              <button type="submit">Save</button>
            </div>
          </form>

          <h2>Change your password</h2>
          <form onSubmit={handleSubmitPassword(onPasswordSubmit)}>
            <div className='slrspot__profile-password'>
              <label>Old password</label>
              <input 
                {...registerPassword("oldPassword", { 
                  required: true,
                })}
                type='password' 
                placeholder='Old Password'
                name='oldPassword'
              />
              {errorsPassword.oldPassword && errorsPassword.oldPassword.type=== "required" && 
                <p className="slrspot__profile-error">This field is required</p>
              }
              <label>New Password</label>
              <input
                {...registerPassword("newPassword", { 
                  required: true,
                  validate: (value) => {
                    return watchPassword('oldPassword') != value;}
                })}
                type='password' 
                placeholder='New Password'
                name='newPassword'   
              />
              {errorsPassword.newPassword && errorsPassword.newPassword.type === "validate" &&  
                <p className="slrspot__profile-error">New password cannot be the same as your current password</p>
              }
              {errorsPassword.newPassword && errorsPassword.newPassword.type=== "required" && 
                <p className="slrspot__profile-error">This field is required</p>
              }
              <label>Confirm New Password</label>
              <input 
                {...registerPassword("confirmPassword", { 
                  required: true, 
                  validate: (value) => {
                    return watchPassword('newPassword') == value;}
                })}
                type='password' 
                placeholder='Confirm Password'
                name='confirmPassword'
              />
              {errorsPassword.confirmPassword && errorsPassword.confirmPassword.type === "validate" &&  
                <p className="slrspot__profile-error">Passwords do not match</p>
              }
              {errorsPassword.confirmPassword && errorsPassword.confirmPassword.type=== "required" && 
                <p className="slrspot__profile-error">This field is required</p>
              }
              {isPasswordChangeSuccessful && (
                <p className="slrspot__profile-error" style={{color:'green'}}>Password changed</p>
              )}
              <button type="submit">Save</button>
            </div>
          </form>

        </div>
      </div>
    </div>
  )
}

export default Profile