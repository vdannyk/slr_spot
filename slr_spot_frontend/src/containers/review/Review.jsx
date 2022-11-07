import React, { useState, useEffect } from 'react'
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";


const Review = () => {
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const [reviewData, setReviewData] = useState([]);


  useEffect(() => {
    console.log(reviewId);
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data);
      console.log(response.data);
    });
  }, []);

  return (
    <div>{reviewData.title}</div>
  )
}

export default Review