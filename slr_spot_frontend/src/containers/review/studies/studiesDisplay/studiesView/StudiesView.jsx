import React, { useState, useEffect } from 'react';
import Check from 'react-bootstrap/FormCheck';
import { Table } from "react-bootstrap";
import { DropdownButton, Dropdown } from 'react-bootstrap';
import axiosInstance from '../../../../../services/api';
import { useParams } from "react-router-dom";
import './studiesView.css';


const StudiesView = ({allowChanges}) => {
  const [loading, setLoading] = useState(false);
  const [folders, setFolders] = useState([{'id':1, 'name':'test1'},{'id':2, 'name':'test2'}]);
  const [selected, setSelected] = useState([]);
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    setLoading(true);
    axiosInstance.get("/studies", { params: {
      reviewId
    }})
    .then((response) => {
      setStudies(response.data)
      setLoading(false);
    })
    .catch(() => {
    });
  }, []);


  const handleSelect = (study) => {
    if (selected.includes(study)) {
      setSelected(selected.filter(item => item.id !== study.id));
    } else {
      setSelected(oldArray => [...oldArray, study]);
    }
  }

  const handleSelectAll = (event) => {
      if (event.target.checked) {
        setSelected(studies);
      } else {
        setSelected([]);
      }
  }

  const listTestData = studies.map((item, id) => 
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
        <td>{item.publicationYear}</td>
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
              <div className='slrspot__screening-options-check' style={{ marginLeft: '20px' }}>
                <Check />
                <label>Title</label>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check />
                <label>Abstract</label>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check />
                <label>Authors</label>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check />
                <label>Year</label>
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
            { allowChanges &&
              <div className='slrspot__studiesView-folders-select'>
                <FoldersDropdown />
                <label>Assign</label>
              </div>
            }
          </div>

          <div className='slrspot__studiesView-duplicate'>
            { allowChanges &&
              <div className='slrspot__studiesView-duplicate-option'>
                <label>Mark as duplicate</label>
              </div>
            }
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
                  checked={ selected.length === studies.length } 
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