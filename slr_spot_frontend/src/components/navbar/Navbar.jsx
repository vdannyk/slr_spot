import React, { useState, useCallback } from "react";
import { RiCloseLine, RiMenu3Line } from 'react-icons/ri';
import slrspot_logo from '../../assets/slrspot_logo.png';
import { AccessPopup } from '../../containers'
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { logout } from "../../actions/auth";
import { CgProfile } from "react-icons/cg";
import { AiFillCaretDown } from "react-icons/ai"
import { useNavigate } from "react-router-dom";
import './navbar.css'


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
  const [openDropdown, setOpenDropdown] = useState(false);
  const { lockScroll, unlockScroll } = useScrollLock();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { user: currentUser } = useSelector((state) => state.auth);

  const { isLoggedIn } = useSelector(state => state.auth);
  // const [isLoggedIn, setisLoggedIn] = useState(true);

  const onLogoClick = () => {
    navigate('/');
    window.location.reload()
  };

  const logOut = useCallback(() => {
    dispatch(logout())
    .then(() => {
      window.location.reload();
    });
  }, [dispatch]);

  isAccessPopup ? lockScroll() : unlockScroll();

  const Menu = () => (
    <>
      <p><Link to={'/about'}>about</Link></p>
      <p><Link to={'/contact'}>contact</Link></p>
      { isLoggedIn && (
        <p><Link to={'/reviews'}>reviews</Link></p>
      )}
    </>
  )

  return (
    <nav className='slrspot__navbar'>
      <div className='slrspot__navbar-links'>
        <div className='slrspot__navbar-links_logo'>
          <img onClick={onLogoClick} src={slrspot_logo}/>
        </div>
        <div className='slrspot__navbar-links-container'>
          <Menu />
        </div>
      </div>
      <div className='slrspot__navbar-sign'>
        {isLoggedIn 
          ? (
            <div className="slrspot__navbar-profile" onClick={() => setOpenDropdown(!openDropdown)}>
              <p>FirstName LastName</p>
              <CgProfile size={45}></CgProfile>
              <AiFillCaretDown size={30}></AiFillCaretDown>
              { openDropdown && (
                <div className="slrspot__navbar-profile-dropdown">
                  <a className="slrspot__navbar-profile-dropdown-item"><Link to={'/profile'}>Settings</Link></a>
                  <a className="slrspot__navbar-profile-dropdown-item" onClick={logOut}><Link>Logout</Link></a>
                </div>
              )}
            </div>
            )
          : (
            <div className='slrspot__navbar-sign'>
              <p onClick={() => { setIsAccessPopup(true); setIsSignInPopup(true); }}>
                <Link>Sign in</Link>
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