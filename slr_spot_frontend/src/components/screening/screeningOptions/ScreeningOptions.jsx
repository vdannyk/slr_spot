import React from 'react';
import Check from 'react-bootstrap/FormCheck';
import './screeningOptions.css';


const ScreeningOptions = (props) => {

  return (
    <div className='slrspot__screening-options'>
      <div className='slrspot__screening-options-container'>
        <div className='slrspot__screening-options-container-checks'>
          <div className='slrspot__screening-options-check'>
            <Check />
            <label>show highlighst</label>
          </div>
          {/* <a>switch to review mode</a> */}
          <div className='slrspot__screening-options-check'>
            <Check onChange={ () => props.triggerChange(!props.showAbstracts) } defaultChecked={ props.showAbstracts }/>
            <label>show abstracts</label>
          </div>
        </div>
        <div className='slrspot__screening-options-search'>
          <label>Search</label>
          <input></input>
        </div>
      </div>
    </div>
  )
}

export default ScreeningOptions