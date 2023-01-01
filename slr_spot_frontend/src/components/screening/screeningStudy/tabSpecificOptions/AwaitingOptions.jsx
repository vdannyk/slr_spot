import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../services/api';
import { useSelector } from 'react-redux';

const AwaitingOptions = (props) => {
  const [showOptions, setShowOptions] = useState(false);
  const [currentVote, setCurrentVote] = useState('');
  const { user: currentUser } = useSelector((state) => state.auth);

  useEffect(() => {
    var userId = currentUser.id;
    axiosInstance.get("/studies/" + props.study.id + "/screening_decision", { params: {
      userId
    }})
    .then((response) => {
      setCurrentVote(response.data);
    })
  }, []);

  return (
    <div className='slrspot__screeningStudy-awaiting'>
      <div className='slrspot__screeningStudy-votes'>
        <p>current vote: <span><b>{currentVote}</b></span></p>
      </div>
      { showOptions 
      ? <div className='slrspot__screeningStudy-awaiting-decision'>
          <button 
            className='slrspot__screeningStudy-decision-button'
            onClick={ () => props.handleVote('EXCLUDE')}>
              exclude
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => props.handleVote('UNCLEAR')}>
              unclear
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => props.handleVote('INCLUDE')}>
              include
          </button>
        </div>
      : <button 
          className='slrspot__screeningStudy-decision-button' 
          onClick={() => setShowOptions(true)}>
            change decision
        </button>
      }
    </div>
  )
}

export default AwaitingOptions