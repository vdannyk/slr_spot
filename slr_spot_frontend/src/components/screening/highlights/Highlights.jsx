import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import EventBus from '../../../common/EventBus';
import axiosInstance from '../../../services/api';
import './highlights.css';

const Highlights = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [reviewKeywords, setReviewKeywords] = useState([]);
  const [userKeywords, setUserKeywords] = useState([]);
  const { user: currentUser } = useSelector((state) => state.auth);
  
  const listReviewKeywords = reviewKeywords.map((keyword) => 
    <td key={keyword.name}><AiFillMinusCircle color='red' />{keyword.name}</td>
  );

  const listUserKeywords = userKeywords.map((keyword) => 
  <td key={keyword.name}><AiFillMinusCircle color='red' />{keyword.name}</td>
);

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/keywords")
    .then((response) => {
      setReviewKeywords(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  useEffect(() => {
    axiosInstance.get("/users/" + currentUser.id + "/keywords")
    .then((response) => {
      setUserKeywords(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  return (
    <div className='slrspot__screening-highlights'>
      <OptionHeader 
        content='Manage keywords'
        backward={ () => navigate(location.pathname.replace('/highlights', '')) }/>
      <div className='slrspot__screening-highlights-assignment-container'>
        <h2>Shared with team</h2>
        <div className='slrspot__screening-highlights-container'>
          <div className='slrspot__screening-highlights-table'>
            <th>Inclusion<AiFillPlusCircle color='green'/></th>
            { listReviewKeywords }
          </div>
          <div className='slrspot__screening-highlights-table-delimiter'>
          </div>
          <div className='slrspot__screening-highlights-table'>
            <th>Exclusion<AiFillPlusCircle color='green'/></th>
            { listReviewKeywords }
          </div>
        </div>
      </div>
      <div className='slrspot__screening-highlights-assignment-container'>
        <h2>Personal</h2>
        <div className='slrspot__screening-highlights-container'>
          <div className='slrspot__screening-highlights-table'>
            <th>Inclusion<AiFillPlusCircle color='green'/></th>
            { listUserKeywords }
          </div>
          <div className='slrspot__screening-highlights-table-delimiter'>
          </div>
          <div className='slrspot__screening-highlights-table'>
            <th>Exclusion<AiFillPlusCircle color='green'/></th>
            { listUserKeywords }
          </div>
        </div>
      </div>
    </div>
  )
}

export default Highlights