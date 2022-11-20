import React from 'react'
import { AiOutlineClose } from "react-icons/ai";
import './newReview.css'

const TeamMemberField = ({username, triggerRemove}) => {

  return (
    <div className='slrspot__teamMemberField' onClick={() => triggerRemove(username)}>
      {username}
      <AiOutlineClose className='slrspot__teamMemberField-remove' />
    </div>
  )
}

export default TeamMemberField