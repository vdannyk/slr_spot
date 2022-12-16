import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../../services/api";
import { AiOutlineClose } from "react-icons/ai";
import './reviewTeam.css';
import EventBus from '../../../common/EventBus';
import { UsersBrowser, ConfirmationPopup } from '../../../components';


const ReviewTeam = () => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [searchedUsers, setSearchedUsers] = useState([]);
  const [isRemoveMemberConfirmation, setIsRemoveMemberConfirmation] = useState(false);
  const [memberNameToRemove, setMemberNameToRemove] = useState('');
  const [memberId, setMemberId] = useState();
  

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/members/search")
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
    axiosInstance.get("/users", { params: {
      reviewId
    }})
    .then((response) => {
      setMembers(response.data);
    });
  }, []);

  const confirmRemoveMember = () => {
    console.log(memberId);
    axiosInstance.post("/reviews/" + reviewId + "/members/" + memberId + "/remove")
    .then(() => {
      setIsRemoveMemberConfirmation(false);
      window.location.reload();
    });
  }

  const onRemoveMemberClick = (triggerPopup, item) => {
    setIsRemoveMemberConfirmation(triggerPopup);
    setMemberNameToRemove(item.firstName + ' ' + item.lastName);
    setMemberId(item.memberId);
  }

  const onCancelRemoveMember = () => {
    setIsRemoveMemberConfirmation(false);
    setMemberNameToRemove('');
    setMemberId();
  }

  const onAddMembers = () => {
    const membersToAdd = selectedMembers;
    axiosInstance.post("/reviews/" + reviewId + "/members/add", {
      membersToAdd
    })
    .then(() => {
      console.log('Adding succesfull');
      window.location.reload();
    });
  }

  const listMembers = members.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td>{item.firstName} {item.lastName}</td>
        <td>{item.role}</td>
        <td>
          {item.role !== 'OWNER' && 
            <AiOutlineClose 
              onClick={() => onRemoveMemberClick(true, item)} 
              style={{color: 'red', cursor: 'pointer'}}
            />
          }
        </td>
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
        { isRemoveMemberConfirmation && 
        <ConfirmationPopup 
          title="remove member"
          message={'Do you want to remove ' + memberNameToRemove + ' from your team?'}
          triggerConfirm={confirmRemoveMember}
          triggerCancel={onCancelRemoveMember}
        /> 
        }
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
          <button onClick={onAddMembers}>Add</button>
        </div>
      </div>
    </div>
  )
}

export default ReviewTeam