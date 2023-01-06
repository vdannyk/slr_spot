import React, { useState } from 'react';
import SortField from './SortField';
import Check from 'react-bootstrap/FormCheck'
import { AiOutlineSortAscending, AiOutlineSortDescending } from "react-icons/ai";
import { ASCENDING, DESCENDING } from './sortDirections';
import { TITLE, AUTHORS, YEAR } from './sortProperties';
import './sortOption.css';

const SortOption = (props) => {
  const [title, setTitle] = useState(true);
  const [authors, setAuthors] = useState(false);
  const [year, setYear] = useState(false);
  const [isAscending, setIsAscending] = useState(true);
  const [isDescending, setIsDescending] = useState(false);

  const handleTitle = () => {
    setTitle(true);
    setAuthors(false);
    setYear(false);
    props.setProperty(TITLE);
  }

  const handleAuthors = () => {
    setTitle(false);
    setAuthors(true);
    setYear(false);
    props.setProperty(AUTHORS);
  }

  const handleYear = () => {
    setTitle(false);
    setAuthors(false);
    setYear(true);
    props.setProperty(YEAR);
  }

  const handleAscending = () => {
    setIsAscending(true);
    setIsDescending(false);
    props.setDirection(ASCENDING);
  }

  const handleDescending = () => {
    setIsDescending(true);
    setIsAscending(false);
    props.setDirection(DESCENDING);
  }

  return (
    <div className='slrspot__screening-sort'>
      <label>Sort by:</label>
      <div className='slrspot__screening-sort-options'>
        <SortField name='title' selected={title} handleSelect={ handleTitle } />
        <SortField name='authors' selected={authors} handleSelect={ handleAuthors } />
        <SortField name='year' selected={year} handleSelect={ handleYear } />
      </div>
      <div className='slrspot__screening-options-check'>
        <Check
          type="radio"
          onChange={ handleAscending } 
          checked={ isAscending } />
        <span><AiOutlineSortAscending size={24}/></span>
        <Check
          type="radio"
          onChange={ handleDescending } 
          checked={ isDescending } />
        <span><AiOutlineSortDescending size={24}/></span>
      </div>
    </div>
  )
}

export default SortOption