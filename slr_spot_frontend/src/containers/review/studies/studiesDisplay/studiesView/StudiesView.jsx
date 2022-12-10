import React from 'react';
import Check from 'react-bootstrap/FormCheck';
import { Table } from "react-bootstrap";

import './studiesView.css';
import { useState } from 'react';

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
      </tr>
    </tbody>
  );

  return (
    <div>
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
          </tr>
        </thead>
        { listTestData }
      </Table>
    </div>
  )
}

export default StudiesView