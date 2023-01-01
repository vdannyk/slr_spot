import React from 'react';
import './pageChanger.css';

const PageChanger = ({defaultSelected, options, changePageSize}) => {

  const listMembers = options.map((item, idx) => {
    return item === defaultSelected 
      ? <span key={idx} className='slrspot__page-changer-option' id='selected'>{item}</span>
      : <span key={idx} onClick={ () => handleClick(item) }className='slrspot__page-changer-option'>{item}</span>
  });

  const handleClick = (value) => {
    changePageSize(value)
    listMembers = options.map((item, idx) => {
      return item === value 
        ? <span key={idx} className='slrspot__page-changer-option' id='selected'>{item}</span>
        : <span key={idx} onClick={ () => handleClick(item) }className='slrspot__page-changer-option'>{item}</span>
    });
  }

  return (
    <div>
      {listMembers}
    </div>
  )
}

export default PageChanger