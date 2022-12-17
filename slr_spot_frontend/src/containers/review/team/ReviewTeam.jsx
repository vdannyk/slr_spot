import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useParams } from "react-router-dom";
import axiosInstance from "../../../services/api";
import { AiOutlineClose, AiFillEdit, AiFillCheckSquare, AiFillCloseSquare } from "react-icons/ai";
import EventBus from '../../../common/EventBus';
import { UsersBrowser, ConfirmationPopup, DropdownSelect } from '../../../components';
import { OWNER, ROLES } from '../../../constants/roles';
import './reviewTeam.css';


const ReviewTeam = () => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [searchedUsers, setSearchedUsers] = useState([]);
  const [isRemoveMemberConfirmation, setIsRemoveMemberConfirmation] = useState(false);
  const [memberNameToRemove, setMemberNameToRemove] = useState('');
  const [memberId, setMemberId] = useState();
  const [selectedChangeRoleMember, setSelectedChangeRoleMember] = useState();
  const [newRole, setNewRole] = useState('');



  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/members/search")
    .then((response) => {
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
    axiosInstance.post("/reviews/" + reviewId + "/members/" + memberId)
    .then(() => {
      setIsRemoveMemberConfirmation(false);
      window.location.reload();
    });
  }

  const onRemoveMemberClick = (triggerPopup, item) => {
    setIsRemoveMemberConfirmation(triggerPopup);
    setMemberNameToRemove(item.firstName + ' ' + item.lastName);
    setMemberId(item.userId);
  }

  const onCancelRemoveMember = () => {
    setIsRemoveMemberConfirmation(false);
    setMemberNameToRemove('');
    setMemberId();
  }

  const onAddMembers = () => {
    const membersToAdd = selectedMembers;
    axiosInstance.post("/reviews/" + reviewId + "/members", {
      membersToAdd
    })
    .then(() => {
      window.location.reload();
    });
  }

  const showRoleDropdown = (member) => {
    setSelectedChangeRoleMember(member);
  }

  const hideRoleDropdown = () => {
    setSelectedChangeRoleMember();
    setNewRole();
  }

  const handleSelect = (event) => {
    setNewRole(event);
  }
  
  const handleChangeRole = () => {
    var userId = selectedChangeRoleMember.userId;
    var role = newRole;
    axiosInstance.put("/users/" + userId + "/role", null, { params: {
      reviewId, role
    }})
    .then(() => {
      window.location.reload()
    });
  }

  const RoleDropdown = (props) => {
    return (
      <div className='slrspot__review-team-roleDropdown'> 
        <DropdownSelect 
            title='Select role' 
            options={ROLES.filter((role) => role !== props.currentRole && role !== OWNER)} 
            onSelect={ handleSelect } 
            value={ newRole } />
        <AiFillCheckSquare 
          className='slrspot__screening-newElement-button' 
          color='green' 
          onClick={ handleChangeRole }/>
        <AiFillCloseSquare 
          className='slrspot__screening-newElement-button' 
          color='red' 
          onClick={ hideRoleDropdown }/>
      </div>
    )
  }

  const MemberRole = ({item}) => {
    return (
      <>
        { selectedChangeRoleMember && selectedChangeRoleMember.userId === item.userId 
          ? 
            <RoleDropdown currentRole={ item.role }/>
          : 
            <>
              {item.role}
              {item.role !== OWNER && 
                <AiFillEdit 
                  onClick={ () => showRoleDropdown(item) } />
              }
            </>
        }
      </>
    )
  }

  const listMembers = members.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td>{item.firstName} {item.lastName}</td>
        <td>
          <MemberRole item={item} />
        </td>
        <td>
          {item.role !== OWNER && 
            <AiOutlineClose 
              onClick={ () => onRemoveMemberClick(true, item) } 
              style={ {color: 'red', cursor: 'pointer'} }
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