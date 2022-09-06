import React, { useState } from 'react'
import { RiCloseLine, RiMenu3Line } from 'react-icons/ri';
import './navbar.css'
import slrspot_logo from '../../assets/slrspot_logo.png';
import { LoginPopup } from '../../containers'

const Menu = () => (
  <>
    <p><a href='#home'>Home</a></p>
    <p><a href='#whspot'>What</a></p>
    <p><a href='#home'>Test2</a></p>
  </>
)

const useScrollLock = () => {
  const lockScroll = () => {
    document.body.style.overflow = 'hidden';
  }

  const unlockScroll = () => {
    document.body.style.overflow = '';
  }
  return {
    lockScroll,
    unlockScroll
  }
}

const Navbar = () => {
  const [toggleMenu, setToggleMenu] = useState(false);
  const [loginPopup, setLoginPopup] = useState(false);
  const { lockScroll, unlockScroll } = useScrollLock();

  loginPopup ? lockScroll() : unlockScroll();

  return (
    <div className='slrspot__navbar'>
      <div className='slrspot__navbar-links'>
        <div className='slrspot__navbar-links_logo'>
          <img src={slrspot_logo} />
        </div>
        <div className='slrspot__navbar-links-container'>
          <Menu />
        </div>
      </div>
      <div className='slrspot__navbar-sign'>
        <p onClick={() =>  setLoginPopup(true)}>Sign in</p>
        <button type='button' onClick={() =>  setLoginPopup(true)}>Sign up</button>
      </div>
      <div className='slrspot__navbar-menu'>
        {toggleMenu
          ? <RiCloseLine color='#fff' size={27} onClick={() => setToggleMenu(false)} />
          : <RiMenu3Line color='#fff' size={27} onClick={() => setToggleMenu(true)} />}
        {toggleMenu && (
          <div className='slrspot__navbar-menu_container scale-up-center'>
            <div className='slrspot__navbar-menu_container-links'>
              <Menu />
              <div className='slrspot__navbar-menu_container-links-sign'>
                <p>Sign in</p>
                <button type='button'>Sign up</button>
              </div>
            </div>
          </div>
        )}
      </div>
      <LoginPopup trigger={loginPopup} setTrigger={setLoginPopup} />
    </div>
  )
}

export default Navbar