import React from 'react'
import Footer from '../footer/Footer';
import Header from '../header/Header';
import WhatSLRSpot from '../whatSLRSpot/WhatSLRSpot';
import './homePage.css'

const homePage = () => {
  return (
    <div className="slrspot__homePage">
      <Header />
      {/* <WhatSLRSpot /> */}
      <Footer />
    </div>
  );
}

export default homePage