import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, ScreeningOptions, PageChanger } from '../../../../components';
import { EXCLUDED } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { OWNER, MEMBER, COOWNER } from '../../../../constants/roles';
import { FULL_TEXT, TITLE_ABSTRACT } from '../../../../constants/studyStatuses';
import { EVERYTHING_SEARCH } from '../../../../constants/searchTypes';
import ReactPaginate from 'react-paginate';
import '../screening.css';


const Excluded = (props) => {
  const [showAbstracts, setShowAbstracts] = useState(true);
  const [showTeamHighlights, setShowTeamHighlights] = useState(false);
  const [showPersonalHighlights, setShowPersonalHighlights] = useState(false);
  const [showHighlights, setShowHighlights] = useState(showTeamHighlights || showPersonalHighlights);

  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);
  const { user: currentUser } = useSelector((state) => state.auth);

  const [searchType, setSearchType] = useState(EVERYTHING_SEARCH);
  const [searchTerm, setSearchTerm] = useState('');

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [pageCount, setPageCount] = useState(0);
  const [searchPerformed, setSearchPerformed] = useState(false);

  const [sortProperty, setSortProperty] = useState('TITLE');
  const [sortDirection, setSortDirection] = useState('ASC');

  function getStudies(page, size) {
    var userId = currentUser.id;
    var stage = props.isFullText ? FULL_TEXT : TITLE_ABSTRACT;
    axiosInstance.get("/studies/excluded", { params: {
      reviewId, userId, stage, page, size, sortProperty, sortDirection
    }})
    .then((response) => {
      setStudies(response.data.content);
      setPageCount(response.data.totalPages);
      setSearchPerformed(false);
      setCurrentPage(response.data.number);
    })
    .catch(() => {
    });
  }

  function getStudiesSearch(searchValue, page, size) {
    var userId = currentUser.id;
    var stage = props.isFullText ? FULL_TEXT : TITLE_ABSTRACT;
    axiosInstance.get("/studies/excluded/search", { params: {
      reviewId, userId, stage, searchType, searchValue, page, size, sortProperty, sortDirection
    }})
    .then((response) => {
      setStudies(response.data.content);
      setPageCount(response.data.totalPages);
      setSearchPerformed(true);
      setCurrentPage(response.data.number);
    })
    .catch(() => {
    });
  }

  useEffect(() => {
    getStudies(0, pageSize)
  }, [props.isFullText]);

  const handleStudiesUpdate = (id) => {
    setStudies(studies.filter(study => study.id !== id));
  }

  const handleSearch = (searchValue) => {
    if (searchValue.trim().length > 0) {
      getStudiesSearch(searchValue, 0, pageSize);
      setSearchTerm(searchValue);
    } else {
      getStudies(0, pageSize);
    }
  }

  useEffect(() => {
    setShowHighlights(showTeamHighlights || showPersonalHighlights);
  }, [showTeamHighlights, showPersonalHighlights]);

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    setCurrentPage(page);
  }

  useEffect(() => {
    if (searchPerformed) {
      getStudiesSearch(searchTerm, currentPage, pageSize);
    } else {
      getStudies(currentPage, pageSize);
    }
  }, [currentPage, pageSize, sortDirection, sortProperty]);

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
            study={study} 
            isShowAbstracts={ showAbstracts } 
            triggerVote={ handleStudiesUpdate }   
            tab={EXCLUDED} 
            isFullText={props.isFullText}
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

export default Excluded