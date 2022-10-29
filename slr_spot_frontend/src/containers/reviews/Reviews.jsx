import React, { useState, useEffect } from 'react'
import { Table } from "react-bootstrap";
import axiosInstance from "../../services/api";
import { useNavigate } from "react-router-dom";
import './reviews.css'

const Reviews = () => {
  const [data, setData] = useState([]);
  const navigate = useNavigate();

  const onClick = () => {
    navigate('/reviews/new');
  };

  useEffect(() => {
    axiosInstance.get("/reviews")
    .then((response) => {
      setData(response.data);
      console.log(response.data);
    });
  }, []);

  // const data = [
  //   {title:"Przeglad na temat tematow", author:"nieznany"},
  //   {title:"Przeglad na temat braku tematow", author:"znany"}
  // ];

  const listItems = data.map((d) => 
    <tbody key={d.title}>
      <tr>
        <td>1</td>
        <td>{d.title}</td>
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__reviews'>
      <div className='slrspot__reviews-header'>
        <h1>Reviews</h1>
      </div>
      <div className='slrspot__reviews-container'>
        <button onClick={onClick}>New review</button>
        <div className='slrspot__reviews-select'>
          <h2>Your reviews</h2>
          <h2>All reviews</h2>
        </div>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
            </tr>
          </thead>
          {listItems}
        </Table>
      </div>
    </div>
  )
}

export default Reviews