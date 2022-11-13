import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import { AiFillCaretRight,  AiFillCaretLeft, AiFillCaretDown } from "react-icons/ai";
import './review.css'


const Review = (props) => {
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const [showMenu, setShowMenu] = useState(true);
  const [showReviewMenu, setShowReviewMenu] = useState(false);
  const [showScreeningMenu, setShowScreeningMenu] = useState(false);

  const onMenuItemClick = (props) => {
    if (props.setShowTrigger) {
      props.setShowTrigger(!props.showTrigger);
    }
    if (props.redirect) {
      navigate("/reviews/" + reviewId + props.redirect);
    }
  }

  const MenuItem = (props) => {
    return (
      <div className='slrspot__review-menu-item' onClick={() => onMenuItemClick(props)}>
        <p>{props.name}</p>
        <span><AiFillCaretDown /></span>
      </div>
    )
  }

  const Menu = () => (
    <div className='slrspot__review-menu'>
      <div className='slrspot__review-menu-options'>
        <MenuItem name='Home' redirect=''/>
        <MenuItem name='Review' setShowTrigger={setShowReviewMenu} showTrigger={showReviewMenu} />
        { showReviewMenu && (
          <div className='slrspot__review-submenu-options'>
            <li>Settings</li>
            <li>Team</li>
          </div>
        )}
        <MenuItem name='Literature search' />
        <MenuItem name='Screening' setShowTrigger={setShowScreeningMenu} showTrigger={showScreeningMenu} />
        { showScreeningMenu && (
          <div className='slrspot__review-submenu-options'>
            <li>Title & Abstract</li>
            <li>Full text</li>
          </div>
        )}
      </div>
    </div>
  )

  return (
    <div className='slrspot__review'>
      { showMenu && <Menu /> }
      <div className='slrspot__review-menu-show' onClick={() => setShowMenu(!showMenu)}>
        { showMenu ? <AiFillCaretLeft /> : <AiFillCaretRight />}
      </div>
      <div className='slrspot__review-content'>
        {props.page}
      </div>
    </div>
  )
}

export default Review