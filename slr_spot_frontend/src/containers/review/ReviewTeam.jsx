import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { AiOutlineClose } from "react-icons/ai";
import './home.css';
import './reviewTeam.css';


const ReviewTeam = () => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/members")
    .then((response) => {
      setMembers(response.data);
    });
  }, []);

  const listMembers = members.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td>{item.firstName} {item.lastName}</td>
        <td>{item.role}</td>
        <td><AiOutlineClose style={{color: 'red'}} /></td>
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__review-team'>
      <div className='slrspot__review-team-members'>
        <div className='slrspot__review-team-header'>
          <h1>Your team</h1>
        </div>
        <div className='slrspot__review-team-content'>
          <Table hover>
            <thead>
              <tr>
                <th>#</th>
                <th>Name</th>
                <th>Role</th>
                <th>Remove</th>
              </tr>
            </thead>
            { listMembers }
          </Table>
        </div>
      </div>
      <div className='slrspot__review-team-newMember'>
        <div className='slrspot__review-team-header'>
          <h1>Add new teammate</h1>
        </div>
      </div>
    </div>
  )
}

export default ReviewTeam