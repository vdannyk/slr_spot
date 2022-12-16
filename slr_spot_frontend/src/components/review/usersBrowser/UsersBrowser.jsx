import React, { useState, useEffect } from 'react'
import { AiOutlineClose, AiFillPlusCircle } from "react-icons/ai";
import './usersBrowser.css';

const TeamMemberField = ({username, triggerRemove}) => {
  return (
    <div className='slrspot__usersBrowser-userSelected' onClick={() => triggerRemove(username)}>
      {username}
      <AiOutlineClose className='slrspot__usersBrowser-userSelected-remove' />
    </div>
  )
}

const UserItem = ({username, triggerAdd}) => {
  return (
    <div className='slrspot__usersBrowser-userItem' onClick={() => triggerAdd(username)}>
      {username}
      <AiFillPlusCircle className='slrspot__usersBrowser-userItem-add' />
    </div>
  )
}

const UsersBrowser = (props) => {
  const [searchResults, setSearchResults] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    if (searchTerm) {
      const results = props.searchedUsers.filter(person =>
        person.toLowerCase().includes(searchTerm)
      );
      setSearchResults(results);
    } else {
      setSearchResults([]);
    }
  }, [searchTerm, props.searchedUsers]);

  const handleRemoveMember = (member) => {
    props.setMembersList(props.selectedMembers.filter(item => item !== member));
    props.setSearchedUsers(oldArray => [...oldArray, member]);
  }

  const handleSearch = (event) => {
    setSearchTerm(event.target.value);
  }

  const handleAddMember = (username) => {
    props.setMembersList(oldArray => [...oldArray, username]);
    props.setSearchedUsers(props.searchedUsers.filter(item => item !== username))
  }

  const listMembers = props.selectedMembers.map(item => 
    <TeamMemberField username={item} triggerRemove={handleRemoveMember}/>
  );

  return (
    <div className='slrspot__usersBrowser'>
      <div className='slrspot__usersBrowser-search'>
        <div className='slrspot__usersBrowser-search-field'>
          <input
            type="text"
            placeholder="Search by email"
            value={searchTerm}
            onChange={handleSearch}
          />
          {searchResults.map((item, id) => (
            id < 5 && <UserItem username={item} triggerAdd={handleAddMember}/>
          ))}
        </div>
        {props.selectedMembers.length !== 0 && (
          <div className='slrspot__usersBrowser-search-selected'>
            {listMembers}
          </div>
        )}
      </div>
    </div>
  )
}

export default UsersBrowser