import React, { useState, useCallback } from "react";
import { RiCloseLine, RiMenu3Line } from 'react-icons/ri';
import './navbar.css'
import slrspot_logo from '../../assets/slrspot_logo.png';
import { AccessPopup } from '../../containers'
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { logout } from "../../actions/auth";
import { CgProfile } from "react-icons/cg";

const Menu = () => (
  <>
    <p><a><Link to={'/users'}>about</Link></a></p>
    <p><a><Link to={'/users'}>users</Link></a></p>
    <p><a><Link to={'/users'}>contact</Link></a></p>
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
  const [isSignInPopup, setIsSignInPopup] = useState(false);
  const [isSignUpPopup, setIsSignUpPopup] = useState(false);
  const { lockScroll, unlockScroll } = useScrollLock();
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
            <div className="slrspot__navbar-profile">
              <CgProfile size={45}></CgProfile>     
              <button type='button' onClick={logOut}>
                Logout
              </button>
            </div>
            )
          : (
            <div className='slrspot__navbar-sign'>
              <p onClick={() => { setIsAccessPopup(true); setIsSignInPopup(true); }}>
                Sign in
              </p>
              <button type='button' onClick={() => { setIsAccessPopup(true); setIsSignUpPopup(true); }}>
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
      
      {isAccessPopup && (
        <AccessPopup 
          trigger={isAccessPopup} 
          isSignIn={isSignInPopup}
          isSignUp={isSignUpPopup}
          setTrigger={setIsAccessPopup}
          setIsSignIn={setIsSignInPopup}
          setIsSignUp={setIsSignUpPopup} /> 
      )}
    </nav>
  )
}

export default Navbar