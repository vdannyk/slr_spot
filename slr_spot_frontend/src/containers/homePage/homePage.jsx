import React from 'react'
import './homePage.css'

const homePage = () => {
  return (
    <div className="slrspot__homePage">
      <div className='slrspot__home-container gradient__bg'>
        <div className='slrspot__home-header'>
          <h1 className='gradient__text'>Move work forward</h1>
          <p>SLR Spot is the easiest way for teams to manage their work through systematic literature review</p>
          <button>get started</button>
        </div>
      </div>
    </div>
  );
}

export default homePage