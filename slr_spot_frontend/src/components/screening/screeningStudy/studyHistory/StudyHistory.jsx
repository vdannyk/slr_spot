import React,{ useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import axiosInstance from '../../../../services/api';
import './studyHistory.css';


const StudyHistory = (props) => {
  const [history, setHistory] = useState([]);

  useEffect(() => {
    axiosInstance.get("/studies/" + props.studyId + "/history")
    .then((response) => {
      setHistory(response.data);
    })
    .catch((error) => {
    });
  }, []);

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
              <td style={{ width: '80%', borderLeft: '0.01px solid black'}}>{ record.description }</td>
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