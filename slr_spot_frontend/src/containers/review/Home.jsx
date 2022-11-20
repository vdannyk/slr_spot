import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { Table } from "react-bootstrap";
import './home.css'
import { CgPen } from "react-icons/cg";


const Home = () => {
  const [reviewData, setReviewData] = useState([]);
  const { reviewId } = useParams();
  const navigate = useNavigate();

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
        <h1>{reviewData.title} <CgPen size={25}/></h1>
      </div>
      <Table>
        <tbody>
          <tr>
            <th>Research Area</th>
            <td>{reviewData.researchArea}</td>
          </tr>
          <tr>
            <th>Description</th>
            <td>{reviewData.description}</td>
          </tr>
          <tr>
            <th>Owner</th>
            <td>{reviewData.owner}</td>
          </tr>
          <tr>
            <th>Team members</th>
            <td><a onClick={ () => navigate("/reviews/" + reviewId + "/team") }>CLICK TO SHOW</a></td>
          </tr>
        </tbody>
      </Table>
    </div>
  )
}

export default Home