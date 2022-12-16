import React from 'react';
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../../constants/tabs';
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

  const Menu = () => (
    <>
      <MenuOption content='to be reviewed' isSelected={props.tab === TO_BE_REVIEWED} handleClick={() => props.setTab(TO_BE_REVIEWED)}/>
      <MenuOption content='conflicted' isSelected={props.tab === CONFLICTED} handleClick={() => props.setTab(CONFLICTED)}/>
      <MenuOption content='awaiting' isSelected={props.tab === AWAITING} handleClick={() => props.setTab(AWAITING)}/>
      <MenuOption content='excluded' isSelected={props.tab === EXCLUDED} handleClick={() => props.setTab(EXCLUDED)}/>
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