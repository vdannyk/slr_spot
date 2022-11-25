import React from 'react';
import { useNavigate, useLocation } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import './tags.css';

const Tags = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className='slrspot__screening-tags'>
      <OptionHeader 
        content='Manage tags'
        backward={ () => navigate(location.pathname.replace('/tags', '')) }/>
      <div className='slrspot__screening-tags-container'>
        <div className='slrspot__screening-tags-table'>
          <th>Tags<AiFillPlusCircle color='green'/></th>
          <td><AiFillMinusCircle color='red' />test</td>
          <td><AiFillMinusCircle color='red' />test</td>
        </div>
      </div>
    </div>
  )
}

export default Tags