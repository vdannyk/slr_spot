import React from 'react'
import slrspot_logo from '../../assets/slrspot_logo.png';
import './footer.css';

const Footer = () => {
  return (
    <div className="slrspot__footer">
      <footer>
        <div className="slrspot__footer-container">
            <div className="slrspot__footer-info">
              <div className='slrspot__footer-infoField'>
                <h3>About</h3>
                <ul>
                    <li><a href="#">Application</a></li>
                    <li><a href="#">Author</a></li>
                </ul>
              </div>
              <div className='slrspot__footer-infoField'>
                <h3>SLR Spot</h3>
                <p>Just to make your reviews easier.</p>
              </div>
            </div>
            <div className='slrspot__footer-copyright'>
              <img src={slrspot_logo} alt="slrspot_logo" />
              <p>SLR Spot ©2022</p>
            </div>
        </div>
      </footer>
    </div>
  )
}

export default Footer