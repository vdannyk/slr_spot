import React from 'react';
import { Table } from "react-bootstrap";
import './studyDiscussion.css'


const testNotes = [
  {
    "date": "xxxx/xx/xx",
    "author": "author",
    "comment": "comment"
  },
  {
    "date": "xxxx/xx/xx",
    "author": "Daniel Kwasniak",
    "comment": "comment"
  },
  {
    "date": "xxxx/xx/xx",
    "author": "author",
    "comment": "comment"
  },
  {
    "date": "xxxx/xx/xx",
    "author": "author",
    "comment": "comment"
  },
  {
    "date": "xxxx/xx/xx",
    "author": "author",
    "comment": "comment"
  },
  {
    "date": "xxxx/xx/xx",
    "author": "author",
    "comment": "comment comment comment commentcomment comment comment comment"
  },
  {
    "date": "xxxx/xx/xx",
    "author": "author",
    "comment": "comment"
  }
]

const StudyDiscussion = (props) => {

  return (
    <div className='slrspot__studyDiscussion'>
      <Table>
        <thead>
          <th>Date</th>
          <th>Author</th>
          <th style={{borderLeft: '0.01px solid black'}}>Comment</th>
        </thead>
        <tbody>
          { testNotes.map(record => (
            <tr>
              <td style={{ width: '20%'}}>{record.date}</td>
              <td style={{ width: '30%'}}>{record.author}</td>
              <td style={{ width: '50%', borderLeft: '0.01px solid black'}}>{record.comment}</td>
            </tr>
          ))}
        </tbody>
      </Table>
      <div className='slrspot__studyDiscussion-comment'>
        <textarea></textarea>
        <button>comment</button>
      </div>
    </div>
  )
}

export default StudyDiscussion