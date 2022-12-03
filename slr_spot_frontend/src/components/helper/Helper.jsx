import React, { useState } from 'react';
import { AiOutlineClose } from "react-icons/ai";
import './helper.css';

const Helper = (props) => {
  const [showHelper, setShowHelper] = useState(props.show);

  return showHelper && (
    <div className='slrspot__helper'>
      <AiOutlineClose onClick={ () => setShowHelper(false) } className='slrspot__helper-close'/>
      { props.content }
    </div>
  )
}

export default Helper