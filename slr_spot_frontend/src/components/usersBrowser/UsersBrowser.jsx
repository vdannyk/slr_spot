import React, { useState, useEffect } from 'react'
import axiosInstance from "../../services/api";
import EventBus from '../../common/EventBus';
import { AiOutlineClose } from "react-icons/ai";
import './usersBrowser.css';

const TeamMemberField = ({username, triggerRemove}) => {
  return (
    <div className='slrspot__teamMemberField' onClick={() => triggerRemove(username)}>
      {username}
      <AiOutlineClose className='slrspot__teamMemberField-remove' />
    </div>
  )
}

const UsersBrowser = (props) => {
  const [searchResults, setSearchResults] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [members, setMembers] = useState([]);
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

  useEffect(() => {
    const results = users.filter(person =>
      person.toLowerCase().includes(searchTerm)
    );
    setSearchResults(results);
  }, [searchTerm, users]);

  const handleRemoveMember = (member) => {
    setMembers(members.filter(item => item !== member))
    props.setMembersList(members);
    setUsers(oldArray => [...oldArray, member]);
  }

  const handleContributorChange = (event) => {
    setSearchTerm(event.target.value);
  }

  const handleUsernameClick = (username) => {
    setMembers(oldArray => [...oldArray, username]);
    props.setMembersList(members);
    setUsers(users.filter(item => item !== username))
  }

  return (
    <div className='slrspot__usersBrowser'>
      <div className='slrspot__usersBrowser-search'>
        <div className='slrspot__usersBrowser-search-field'>
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
      </div>
      <div className='slrspot__usersBrowser-selected-list'>
        {members.map(item => (
          <TeamMemberField username={item} triggerRemove={handleRemoveMember}/>
        ))}
      </div>
    </div>
  )
}

export default UsersBrowser