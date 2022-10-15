import React from 'react';
import './header.css';

const Header = () => {
  return (
    <div className='slrspot__header gradient__bg'>
      <div className='slrspot__header-content'>
        <h1 className='gradient__text'>Move work forward</h1>
        <p>SLR Spot is the easiest way for teams to manage their work through systematic literature review</p>
        <button>get started</button>
      </div>
    </div>
  )
}

export default Header