import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import EventBus from '../../../common/EventBus';
import axiosInstance from '../../../services/api';
import './criteria.css';

const Criteria = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [criteria, setCriteria] = useState([]);
  
  const listCriteria = criteria.map((tag) => 
    <td key={tag.name}><AiFillMinusCircle color='red' />{tag.name}</td>
  );

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/criteria")
    .then((response) => {
      setCriteria(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  return (
    <div className='slrspot__screening-criteria'>
      <OptionHeader 
        content='Set inclusion and exclusion criteria'
        backward={ () => navigate(location.pathname.replace('/criteria', '')) }/>
      <div className='slrspot__screening-criteria-container'>
        <div className='slrspot__screening-criteria-table'>
          <th>Inclusion<AiFillPlusCircle color='green'/></th>
          <td><AiFillMinusCircle color='red' />test</td>
          <td><AiFillMinusCircle color='red' />test</td>
        </div>
        <div className='slrspot__screening-criteria-table-delimiter'>
        </div>
        <div className='slrspot__screening-criteria-table'>
          <th>Exclusion<AiFillPlusCircle color='green'/></th>
          { listCriteria }
        </div>
      </div>
    </div>
  )
}

export default Criteria