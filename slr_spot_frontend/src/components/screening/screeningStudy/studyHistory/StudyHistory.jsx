import React from 'react';
import { Table } from "react-bootstrap";
import './studyHistory.css';

const testHistory = [
  {
    "date": "xxxx/xx/xx",
    "action": "importing"
  },

]

const StudyHistory = (props) => {
  return (
    <div className='slrspot__studyHistory'>
      <Table>
        <thead>
          <th>Date</th>
          <th style={{ borderLeft: '0.01px solid black' }}>Action</th>
        </thead>
        <tbody>
          { testHistory.map(record => (
            <tr>
              <td style={{ width: '20%'}}>{ record.date }</td>
              <td style={{ width: '80%', borderLeft: '0.01px solid black'}}>{ record.action }</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  )
}

export default StudyHistory