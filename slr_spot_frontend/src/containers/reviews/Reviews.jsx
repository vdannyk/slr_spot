import React from 'react'
import { Table } from "react-bootstrap";
import './reviews.css'

const Reviews = () => {

  const data = [
    {title:"Przeglad na temat tematow", author:"nieznany"},
    {title:"Przeglad na temat braku tematow", author:"znany"}
  ];

  const listItems = data.map((d) => 
  <li key={d.title}>
    <span>{d.title}</span>
    <span> - </span>
    <span>{d.author}</span>
  </li>);

  return (
    <div className='slrspot__reviews'>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Email</th>
            <th>Contact</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>TEST 123</td>
            <td>test@test123.com</td>
            <td>1122334455</td>
          </tr>
          <tr>
            <td>2</td>
            <td>TEST 456</td>
            <td>test@test456.com</td>
            <td>6677889910</td>
          </tr>
          <tr>
            <td>3</td>
            <td>TEST 789</td>
            <td>test@test789.com</td>
            <td>6677889911</td>
          </tr>
        </tbody>
      </Table>
    </div>
  )
}

export default Reviews