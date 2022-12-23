import React, { useState } from 'react'
import { AiFillCheckCircle } from "react-icons/ai";

const FieldItem = (props) => {
  const [isSelected, setIsSelected] = useState(false);

  const handleClick = () => {
    setIsSelected(!isSelected)
    if (props.selected.includes(props.value)) {
      props.setSelected(props.selected.filter(item => item !== props.value));
    } else {
      props.setSelected(oldArray => [...oldArray, props.value]);
    }
  }

  return (
    <div className='slrspot__extractData-field' onClick={ handleClick }>
      <p>{props.name}{ isSelected ? <AiFillCheckCircle color='green' style={{ marginLeft: '5px' }}/> 
                                  : <AiFillCheckCircle style={{ marginLeft: '5px' }} />} </p>
    </div>
  )
}

export default FieldItem