import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../services/api";
import { AiOutlineClose } from "react-icons/ai";
import './reviewTeam.css';
import EventBus from '../../../common/EventBus';
import { UsersBrowser } from '../../../components';
// import TeamMemberField from '../../components/newReview/TeamMemberField';


const ReviewTeam = () => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [searchedUsers, setSearchedUsers] = useState([]);

  useEffect(() => {
    axiosInstance.get("/users/emails")
    .then((response) => {
      console.log(response.data);
      setSearchedUsers(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

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
        <td>{item.role !== 'OWNER' && <AiOutlineClose style={{color: 'red'}} />}</td>
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
          <h1>Add new member</h1>
        </div>
        <div className='slrspot__review-team-newMember-search'>
          <UsersBrowser
            searchedUsers={searchedUsers}
            setSearchedUsers={setSearchedUsers} 
            selectedMembers={selectedMembers} 
            setMembersList={setSelectedMembers} 
          />
        </div>
      </div>
    </div>
  )
}

export default ReviewTeam