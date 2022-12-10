import React, { useState } from 'react';
import Check from 'react-bootstrap/FormCheck';
import { Table } from "react-bootstrap";
import { DropdownButton, Dropdown } from 'react-bootstrap';
import './studiesView.css';


const testImprots = [
  {
    "id": 1,
    "title": "title",
    "authors": "author",
    "year": "1000",
  },
  {
    "id": 2,
    "title": "title2",
    "authors": "author2",
    "year": "1002",
  },
  {
    "id": 3,
    "title": "title3",
    "authors": "author3",
    "year": "1003",
  },
  {
    "id": 4,
    "title": "title4",
    "authors": "author4",
    "year": "1004",
  },
  {
    "id": 5,
    "title": "title5",
    "authors": "author5",
    "year": "1005"
  }
]

const StudiesView = () => {
  const [selected, setSelected] = useState([]);
  const [folders, setFolders] = useState([{'id':1, 'name':'test1'},{'id':2, 'name':'test2'}]);


  const handleSelect = (study) => {
    if (selected.includes(study)) {
      setSelected(selected.filter(item => item.id !== study.id));
    } else {
      setSelected(oldArray => [...oldArray, study]);
    }
  }

  const handleSelectAll = (event) => {
      if (event.target.checked) {
        setSelected(testImprots);
      } else {
        setSelected([]);
      }
  }

  const listTestData = testImprots.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>
          <Check 
            type='checkbox'
            checked={ selected.includes(item) }
            onChange={ () => handleSelect(item) } />
        </td>
        <td>{id + 1}</td>
        <td>{item.title}</td>
        <td>{item.authors}</td>
        <td>{item.year}</td>
        <td>not assigned</td>
      </tr>
    </tbody>
  );

  const FoldersDropdown = () => {
    const [title, setTitle] = useState('Select folder');

    const handleSelectFolder = (event) => {
      setTitle(event)
    }

    return (
      <DropdownButton
        id="slrspot__dropdown-folders"
        title={title} onSelect={ handleSelectFolder }>
        { folders.map(item => (
          <Dropdown.Item 
            key={item.id} 
            eventKey={item.name}>
            {item.name}
          </Dropdown.Item>
        ))}
      </DropdownButton>
    )
  }

  return (
    <div>
      <div className='slrspot__screening-options'>
        <div className='slrspot__studiesView-container'>

          <div className='slrspot__studiesView-search'>
            <div className='slrspot__screening-options-container-checks'>
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>Title</label>
              </div>
              {/* <a>switch to review mode</a> */}
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>authors</label>
              </div>
            </div>
            <div className='slrspot__screening-options-search'>
              <label>Search</label>
              <input></input>
            </div>
          </div>

          <div className='slrspot__studiesView-folders'>
            <div className='slrspot__screening-options-container-checks'>
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>Show only not assigned studies</label>
              </div>
            </div>
            <div className='slrspot__studiesView-folders-select'>
              <FoldersDropdown />
              <label>Assign</label>
            </div>
          </div>

        </div>
      </div>
      <Table>
        <thead>
          <tr>
            <th>
              <div className='d-flex'>
                <Check 
                  type='checkbox'
                  checked={ selected.length === testImprots.length } 
                  onChange={ handleSelectAll }/><span>All</span>
              </div>
            </th>
            <th>#</th>
            <th>Title</th>
            <th>Authors</th>
            <th>Year</th>
            <th>Folder</th>
          </tr>
        </thead>
        { listTestData }
      </Table>
    </div>
  )
}

export default StudiesView