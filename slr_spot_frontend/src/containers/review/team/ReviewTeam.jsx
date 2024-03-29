import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { useParams } from "react-router-dom";
import axiosInstance from "../../../services/api";
import { AiOutlineClose, AiFillEdit, AiFillCheckSquare, AiFillCloseSquare } from "react-icons/ai";
import { UsersBrowser, ConfirmationPopup, DropdownSelect } from '../../../components';
import { OWNER, ROLES, COOWNER } from '../../../constants/roles';
import ReactPaginate from 'react-paginate';
import './reviewTeam.css';


const ReviewTeam = (props) => {
  const [members, setMembers] = useState([]);
  const { reviewId } = useParams();
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [searchedUsers, setSearchedUsers] = useState([]);
  const [isRemoveMemberConfirmation, setIsRemoveMemberConfirmation] = useState(false);
  const [memberNameToRemove, setMemberNameToRemove] = useState('');
  const [memberId, setMemberId] = useState();
  const [selectedChangeRoleMember, setSelectedChangeRoleMember] = useState();
  const [newRole, setNewRole] = useState('');
  var allowChanges = props.userRole && [OWNER, COOWNER].includes(props.userRole);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageCount, setPageCount] = useState(0);

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/members/search")
    .then((response) => {
      setSearchedUsers(response.data);
    })
    .catch((error) => {
    });
  }, []);

  useEffect(() => {
    var page = currentPage;
    axiosInstance.get("/users", { params: {
      reviewId, page
    }})
    .then((response) => {
      setMembers(response.data.content);
      setPageCount(response.data.totalPages);
    });
  }, [currentPage]);

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    setCurrentPage(page);
  }

  const confirmRemoveMember = () => {
    axiosInstance.post("/reviews/" + reviewId + "/members/" + memberId)
    .then(() => {
      setIsRemoveMemberConfirmation(false);
      window.location.reload();
    });
  }

  const onRemoveMemberClick = (triggerPopup, item) => {
    setIsRemoveMemberConfirmation(triggerPopup);
    setMemberNameToRemove(item.firstName + ' ' + item.lastName);
    setMemberId(item.id);
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
    var userId = selectedChangeRoleMember.user.id;
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
        { selectedChangeRoleMember && selectedChangeRoleMember.user.id === item.user.id 
          ? 
            <RoleDropdown currentRole={ item.role.name }/>
          : 
            <>
              {item.role.name}
              {item.role.name !== OWNER && allowChanges &&
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
        <td>{item.user.firstName} {item.user.lastName}</td>
        <td>
          <MemberRole item={ item } />
        </td>
        { allowChanges && 
          <td>
            {item.role.name !== OWNER && 
              <AiOutlineClose 
                onClick={ () => onRemoveMemberClick(true, item.user) } 
                style={ {color: 'red', cursor: 'pointer'} }
              />
            }
          </td>
        }
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
                { allowChanges && <th>Remove</th> }
              </tr>
            </thead>
            { listMembers }
          </Table>

          { members.length > 0 && pageCount > 1 &&
            <ReactPaginate
              pageCount={pageCount}
              pageRangeDisplayed={5}
              marginPagesDisplayed={2}
              onPageChange={handlePageChange}
              forcePage={currentPage}
              containerClassName="slrspot__pagination"
              activeClassName="slrspot__pagination-active"
            />
          }
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

      { allowChanges && 
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
      }
    </div>
  )
}

export default ReviewTeam