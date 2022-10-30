import React from 'react'
import { CgProfile } from "react-icons/cg";
import { useDispatch, useSelector } from "react-redux";
import './profile.css'


const Profile = () => {
  const { user: currentUser } = useSelector((state) => state.auth);

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
            <div className='slrspot__profile-avatar'>
              <p>Your Avatar</p>
              <CgProfile size={100}></CgProfile>
              <button>Change avatar</button>
            </div>
            <div className='slrspot__profile-name'>
              <label>First name</label>
              <input placeholder={currentUser.firstName}/>
              <label>Last name</label>
              <input placeholder={currentUser.lastName}/>
              <button>Save</button>
            </div>
          </div>
          <h2>Change your email</h2>
          <div className='slrspot__profile-email'>
            <label>Email</label>
            <input placeholder={currentUser.username}/>
            <button>Save</button>
          </div>
          <h2>Change your password</h2>
          <div className='slrspot__profile-password'>
            <label>Old password</label>
            <input/>
            <label>New Password</label>
            <input/>
            <label>Confirm New Password</label>
            <input/>
            <button>Save</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Profile