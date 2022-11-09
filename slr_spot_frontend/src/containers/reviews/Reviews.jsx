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

  const onReviewClick = (id) => {
    navigate('/reviews/' + id);
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

  const listAllReviews = data.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td onClick={ () => onReviewClick(item.id) }>{item.title}</td>
        {/* <td>{item.user.firstName} {item.user.lastName}</td> */}
        <td>Mock</td>
      </tr>
    </tbody>
  );

  const listUserReviews = data.map((item, id) => 
    <tbody key={id}>
      <tr onClick={ () => onReviewClick(item.id) }>
        <td>{id+1}</td>
        <td>{item.title}</td>
      </tr>
    </tbody>
  );

  const ReviewsTable = () => {
    if (isShowAll) {
      return (
        <Table striped hover>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
              <th>Owner</th>
            </tr>
          </thead>
          {listAllReviews}
        </Table>
      )
    } else {
      return (
        <Table striped hover>
          <thead>
            <tr>
              <th>#</th>
              <th>Title</th>
            </tr>
          </thead>
          {listUserReviews}
        </Table>
      )
    }
  }

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
        <ReviewsTable />
      </div>
    </div>
  )
}

export default Reviews