import React from 'react';
import { Table } from "react-bootstrap";

const StudyDiscussion = (props) => {

  return (
    <div className='slrspot__importDetails'>
      <div className='slrspot__importDetails-container'>
        <Table>
          <thead>
            <th>Date</th>
            <th>Author</th>
            <th>Comment</th>
          </thead>
          <tbody>
            {/* { testHistory.map(record => (
              <tr>
                <td>test</td>
                <td>test</td>
              </tr>
            ))} */}
          </tbody>
        </Table>
        {/* <div className='slrspot__importDetails-container-buttons'>
          <button 
            className='slrspot__importDetails-exitButton' 
            onClick={() => props.triggerCancel(false)}>back</button>
        </div> */}
      </div>
    </div>
  )
}

export default StudyDiscussion