import React from 'react';
import { useNavigate, useLocation } from "react-router-dom";
import './screeningSettings.css';


const ScreeningSettings = () => {
  const navigate = useNavigate();
  const location = useLocation();
  
  return (
    <div className='slrspot__review-screening'>
      <div className='slrspot__review-screening-menu'>
        <div className='slrspot__review-header'>
          <h1>Screening settings</h1>
        </div >
        <div className='slrspot__review-screening-menu-options'>
          <h2 onClick={() => navigate(location.pathname + '/criteria')}><li>Inclusion and exclusion criteria</li></h2>
          <h2 onClick={() => navigate(location.pathname + '/tags')}><li>Tags</li></h2>
          <h2 onClick={() => navigate(location.pathname + '/highlights')}><li>Highlights</li></h2>
        </div>
      </div>
    </div>
  )
}

export default ScreeningSettings