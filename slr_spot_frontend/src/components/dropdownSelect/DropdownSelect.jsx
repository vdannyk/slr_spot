import React from 'react';
import { Dropdown } from 'react-bootstrap';
import './dropdownSelect.css'

const DropdownSelect = (props) => {
  const options = props.options.map((item, idx) => {
    return (
      <Dropdown.Item eventKey={ item } key={ idx }>
        { item }
      </Dropdown.Item>
    )
  })

  function getSelection(value) {
    const selected = props.options.filter((item, idx) => {
      return item === value;
    })

    return selected.length > 0 ? (
      selected 
    ) : (
      props.title
    );
  }

  return (
    <Dropdown onSelect={ props.onSelect }>
      <Dropdown.Toggle id='slrspot__dropdown'>
        { getSelection(props.value) }
      </Dropdown.Toggle>
      <Dropdown.Menu>{ options }</Dropdown.Menu>
    </Dropdown>
  )
}

export default DropdownSelect  
