import React from 'react';
import { useNavigate, useLocation } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import './criteria.css';

const Criteria = () => {
  const navigate = useNavigate();
  const location = useLocation();

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
          <td>testtesttesttesttesttesttesttesttesttesttesttesttesttestasdasdasdasdasdasdasdasdasdasdasdasdtesttesttesttesttesttesttesttesttessialalalalalaialalalt</td>
          <td>test</td>
          <td>test</td>
          <td>test</td>
          <td>test</td>
        </div>
      </div>
    </div>
  )
}

export default Criteria