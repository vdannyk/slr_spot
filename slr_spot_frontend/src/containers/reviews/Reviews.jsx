import React, { useState, useEffect } from 'react'
import { Table } from "react-bootstrap";
import axiosInstance from "../../services/api";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import './reviews.css'

const Reviews = () => {
  const [data, setData] = useState([]);
  const navigate = useNavigate();
  const [isShowAll, setIsShowAll] = useState(false);
  const { user: currentUser } = useSelector((state) => state.auth);

  const onClick = () => {
    navigate('/reviews/new');
  };

  useEffect(() => {
    if (isShowAll) {
      axiosInstance.get("/reviews")
      .then((response) => {
        setData(response.data);
        console.log(response.data);
      });
    } else {
      axiosInstance.get("/users/" + currentUser.id + "/reviews")
      .then((response) => {
        setData(response.data);
        console.log(response.data);
      });
    }
  }, [isShowAll]);

  const listItems = data.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td>{item.title}</td>
        {/* <td>{item.user.firstName} {item.user.lastName}</td> */}
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__reviews'>
      <div className='slrspot__reviews-header'>
        <h1>Reviews</h1>
        <button onClick={onClick}>New review</button>
      </div>
      <div className='slrspot__reviews-container'>
        <div className='slrspot__reviews-select'>
          <a onClick={() => setIsShowAll(false)}>Your reviews</a>
          <a onClick={() => setIsShowAll(true)}>All reviews</a>
        </div>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>Owner</th>
            </tr>
          </thead>
          {listItems}
        </Table>
      </div>
    </div>
  )
}

export default Reviews