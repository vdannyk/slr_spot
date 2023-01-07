import React, { useState, useEffect } from 'react';
import { ReviewMenu } from '../../components';
import { useParams } from "react-router-dom";
import axiosInstance from '../../services/api';
import { useSelector } from 'react-redux';
import EventBus from '../../common/EventBus';
import { useLocation } from 'react-router-dom';
import './review.css'


const Review = (props) => {
  const [isLoading, setIsLoading] = useState(false);
  const [isPublic, setIsPublic] = useState(false);
  const [userRole, setUserRole] = useState();
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const Component = props.page;
  const state = props.props;
  const location = useLocation();
  const [isNotAuthorized, setIsNotAuthorized] = useState(true);

  const checkIfAccessAllowed = () => {
    if (!userRole && !isPublic) {
      setIsNotAuthorized(true);
    } else {
      setIsNotAuthorized(false);
    }
  }

  useEffect(() => {
    setIsLoading(true);
    axiosInstance.get('/reviews/' + reviewId + '/is-public')
      .then(response => {
        setIsPublic(response.data);
        setIsLoading(false);
      })
      .catch(error => {
      });

      axiosInstance.get('/reviews/' + reviewId + '/members/' + currentUser.id + "/role")
      .then(response => {
        setUserRole(response.data);
        setIsLoading(false);
      })
      .catch(error => {
      });
  }, []);

  useEffect(() => {
    checkIfAccessAllowed();
  }, [location, isPublic, userRole]);

  if (!isLoading && isNotAuthorized) {
    return (
      <div className='slrspot__review-unauthorized'>
        <h1>Access not allowed</h1>
      </div>
    )
  }

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