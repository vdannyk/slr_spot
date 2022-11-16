import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { AiOutlineClose } from "react-icons/ai";
import './home.css';


const ReviewTeam = () => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/members")
    .then((response) => {
      setMembers(response.data);
      console.log(response.data);
    });
  }, []);

  const listMembers = members.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td>{item}</td>
        <td><AiOutlineClose style={{color: 'red'}} /></td>
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__review-home'>
      <div className='slrspot__review-home-title'>
        <h1>Your team</h1>
      </div>
      <Table hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Username</th>
            <th>Remove</th>
          </tr>
        </thead>
        {listMembers}
      </Table>
    </div>
  )
}

export default ReviewTeam