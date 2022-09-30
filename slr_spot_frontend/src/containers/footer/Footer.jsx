import React from 'react'
import slrspot_logo from '../../assets/slrspot_logo.png';
import './footer.css';

const Footer = () => {
  return (
    <div className="slrspot__footer section__padding">
      <div className="slrspot__footer-heading">
        <h1 className="gradient__text">Do you want to step in to the future before others</h1>
      </div>

      <div className="slrspot__footer-btn">
        <p>Request Early Access</p>
      </div>

      <div className="slrspot__footer-links">
        <div className="slrspot__footer-links_logo">
          <img src={slrspot_logo} alt="slrspot_logo" />
          <p>Test, <br /> All Rights Reserved</p>
        </div>
        <div className="slrspot__footer-links_div">
          <h4>Links</h4>
          <p>Overons</p>
          <p>Social Media</p>
          <p>Counters</p>
          <p>Contact</p>
        </div>
        <div className="slrspot__footer-links_div">
          <h4>Company</h4>
          <p>Terms & Conditions </p>
          <p>Privacy Policy</p>
          <p>Contact</p>
        </div>
        <div className="slrspot__footer-links_div">
          <h4>Test</h4>
          <p>test</p>
          <p>222</p>
          <p>test</p>
        </div>
      </div>

      <div className="slrspot__footer-copyright">
        <p>@2022 SLR SPOT. All rights reserved.</p>
      </div>
    </div>
  )
}

export default Footer