import React from 'react';
import './criteriaButton.css';

const CriteriaButton = (props) => {
  return (
    <div className='slrspot__criteriaButton'>
      <div className='slrspot__criteriaButton-container' onClick={ props.triggerCriteria }>
        CRITERIA
      </div>
    </div>
  )
}

export default CriteriaButton