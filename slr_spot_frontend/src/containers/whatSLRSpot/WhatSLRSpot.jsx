import React from 'react';
import { Feature } from '../../components'
import './whatSLRSpot.css';

const WhatSLRSpot = () => {
    return (
      <div className='slrspot__whatslrspot section__margin' id='whspot'>
        <div className='slrspot__whatslrspot-feature'>
            <Feature title='What is SLR Spot?' text='To cos niesamowitego. adcxzcasdasdasddasdasdasdasdasdasdasdasdczxczxcvzfasdasdasdasdacxzcvzvxcvxcvcbsdsadasdasdasdcvxwasdvfewdsfvewdsfvewdasfesdf'/>
        </div>
        <div className='slrspot__whatslrspot-heading'>
            <h1 className='gradient__text'>True is true but false is not always false</h1>
            <p>Cos za cos</p>
        </div>
        <div className='slrspot__whatslrspot-container'>
            <Feature title='Costam1' text='Cos cos cos3'/>
            <Feature title='Costam2' text='Cos cos cos2'/>
            <Feature title='Costam3' text='Cos cos cos3 '/>
        </div>
      </div>
    )
  }
  
  export default WhatSLRSpot