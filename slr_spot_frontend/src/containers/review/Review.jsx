import React, { useState, useEffect } from 'react'
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { AiFillCaretRight,  AiFillCaretLeft, AiFillCaretDown } from "react-icons/ai";
import './review.css'


const Review = () => {
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const [reviewData, setReviewData] = useState([]);
  const [showMenu, setShowMenu] = useState(true);
  const [showReviewMenu, setShowReviewMenu] = useState(false);
  const [showScreeningMenu, setShowScreeningMenu] = useState(false);

  useEffect(() => {
    console.log(reviewId);
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data);
      console.log(response.data);
    });
  }, []);

  const MenuItem = (props) => {
    return (
      <div className='slrspot__review-menu-item' onClick={() => props.setShowTrigger(!props.showTrigger)}>
        <p>{props.name}</p>
        <span><AiFillCaretDown /></span>
      </div>
    )
  }

  const Menu = () => (
    <div className='slrspot__review-menu'>
      <div className='slrspot__review-menu-options'>
        <MenuItem name='Home'/>
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
        <h1>{reviewData.title}</h1>
      </div>
    </div>
  )
}

export default Review