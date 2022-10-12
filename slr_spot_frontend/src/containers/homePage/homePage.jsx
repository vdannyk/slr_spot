import React from 'react'
import Footer from '../footer/Footer';
import Header from '../header/Header';
import WhatSLRSpot from '../whatSLRSpot/WhatSLRSpot';

const homePage = () => {
  return (
    <div className="Home">
      <div className='gradient__bg'>
        <Header />
        {/* <WhatSLRSpot /> */}
      </div>
      <Footer />
    </div>
  );
}

export default homePage