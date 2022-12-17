import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from "react-router-dom";
import axiosInstance from "../../../services/api";
import { Table } from "react-bootstrap";
import { CgPen } from "react-icons/cg";
import './reviewHome.css'


const ReviewHome = () => {
  const [reviewData, setReviewData] = useState([]);
  const [researchQuestions, setResearchQuestions] = useState([]);
  const [owner, setOwner] = useState();
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    console.log(reviewId);
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data.review);
      setResearchQuestions(response.data.review.researchQuestions);
      setOwner(response.data.firstName + ' ' + response.data.lastName);
      console.log(response.data)
    });
  }, []);

  return (
    <div className='slrspot__review-home'>
      <div className='slrspot__review-home-title'>
        <h1>
          {reviewData.title}
          <CgPen size={25} onClick={ () => navigate(location.pathname + "/settings") } cursor='pointer' />
        </h1>
      </div>
      <Table>
        <tbody>
          <tr>
            <th>Research Area</th>
            <td>{ reviewData.researchArea }</td>
          </tr>
          <tr>
            <th>Research Questions</th>
            <td>
              { researchQuestions.length > 0 
              ? researchQuestions.map((question) => (
                <li>{ question.name }</li>
                ))
              : '-'  
              }
            </td>
          </tr>
          <tr>
            <th>Description</th>
            <td>{ reviewData.description }</td>
          </tr>
          <tr>
            <th>Owner</th>
            <td>{ owner }</td>
          </tr>
          <tr>
            <th>Team members</th>
            <td>
              <a onClick={ () => navigate("/reviews/" + reviewId + "/team") } className='slrspot__review-home-clickToShow'>
                CLICK TO SHOW
              </a>
            </td>
          </tr>
        </tbody>
      </Table>
    </div>
  )
}

export default ReviewHome