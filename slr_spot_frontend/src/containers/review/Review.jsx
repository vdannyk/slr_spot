import React, { useState, useEffect } from 'react';
import { ReviewMenu } from '../../components';
import { useParams } from "react-router-dom";
import axiosInstance from '../../services/api';
import { useSelector } from 'react-redux';
import EventBus from '../../common/EventBus';
import './review.css'


const Review = (props) => {
  const [isPublic, setIsPublic] = useState(false);
  const [userRole, setUserRole] = useState();
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const Component = props.page;
  const state = props.props;

  useEffect(() => {
    axiosInstance.get('/reviews/' + reviewId + '/is-public')
      .then(response => {
        setIsPublic(response.data);
      })
      .catch(error => {
        // console.error(error);
        // setError(error);
      });

      axiosInstance.get('/reviews/' + reviewId + '/members/' + currentUser.id + "/role")
      .then(response => {
        setUserRole(response.data);
      })
      .catch(error => {
        if (error.response && error.response.status === 403) {
          EventBus.dispatch('expirationLogout');
        }
      });
  }, []);

  return (
    <div className='slrspot__review'>
      <ReviewMenu 
        isPublic={isPublic} 
        userRole={userRole} />
      <div className='slrspot__review-content'>
        { React.createElement(Component, { isPublic, userRole, state } ) }
      </div>
    </div>
  )
}

export default Review