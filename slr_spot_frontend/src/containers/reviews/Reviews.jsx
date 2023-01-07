import React, { useState, useEffect } from 'react'
import { Table } from "react-bootstrap";
import axiosInstance from "../../services/api";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import './reviews.css'
import ReactPaginate from 'react-paginate';


const Reviews = () => {
  const [data, setData] = useState([]);
  const navigate = useNavigate();
  const [isShowAll, setIsShowAll] = useState(false);
  const { user: currentUser } = useSelector((state) => state.auth);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [pageCount, setPageCount] = useState(0);

  const onReviewClick = (id) => {
    navigate('/reviews/' + id);
  };

  useEffect(() => {
    var page = currentPage;
    if (isShowAll) {
      axiosInstance.get("/reviews/public", { params: {
        page
      }})
      .then((response) => {
        setData(response.data.reviews);
        setPageCount(response.data.totalPagesNum);
      })
      .catch((error) => {
      });
    } else {
      var userId = currentUser.id;
      axiosInstance.get("/reviews", { params: {
        userId, page
      }})
      .then((response) => {
        setData(response.data.reviews);
        setPageCount(response.data.totalPagesNum);
      })
      .catch((error) => {
        console.log(error);
      });
    }
  }, [isShowAll, currentPage]);

  const listAllReviews = data.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id+1}</td>
        <td onClick={ () => onReviewClick(item.review.id) }>{item.review.title}</td>
        <td>{item.firstName} {item.lastName}</td>
      </tr>
    </tbody>
  );

  const listUserReviews = data.map((item, id) => 
    <tbody key={id}>
      <tr onClick={ () => onReviewClick(item.review.id) }>
        <td>{id+1}</td>
        <td>{item.review.title}</td>
      </tr>
    </tbody>
  );

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    setCurrentPage(page);
  }

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
        <button onClick={ () => navigate('/reviews/new') }>New review</button>
      </div>

      <div className='slrspot__reviews-container'>

        <div className='slrspot__reviews-select'>
          <a onClick={() => setIsShowAll(false)}>Your reviews</a>
          <a onClick={() => setIsShowAll(true)}>Public reviews</a>
        </div>

        { data.length > 0 
        ? <ReviewsTable />
        : 
        <div style={{ display: 'flex', flex:'1', alignItems: 'center', justifyContent: 'center', marginTop: '50px'}}>
          <h1 style={{ textTransform: 'uppercase' }}>{ isShowAll ? 'public studies not found' : 'studies not found'}</h1>
        </div>
        }
        

        { data.length > 0 && pageCount > 1 &&
          <ReactPaginate
            pageCount={pageCount}
            pageRangeDisplayed={5}
            marginPagesDisplayed={2}
            onPageChange={handlePageChange}
            forcePage={currentPage}
            containerClassName="slrspot__folder-pagination"
            activeClassName="slrspot__folder-pagination-active"
          />
        }
        
      </div>
    </div>
  )
}

export default Reviews