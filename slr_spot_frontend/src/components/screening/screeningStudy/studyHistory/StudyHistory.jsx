import React from 'react';
import { useState } from 'react';
import { Table } from "react-bootstrap";
import './studyHistory.css';

const testHistory = [
  {
    "date": "xxxx/xx/xx",
    "action": "importing"
  },

]

const StudyHistory = (props) => {
  const [history, setHistory] = useState(testHistory);

  const HistoryTable = () => {
    return (
      <Table>
        <thead>
          <th>Date</th>
          <th style={{ borderLeft: '0.01px solid black' }}>Action</th>
        </thead>
        <tbody>
          { history.map(record => (
            <tr>
              <td style={{ width: '20%'}}>{ record.date }</td>
              <td style={{ width: '80%', borderLeft: '0.01px solid black'}}>{ record.action }</td>
            </tr>
          ))}
        </tbody>
      </Table>
    )
  }

  return (
    <div className='slrspot__studyHistory'>
      { history.length > 0 
        ? <HistoryTable /> 
        : <h2 style={{ alignSelf: 'center', marginTop: '80px'}}>HISTORY IS EMPTY</h2>}
    </div>
  )
}

export default StudyHistory