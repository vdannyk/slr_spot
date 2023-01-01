import React, { useState, useEffect } from 'react';
import Check from 'react-bootstrap/FormCheck';
import { AUTHORS_SEARCH, AUTHORS_YEAR_SEARCH, TITLE_AUTHORS_SEARCH, TITLE_AUTHORS_YEAR_SEARCH, TITLE_SEARCH, TITLE_YEAR_SEARCH, YEAR_SEARCH, EVERYTHING_SEARCH } from '../../../constants/searchTypes';
import './screeningOptions.css';


const ScreeningOptions = (props) => {
  const [searchValue, setSearchValue] = useState('');

  const [searchPlaceholder, setSearchPlaceholder] = useState("Everything");
  const [titleCheck, setTitleCheck] = useState(false);
  const [authorsCheck, setAuthorsCheck] = useState(false);
  const [yearCheck, setYearCheck] = useState(false);
  const [everythingCheck, setEverythingCheck] = useState(true);

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

  useEffect(() => {
    if (everythingCheck) {
      props.setSearchType(EVERYTHING_SEARCH);
      setSearchPlaceholder('Everything...');
    }
    else if (titleCheck && authorsCheck && yearCheck) {
      props.setSearchType(TITLE_AUTHORS_YEAR_SEARCH);
      setSearchPlaceholder('Title, Authors, Year...')
    }
    else if (titleCheck && authorsCheck) {
      props.setSearchType(TITLE_AUTHORS_SEARCH);
      setSearchPlaceholder('Title, Authors...')
    }
    else if (titleCheck && yearCheck) {
      props.setSearchType(TITLE_YEAR_SEARCH);
      setSearchPlaceholder('Title, Year...')
    }
    else if (authorsCheck && yearCheck) {
      props.setSearchType(AUTHORS_YEAR_SEARCH);
      setSearchPlaceholder('Authors, Year...')
    }
    else if (titleCheck) {
      props.setSearchType(TITLE_SEARCH);
      setSearchPlaceholder('Title...')
    }
    else if (authorsCheck) {
      props.setSearchType(AUTHORS_SEARCH);
      setSearchPlaceholder('Authors...')
    } else if (yearCheck) {
      props.setSearchType(YEAR_SEARCH);
      setSearchPlaceholder('Year...')
    } else {
      props.setSearchType(EVERYTHING_SEARCH);
      setSearchPlaceholder('Everything...')
      setEverythingCheck(true);
    }
  }, [titleCheck, authorsCheck, yearCheck, everythingCheck]);

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  }

  const handleTitleCheck = () => {
    setTitleCheck(!titleCheck);
    if (everythingCheck) {
      setEverythingCheck(false);
    }
  }

  const handleAuthorsCheck = () => {
    setAuthorsCheck(!authorsCheck);
    if (everythingCheck) {
      setEverythingCheck(false);
    }
  }

  const handleYearCheck = () => {
    setYearCheck(!yearCheck);
    if (everythingCheck) {
      setEverythingCheck(false);
    }
  }

  const handleEverythingCheck = () => {
    if (everythingCheck) {
      setEverythingCheck(false);
    } else {
      setEverythingCheck(true);
      setTitleCheck(false);
      setAuthorsCheck(false);
      setYearCheck(false);
    }
  }

  return (
    <div className='slrspot__screening-options'>
      <div className='slrspot__screening-options-container'>

        <div className='slrspot__screening-options-search'>

          <div className='slrspot__screening-options-search-term'>
            <label onClick={ () => props.handleSearch(searchValue) }>Search</label>
            <input onChange={ handleSearchChange } placeholder={searchPlaceholder} />
          </div>

          <div className='slrspot__screening-options-search-checks'>
            
            <div className='slrspot__screening-options-search-checks-container'>
              <Check 
                onChange={ handleTitleCheck } 
                checked={ titleCheck } />
              <span>Title</span>
              <Check 
                onChange={ handleAuthorsCheck } 
                checked={ authorsCheck } 
                style={{ marginLeft: '40px' }}/>
              <span>Authors</span>
            </div>

            <div className='slrspot__screening-options-search-checks-container'>
              <Check 
                onChange={ handleYearCheck } 
                checked={ yearCheck } />
              <span>Year</span>
              <Check 
                onChange={ handleEverythingCheck } 
                checked={ everythingCheck } 
                style={{ marginLeft: '40px' }}/>
              <span>Everything</span>
            </div>

          </div>
        </div>

        <div className='slrspot__screening-options-container-checks'>

          <div className='slrspot__screening-options-check'>
            <Check 
              onChange={ () => props.triggerShowAbstractsChange(!props.showAbstracts) } 
              defaultChecked={ props.showAbstracts } />
            <label>show abstracts</label>
          </div>

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

        </div>

      </div>
    </div>
  )
}

export default ScreeningOptions