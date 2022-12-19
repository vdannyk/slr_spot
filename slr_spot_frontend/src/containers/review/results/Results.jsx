import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { useParams } from 'react-router-dom';
import { Table } from "react-bootstrap";
import Check from 'react-bootstrap/FormCheck';
import './results.css';

const Results = () => {
  const [includedStudies, setIncludedStudies] = useState([]);
  const [selected, setSelected] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get("/studies/included", { params: {
      reviewId
    }})
    .then((response) => {
      console.log(response.data);
      setIncludedStudies(response.data);
    });
  }, []);

  const handleSelectAll = (event) => {
    if (event.target.checked) {
      setSelected(includedStudies);
    } else {
      setSelected([]);
    }
  }

  const handleSelect = (study) => {
    if (selected.includes(study)) {
      setSelected(selected.filter(item => item.id !== study.id));
    } else {
      setSelected(oldArray => [...oldArray, study]);
    }
  }

  const listStudies = includedStudies.map((item, id) => 
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
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__review-results'>
      <div className='slrspot__review-header'>
        <h1>review results</h1>
      </div>
      <div className='slrspot__review-duplicates-container'>
        <Table>
          <thead>
            <tr>
              <th>
                <div className='d-flex'>
                  <Check 
                    type='checkbox'
                    checked={ selected.length === includedStudies.length } 
                    onChange={ handleSelectAll }/><span>All</span>
                </div>
              </th>
              <th>#</th>
              <th>Title</th>
              <th>Authors</th>
              <th>Year</th>
            </tr>
          </thead>
          { listStudies }
        </Table>
      </div>
    </div>
  )
}

export default Results