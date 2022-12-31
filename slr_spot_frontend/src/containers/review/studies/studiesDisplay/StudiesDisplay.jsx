import React, { useState } from 'react';
import Helper from '../../../../components/helper/Helper';
import Check from 'react-bootstrap/FormCheck';
import './studiesDisplay.css';
import StudiesView from './studiesView/StudiesView';
import FoldersView from './foldersView/FoldersView';
import { OWNER, COOWNER, MEMBER } from '../../../../constants/roles';


const StudiesDisplay = (props) => {
  const [isStudiesView, setIsStudiesView] = useState(true);
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  return (
    <div className='slrspot__review-studiesDisplay'>
      <div className='slrspot__review-studiesDisplay-header'>
        <h1>Imported studies</h1>
        { isStudiesView 
          ? <span onClick={ () => setIsStudiesView(false) }>Switch to folders view</span> 
          : <span onClick={ () => setIsStudiesView(true) }>Switch to studies view</span> }
      </div>
      <div className='slrspot__review-studiesDisplay-container'>
          { isStudiesView 
            ? <StudiesView allowChanges={allowChanges}/> 
            : <FoldersView allowChanges={allowChanges}/>}
      </div>
    </div>
  )
}

export default StudiesDisplay