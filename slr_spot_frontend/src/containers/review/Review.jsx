import React from 'react';
import { ReviewMenu } from '../../components';
import './review.css'


const Review = (props) => {

  return (
    <div className='slrspot__review'>
      <ReviewMenu />
      <div className='slrspot__review-content'>
        {props.page}
      </div>
    </div>
  )
}

export default Review