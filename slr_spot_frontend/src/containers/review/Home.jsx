import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import './home.css'


const Home = () => {
  const [reviewData, setReviewData] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    console.log(reviewId);
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data);
      console.log(response.data);
    });
  }, []);

  return (
    <div className='slrspot__review-home'>
      <div className='slrspot__review-home-title'>
        <h1>{reviewData.title}</h1>
      </div>
      Home
    </div>
  )
}

export default Home