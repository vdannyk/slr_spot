import React, { useState, useEffect, useCallback } from "react";
import { RiCloseLine, RiMenu3Line } from 'react-icons/ri';
import './navbar.css'
import slrspot_logo from '../../assets/slrspot_logo.png';
import { AccessPopup } from '../../containers'
import { SignIn, SignUp } from '../../components'
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { logout } from "../../actions/auth";


const Menu = () => (
  <>
    <p><a href='#home'>Home</a></p>
    <p><a href='#whspot'>What</a></p>
    <p>
      <Link to={'/users'}>
        Users
      </Link>
    </p>
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
  const [isAccessPopup, setIsAccessPopup] = useState(false);
  const { lockScroll, unlockScroll } = useScrollLock();
  const [accessPopup, setAccessPopup] = useState();
  const signIn = <SignIn />
  const signUp = <SignUp />
  const dispatch = useDispatch();

  const { isLoggedIn } = useSelector(state => state.auth);

  const logOut = useCallback(() => {
    dispatch(logout())
    .then(() => {
      window.location.reload();
    });
  }, [dispatch]);

  isAccessPopup ? lockScroll() : unlockScroll();

  return (
    <nav className='slrspot__navbar'>
      <div className='slrspot__navbar-links'>
        <div className='slrspot__navbar-links_logo'>
          <Link to={'/'}>
            <img src={slrspot_logo} />
          </Link>
        </div>
        <div className='slrspot__navbar-links-container'>
          <Menu />
        </div>
      </div>
      <div className='slrspot__navbar-sign'>
        {isLoggedIn 
          ? (            
            <button type='button' onClick={logOut}>
              Logout
            </button>
            )
          : (
            <div className='slrspot__navbar-sign'>
              <p onClick={() => { setIsAccessPopup(true); setAccessPopup(signIn); }}>
                Sign in
              </p>
              <button type='button' onClick={() => { setIsAccessPopup(true); setAccessPopup(signUp); }}>
                Sign up
              </button>
            </div>
            )
        }
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
      <AccessPopup trigger={isAccessPopup} popup={accessPopup} setTrigger={setIsAccessPopup} />
    </nav>
  )
}

export default Navbar