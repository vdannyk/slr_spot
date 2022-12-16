import React from 'react';
import { Table } from "react-bootstrap";
import './studyHistory.css';

const testHistory = [
  {
    "date": "xxxx/xx/xx",
    "action": "importing"
  },
  {
    "date": "xxxx/xx/xx",
    "action": "comment"
  },
  {
    "date": "xxxx/xx/xx",
    "action": "delete"
  },
  {
    "date": "xxxx/xx/xx",
    "action": "taga"
  },
]

const StudyHistory = (props) => {
  return (
    <div className='slrspot__importDetails'>
      <div className='slrspot__importDetails-container'>
        <Table>
          <thead>
            <th>Date</th>
            <th>Action</th>
          </thead>
          <tbody>
            { testHistory.map(record => (
              <tr>
                <td>{ record.date }</td>
                <td>{ record.action }</td>
              </tr>
            ))}
          </tbody>
        </Table>
        <div className='slrspot__importDetails-container-buttons'>
          <button 
            className='slrspot__importDetails-exitButton' 
            onClick={() => props.triggerCancel(false)}>back</button>
        </div>
      </div>
    </div>
  )
}

export default StudyHistory