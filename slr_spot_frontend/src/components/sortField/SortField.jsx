import React, { useState } from 'react';
import './sortField.css';

const SortField = (props) => {

  if (props.selected) {
    return (
      <div className='slrspot__sortField' id='selected'>
        <label>{props.name}</label>
      </div>
    )
  }

  else {
    return (
      <div className='slrspot__sortField' onClick={ props.handleSelect }>
        <label>{props.name}</label>
      </div>
    )
  }
}

export default SortField