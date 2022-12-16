import React from 'react';
import { AiFillCheckSquare, AiFillCloseSquare } from "react-icons/ai";
import './newElementField.css';

const NewElementField = (props) => {

  return (
    <div className='slrspot__screening-screening-newElement'>
      <input  
        {...props.register(props.fieldName, {
          required: true,
        })}
        name={props.fieldName}
        placeholder='Name'
      />
      <AiFillCheckSquare 
        className='slrspot__screening-newElement-button' 
        color='green'
        onClick={ props.handleSubmit }/>
      <AiFillCloseSquare 
        className='slrspot__screening-newElement-button'
        color='red' 
        onClick={ props.handleClose }/>
    </div>
  )
}

export default NewElementField