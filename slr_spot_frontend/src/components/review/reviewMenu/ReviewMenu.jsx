import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from "react-router-dom";
import { AiFillCaretRight,  AiFillCaretLeft, AiFillCaretDown } from "react-icons/ai";
import { OWNER, COOWNER } from '../../../constants/roles';
import './reviewMenu.css';


const ReviewMenu = (props) => {
  const [isHidden, setIsHidden] = useState(false);
  const [showReviewMenu, setShowReviewMenu] = useState(false);
  const [showScreeningMenu, setShowScreeningMenu] = useState(false);
  const [showStudiesMenu, setShowStudiesMenu] = useState(false);
  const { reviewId } = useParams();
  const navigate = useNavigate();
  var allowChanges = props.userRole && [OWNER, COOWNER].includes(props.userRole);

  console.log(props.userRole)

  const onItemClick = (props) => {
    if (props.setShowTrigger) {
      props.setShowTrigger(!props.showTrigger);
    }
    if (props.redirect !== undefined) {
      navigate("/reviews/" + reviewId + props.redirect)
    }
  }

  const MenuItem = (props) => {
    return (
      <div className='slrspot__review-menu-item' onClick={ () => onItemClick(props) }>
        <p>{props.name}</p>
        <span><AiFillCaretDown /></span>
      </div>
    )
  }

  const SubMenuItem = (props) => {
    return (
      <>
        <li onClick={ () => onItemClick(props) }>
          {props.name}
        </li>
      </>
    )
  }

  const ShowMenuButton = () => (
    <div className='slrspot__review-menu-show' onClick={() => setIsHidden(!isHidden)}>
      { isHidden ? <AiFillCaretRight /> : <AiFillCaretLeft /> }
    </div>
  )

  if (isHidden) {
    return (
      <ShowMenuButton />
    )
  }

  return (
    <>
      <div className='slrspot__review-menu'>
        <div className='slrspot__review-menu-options'>
          <MenuItem name='Home' redirect=''/>
          <MenuItem name='Review' setShowTrigger={setShowReviewMenu} showTrigger={showReviewMenu}/>
          { showReviewMenu && (
            <div className='slrspot__review-submenu-options'>
              { allowChanges && <SubMenuItem name='Settings' redirect='/settings'/> }
              <SubMenuItem name='Team' redirect='/team'/>
            </div>
          )}
          <MenuItem name='Studies' setShowTrigger={setShowStudiesMenu} showTrigger={showStudiesMenu}/>
          { showStudiesMenu && (
            <div className='slrspot__review-submenu-options'>
              <SubMenuItem name='Search' redirect='/studies/search'/>
              <SubMenuItem name='Display' redirect='/studies/display'/>
              <SubMenuItem name='Duplicates' redirect='/studies/duplicates'/>
            </div>
          )}
          <MenuItem name='Screening' setShowTrigger={setShowScreeningMenu} showTrigger={showScreeningMenu}/>
          { showScreeningMenu && (
            <div className='slrspot__review-submenu-options'>
              <SubMenuItem name='Settings' redirect='/screening'/>
              <SubMenuItem name='Title & Abstract' redirect='/screening/title_abstract'/>
              <SubMenuItem name='Full text' redirect='/screening/full-text'/>
            </div>
          )}
          <MenuItem name='Results' redirect='/results'/>
        </div>
      </div>
      <ShowMenuButton />
    </>
  )
}

export default ReviewMenu