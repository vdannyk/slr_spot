import React, { useState } from 'react';
import Check from 'react-bootstrap/FormCheck';
import './screeningOptions.css';


const ScreeningOptions = (props) => {
  const [searchValue, setSearchValue] = useState('');

  const handleShowTeamHighlightsChange = () => {
    if (props.showTeamHighlights) {
      props.triggerShowTeamHighlights(false);
    } else {
      props.triggerShowTeamHighlights(true);
      props.triggerShowPersonalHighlights(false);
    }
  }

  const handleShowPersonalHighlightsChange = () => {
    if (props.showPersonalHighlights) {
      props.triggerShowPersonalHighlights(false);
    } else {
      props.triggerShowPersonalHighlights(true);
      props.triggerShowTeamHighlights(false);
    }
  }

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  }

  return (
    <div className='slrspot__screening-options'>
      <div className='slrspot__screening-options-container'>

        <div className='slrspot__screening-options-container-checks'>

          <div className='slrspot__screening-options-check'>
            <Check 
              onChange={ handleShowTeamHighlightsChange } 
              defaultChecked={ props.showTeamHighlights } 
              checked={ props.showTeamHighlights } />
            <label>team highlights</label>
          </div>

          <div className='slrspot__screening-options-check'>
            <Check 
              onChange={ handleShowPersonalHighlightsChange } 
              defaultChecked={ props.showPersonalHighlights } 
              checked={ props.showPersonalHighlights } />
            <label>personal highlights</label>
          </div>

          <div className='slrspot__screening-options-check'>
            <Check 
              onChange={ () => props.triggerShowAbstractsChange(!props.showAbstracts) } 
              defaultChecked={ props.showAbstracts } />
            <label>show abstracts</label>
          </div>

        </div>

        <div className='slrspot__screening-options-search'>
          <label onClick={ () => props.handleSearch(searchValue) }>Search</label>
          <input onChange={ handleSearchChange }/>
        </div>

      </div>
    </div>
  )
}

export default ScreeningOptions