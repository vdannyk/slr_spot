import React from 'react'
import { Table } from "react-bootstrap";
import './reviews.css'

const Reviews = () => {

  const data = [
    {title:"Przeglad na temat tematow", author:"nieznany"},
    {title:"Przeglad na temat braku tematow", author:"znany"}
  ];

  const listItems = data.map((d) => 
    <tbody key={d.title}>
      <tr>
        <td>1</td>
        <td>{d.title}</td>
        <td>{d.author}</td>
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__reviews'>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Title</th>
            <th>Author</th>
          </tr>
        </thead>
        {listItems}
      </Table>
    </div>
  )
}

export default Reviews