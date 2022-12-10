import React from 'react';
import './screeningMenu.css';

const ScreeningMenu = (props) => {

  const MenuOption = (optionProps) => {
    if (optionProps.isSelected) {
      return (
        <div className='slrspot__screening-menu-option' id='selected'>
          <p>{ optionProps.content }</p>
        </div>
      )
    } else {
      return (
        <div onClick={optionProps.handleClick} className='slrspot__screening-menu-option'>
          <p>{ optionProps.content }</p>
        </div>
      )
    }
  }

  const handleToBeReviewedClick = () => {
    props.setIsToBeReviewed(true);
    props.setIsReviewed(false);
    props.setIsConflicts(false);
    props.setIsAwaiting(false);
    props.setIsExcluded(false);
  }

  const handleReviewedClick = () => {
    props.setIsToBeReviewed(false);
    props.setIsReviewed(true);
    props.setIsConflicts(false);
    props.setIsAwaiting(false);
    props.setIsExcluded(false);
  }
  
  const handleConflictedClick = () => {
    props.setIsToBeReviewed(false);
    props.setIsReviewed(false);
    props.setIsConflicts(true);
    props.setIsAwaiting(false);
    props.setIsExcluded(false);
  }

  const handleAwaitingClick = () => {
    props.setIsToBeReviewed(false);
    props.setIsReviewed(false);
    props.setIsConflicts(false);
    props.setIsAwaiting(true);
    props.setIsExcluded(false);
  }

  const handleExcludedClick = () => {
    props.setIsToBeReviewed(false);
    props.setIsReviewed(false);
    props.setIsConflicts(false);
    props.setIsAwaiting(false);
    props.setIsExcluded(true);
  }

  const Menu = () => (
    <>
      <MenuOption content='to be reviewed' isSelected={props.isToBeReviewed} handleClick={handleToBeReviewedClick}/>
      <MenuOption content='reviewed' isSelected={props.isReviewed} handleClick={handleReviewedClick}/>
      <MenuOption content='conflicted' isSelected={props.isConflicts} handleClick={handleConflictedClick}/>
      <MenuOption content='awaiting' isSelected={props.isAwaiting} handleClick={handleAwaitingClick}/>
      <MenuOption content='excluded' isSelected={props.isExcluded} handleClick={handleExcludedClick}/>
    </>
  )

  return (
    <div className='slrspot__screening-menu'>
      <div className="slrspot__screening-menu-options">
        <Menu />
      </div>
    </div>
  )
}

export default ScreeningMenu