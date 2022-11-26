import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import EventBus from '../../../common/EventBus';
import axiosInstance from '../../../services/api';
import './tags.css';

const Tags = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [tags, setTags] = useState([]);
  
  const listTags = tags.map((tag) => 
    <td key={tag.name}><AiFillMinusCircle color='red' />{tag.name}</td>
  );

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/tags")
    .then((response) => {
      setTags(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  return (
    <div className='slrspot__screening-tags'>
      <OptionHeader 
        content='Manage tags'
        backward={ () => navigate(location.pathname.replace('/tags', '')) }/>
      <div className='slrspot__screening-tags-container'>
        <div className='slrspot__screening-tags-table'>
          <th>Tags<AiFillPlusCircle color='green'/></th>
          { listTags }
        </div>
      </div>
    </div>
  )
}

export default Tags