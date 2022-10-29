import React from 'react'
import slrspot_logo from '../../assets/slrspot_logo.png';
import './footer.css';

const Footer = () => {
  return (
    <div className="slrspot__footer">
      <div className="slrspot__footer-container">
        <div className="slrspot__footer-copyright">
          <div className="slrspot__footer-logo">
            <img src={slrspot_logo} alt="slrspot_logo" />
          </div>
          <p>@2022 SLR SPOT. All rights reserved.</p>
        </div>
      </div>
    </div>
  )
}

export default Footer