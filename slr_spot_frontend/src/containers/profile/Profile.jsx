import React from 'react'
import { CgProfile } from "react-icons/cg";
import './profile.css'


const Profile = () => {
  return (
    <div className='slrspot__profile'>
      <div className='slrspot__profile-header'>
        <h2>Settings</h2>
      </div>
      <div className='slrspot__profile-container'>
        <div className='slrspot__profile-menu'>
          <p>Profile</p>
        </div>
        <div className='slrspot__profile-profileSettings'>
          <div className='slrspot__profile-avatar'>
            <p>Your Avatar</p>
            <CgProfile size={100}></CgProfile>
            <button>Update</button>
          </div>
          <div className='slrspot__profile-name'>
            <label>First name</label>
            <input/>
            <label>Last name</label>
            <input/>
            <button>Update</button>
          </div>
          <div className='slrspot__profile-email'>
            <label>Change your email</label>
            <input/>
            <button>Update</button>
          </div>
          <div className='slrspot__profile-password'>
            <label>Old password</label>
            <input/>
            <label>New Password</label>
            <input/>
            <label>Confirm New Password</label>
            <input/>
            <button>Update</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Profile