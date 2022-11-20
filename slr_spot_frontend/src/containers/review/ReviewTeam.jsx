import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { AiOutlineClose } from "react-icons/ai";
import './home.css';
import './reviewTeam.css';
import TeamMemberField from '../../components/reviews/TeamMemberField';
import EventBus from '../../common/EventBus';


const ReviewTeam = () => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();
  const [possibleNewMembers, setPossibleNewMembers] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [searchResults, setSearchResults] = React.useState([]);
  const [users, setUsers] = useState([]);

  useEffect(() => {
    axiosInstance.get("/users/emails")
    .then((response) => {
      console.log(response.data);
      setUsers(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  const handleContributorChange = (event) => {
    setSearchTerm(event.target.value);
  }

  const handleUsernameClick = (username) => {
    setPossibleNewMembers(oldArray => [...oldArray, username]);
    setUsers(users.filter(item => item !== username))
  }

  const handleRemoveMember = (member) => {
    setPossibleNewMembers(possibleNewMembers.filter(item => item !== member))
    setUsers(oldArray => [...oldArray, member]);
  }

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
        <div className='slrspot__newReview-members'>
          <div className='slrspot__newReview-members-add'>
              <div className='slrspot__newReview-members-add-field'>
                <input
                  type="text"
                  placeholder="Search by email"
                  value={searchTerm}
                  onChange={handleContributorChange}
                />
                <ul>
                  {searchResults.map(item => (
                    <li onClick={ () => handleUsernameClick(item) }>{ item }</li>
                  ))}
                </ul>
              </div>
              {/* <button type="button" onClick={() => onAddContributor(contributor)}>add</button> */}
              {/* {isAddMemberEmpty && 
                <p className="slrspot__input-error">Invalid username</p>
              } */}
          </div>
          <div className='slrspot__newReview-members-list'>
            {possibleNewMembers.map(item => (
              <TeamMemberField username={item} triggerRemove={handleRemoveMember}/>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

export default ReviewTeam