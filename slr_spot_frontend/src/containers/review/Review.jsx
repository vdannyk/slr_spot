import React, { useState, useEffect } from 'react'
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { AiFillCaretRight,  AiFillCaretLeft } from "react-icons/ai";
import './review.css'


const Review = () => {
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const [reviewData, setReviewData] = useState([]);
  const [showMenu, setShowMenu] = useState(true);


  useEffect(() => {
    console.log(reviewId);
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data);
      console.log(response.data);
    });
  }, []);

  const Menu = () => (
    <div className='slrspot__review-menu'>
      <div className='slrspot__review-menu-options'>
        <p>Jedna opcja</p>
        <p>Druga opcja</p>
        <p>Trzecia opcja</p>
        <p>Czwarta opcja</p>
        <p>Piąta opcja</p>
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