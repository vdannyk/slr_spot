import React from 'react';
import './screeningMenu.css';

const ScreeningMenu = () => {

  const MenuOption = (props) => (
    <div className='slrspot__screening-menu-option'>
      <p>{ props.content }</p>
    </div>
  )

  const Menu = () => (
    <>
      <MenuOption content='to be reviewed'/>
      <MenuOption content='reviewed'/>
      <MenuOption content='conflicted'/>
      <MenuOption content='awaiting'/>
      <MenuOption content='excluded'/>
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