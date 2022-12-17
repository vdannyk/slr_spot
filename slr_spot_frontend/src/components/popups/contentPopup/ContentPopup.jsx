import React from 'react';
import './contentPopup.css';
import { AiOutlineClose } from "react-icons/ai";

const ContentPopup = (props) => {
  return (
    <div className='slrspot__contentPopup'>
      <div className='slrspot__contentPopup-container'>
        <div className="slrspot__contentPopup-closeBtn_container">
          <AiOutlineClose size={25} onClick={ props.triggerExit } />
        </div>
        <div className='slrspot__contentPopup-container-content'>
          { props.content }
        </div>
      </div>
    </div>
  )
}

export default ContentPopup