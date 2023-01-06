import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, ScreeningOptions, PageChanger } from '../../../../components';
import { TO_BE_REVIEWED } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { OWNER, MEMBER, COOWNER } from '../../../../constants/roles';
import { FULL_TEXT, TITLE_ABSTRACT } from '../../../../constants/studyStatuses';
import { EVERYTHING_SEARCH } from '../../../../constants/searchTypes';
import ReactPaginate from 'react-paginate';
import '../screening.css';

const ToBeReviewed = (props) => {
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [showTeamHighlights, setShowTeamHighlights] = useState(false);
  const [showPersonalHighlights, setShowPersonalHighlights] = useState(false);
  const [showHighlights, setShowHighlights] = useState(showTeamHighlights || showPersonalHighlights);

  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  const [searchType, setSearchType] = useState(EVERYTHING_SEARCH);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [pageCount, setPageCount] = useState(0);

  function getStudies() {
    var userId = currentUser.id;
    var page = currentPage;
    var size = pageSize;
    if (props.isFullText) {
      var stage = 'FULL_TEXT';
      axiosInstance.get("/studies/to-be-reviewed", { params: {
        reviewId, userId, stage, page, size
      }})
      .then((response) => {
        setStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setCurrentPage(response.number);
      })
      .catch(() => {
      });
    } else {
      var stage = 'TITLE_ABSTRACT';
      axiosInstance.get("/studies/to-be-reviewed", { params: {
        reviewId, userId, stage, page, size
      }})
      .then((response) => {
        setStudies(response.data.content)
        setPageCount(response.data.totalPages);
        setCurrentPage(response.number);
      })
      .catch(() => {
      });
    }
  }

  const handleStudiesUpdate = (id) => {
    setStudies(studies.filter(study => study.id !== id));
  }

  const handleSearch = (searchValue) => {
    var userId = currentUser.id;
    var stage = props.isFullText ? FULL_TEXT : TITLE_ABSTRACT;
    if (searchValue.trim().length > 0) {
      axiosInstance.get("/studies/to-be-reviewed/search", { params: {
        reviewId, userId, stage, searchType, searchValue 
      }})
      .then((response) => {
        setStudies(response.data.content)
      })
      .catch(() => {
      });
    } else {
      getStudies();
    }
  }

  useEffect(() => {
    getStudies()
  }, [props.isFullText]);

  useEffect(() => {
    setShowHighlights(showTeamHighlights || showPersonalHighlights);
  }, [showTeamHighlights, showPersonalHighlights]);

  const handlePageChange = (studyPage) => {

  }

  return (
    <div className='slrspot__screening-studies'>
      <ScreeningOptions 
        triggerShowAbstractsChange={setShowAbstracts}
        showAbstracts={showAbstracts} 
        triggerShowTeamHighlights={setShowTeamHighlights}
        showTeamHighlights={showTeamHighlights} 
        triggerShowPersonalHighlights={setShowPersonalHighlights}
        showPersonalHighlights={showPersonalHighlights} 
        handleSearch={ handleSearch } 
        setSearchType={ setSearchType }/>

      <div style={{ textAlign: 'right' }}>
        { studies.length > 0 &&
          <PageChanger 
            defaultSelected={pageSize}
            options={[5,10,25]}
            changePageSize={setPageSize}
          />
        }
      </div>

      { studies.length > 0 
        ? studies.map(study => (
          <ScreeningStudy 
            study={ study } 
            isShowAbstracts={ showAbstracts } 
            triggerVote={ handleStudiesUpdate }
            tab={ TO_BE_REVIEWED }
            isFullText={ props.isFullText } 
            reviewTags={ props.reviewTags } 
            allowChanges={ allowChanges } 
            showHighlights={ showHighlights } 
            highlights={ showTeamHighlights ? props.teamHighlights : props.personalHighlights } />
          )) 
        : 
        <div style={{ display: 'flex', flex:'1', alignItems: 'center', justifyContent: 'center', marginTop: '50px'}}>
          <h1 style={{ textTransform: 'uppercase' }}>Studies not found</h1>
        </div>
      }

      { studies.length > 0 && pageCount > 1 &&
        <ReactPaginate
          pageCount={pageCount}
          pageRangeDisplayed={5}
          marginPagesDisplayed={2}
          onPageChange={handlePageChange}
          forcePage={currentPage}
          containerClassName="slrspot__pagination"
          activeClassName="slrspot__pagination-active"
        />
      }

    </div>
  )
}

export default ToBeReviewed