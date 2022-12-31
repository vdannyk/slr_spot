import React, { useState } from 'react';

const ConflictedOptions = (props) => {
  // to do -> check role in useState
  const [showOptions, setShowOptions] = useState(false);

  return (
    <div className='slrspot__screeningStudy-decision'>
      { showOptions 
      ? <>
          <button 
            className='slrspot__screeningStudy-decision-button'
            onClick={ () => props.handleVote('EXCLUDE')}>
              exclude
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => props.handleVote('INCLUDE')}>
              include
          </button>
        </>
      : <button 
          className='slrspot__screeningStudy-decision-button' 
          onClick={() => setShowOptions(true)}>
            change decision
        </button>
      }
    </div>
  )
}

export default ConflictedOptions