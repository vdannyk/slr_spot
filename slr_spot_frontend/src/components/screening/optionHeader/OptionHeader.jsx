import React from 'react';
import { BiArrowBack } from "react-icons/bi";
import './optionHeader.css';

const OptionHeader = (props) => {

  return (
    <div className='slrspot__screening-optionHeader'>
      <div className='slrspot__screening-optionHeader-backward'>
        <BiArrowBack onClick={props.backward}/>
      </div>
      <h1>{ props.content }</h1>
    </div>
  )
}

export default OptionHeader