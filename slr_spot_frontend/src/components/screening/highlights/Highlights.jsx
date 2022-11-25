import React from 'react';
import { useNavigate, useLocation } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import './highlights.css';

const Highlights = () => {
  const navigate = useNavigate();
  const location = useLocation();

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
            <td><AiFillMinusCircle color='red' />test</td>
            <td><AiFillMinusCircle color='red' />test</td>
          </div>
          <div className='slrspot__screening-highlights-table-delimiter'>
          </div>
          <div className='slrspot__screening-highlights-table'>
            <th>Exclusion<AiFillPlusCircle color='green'/></th>
            <td>testtesttesttesttesttesttesttesttesttesttesttesttesttestasdasdasdasdasdasdasdasdasdasdasdasdtesttesttesttesttesttesttesttesttessialalalalalaialalalt</td>
            <td>test</td>
            <td>test</td>
            <td>test</td>
            <td>test</td>
          </div>
        </div>
      </div>
      <div className='slrspot__screening-highlights-assignment-container'>
        <h2>Personal</h2>
        <div className='slrspot__screening-highlights-container'>
          <div className='slrspot__screening-highlights-table'>
            <th>Inclusion<AiFillPlusCircle color='green'/></th>
            <td><AiFillMinusCircle color='red' />test</td>
            <td><AiFillMinusCircle color='red' />test</td>
          </div>
          <div className='slrspot__screening-highlights-table-delimiter'>
          </div>
          <div className='slrspot__screening-highlights-table'>
            <th>Exclusion<AiFillPlusCircle color='green'/></th>
            <td>testtesttesttesttesttesttesttesttesttesttesttesttesttestasdasdasdasdasdasdasdasdasdasdasdasdtesttesttesttesttesttesttesttesttessialalalalalaialalalt</td>
            <td>test</td>
            <td>test</td>
            <td>test</td>
            <td>test</td>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Highlights