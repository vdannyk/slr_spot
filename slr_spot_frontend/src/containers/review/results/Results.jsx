import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { useParams } from 'react-router-dom';
import Check from 'react-bootstrap/FormCheck';
import { ContentPopup, PageChanger } from '../../../components'
import './results.css';
import ExtractData from './ExtractData';
import GenerateRaport from './GenerateRaport';
import ExportStudies from './ExportStudies';
import ResultStudy from './ResultStudy';
import { OWNER, MEMBER, COOWNER } from '../../../constants/roles';
import { AUTHORS_SEARCH, AUTHORS_YEAR_SEARCH, TITLE_AUTHORS_SEARCH, TITLE_AUTHORS_YEAR_SEARCH, TITLE_SEARCH, TITLE_YEAR_SEARCH, YEAR_SEARCH, EVERYTHING_SEARCH } from '../../../constants/searchTypes';
import ReactPaginate from 'react-paginate';

const Results = (props) => {
  const [includedStudies, setIncludedStudies] = useState([]);
  const [selected, setSelected] = useState([]);
  const [showExtractMenu, setShowExtractMenu] = useState(false);
  const [showExportMenu, setShowExportMenu] = useState(false);
  const [showRaportMenu, setShowRaportMenu] = useState(false);
  const [reviewTags, setReviewTags] = useState([]);
  const { reviewId } = useParams();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  const [searchValue, setSearchValue] = useState('');
  const [searchPlaceholder, setSearchPlaceholder] = useState("Everything");
  const [searchType, setSearchType] = useState(EVERYTHING_SEARCH);
  const [titleCheck, setTitleCheck] = useState(false);
  const [authorsCheck, setAuthorsCheck] = useState(false);
  const [yearCheck, setYearCheck] = useState(false);
  const [everythingCheck, setEverythingCheck] = useState(true);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [pageCount, setPageCount] = useState(0);
  const [searchPerformed, setSearchPerformed] = useState(false);

  function getStudies() {
    var page = currentPage;
    var size = pageSize;
    axiosInstance.get("/studies/included", { params: {
      reviewId, page, size
    }})
    .then((response) => {
      setIncludedStudies(response.data.content);
    });
  }

  useEffect(() => {
    getStudies();
  }, []);

  useEffect(() => {
    axiosInstance.get("/tags", { params: {
      reviewId
    }})
    .then((response) => {
      setReviewTags(response.data)
    })
    .catch(() => {
    });
  }, []);

  const handleStudiesUpdate = (id) => {
    setIncludedStudies(includedStudies.filter(study => study.id !== id));
  }

  const handleSelectAll = (event) => {
    if (event.target.checked) {
      setSelected(includedStudies);
    } else {
      setSelected([]);
    }
  }

  const handleSelect = (study) => {
    if (selected.includes(study)) {
      setSelected(selected.filter(item => item.id !== study.id));
    } else {
      setSelected(oldArray => [...oldArray, study]);
    }
  }

  useEffect(() => {
    if (everythingCheck) {
      setSearchType(EVERYTHING_SEARCH);
      setSearchPlaceholder('Everything...');
    }
    else if (titleCheck && authorsCheck && yearCheck) {
      setSearchType(TITLE_AUTHORS_YEAR_SEARCH);
      setSearchPlaceholder('Title, Authors, Year...')
    }
    else if (titleCheck && authorsCheck) {
      setSearchType(TITLE_AUTHORS_SEARCH);
      setSearchPlaceholder('Title, Authors...')
    }
    else if (titleCheck && yearCheck) {
      setSearchType(TITLE_YEAR_SEARCH);
      setSearchPlaceholder('Title, Year...')
    }
    else if (authorsCheck && yearCheck) {
      setSearchType(AUTHORS_YEAR_SEARCH);
      setSearchPlaceholder('Authors, Year...')
    }
    else if (titleCheck) {
      setSearchType(TITLE_SEARCH);
      setSearchPlaceholder('Title...')
    }
    else if (authorsCheck) {
      setSearchType(AUTHORS_SEARCH);
      setSearchPlaceholder('Authors...')
    } else if (yearCheck) {
      setSearchType(YEAR_SEARCH);
      setSearchPlaceholder('Year...')
    } else {
      setSearchType(EVERYTHING_SEARCH);
      setSearchPlaceholder('Everything...')
      setEverythingCheck(true);
    }
  }, [titleCheck, authorsCheck, yearCheck, everythingCheck]);

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  }

  const handleTitleCheck = () => {
    setTitleCheck(!titleCheck);
    if (everythingCheck) {
      setEverythingCheck(false);
    }
  }

  const handleAuthorsCheck = () => {
    setAuthorsCheck(!authorsCheck);
    if (everythingCheck) {
      setEverythingCheck(false);
    }
  }

  const handleYearCheck = () => {
    setYearCheck(!yearCheck);
    if (everythingCheck) {
      setEverythingCheck(false);
    }
  }

  const handleEverythingCheck = () => {
    if (everythingCheck) {
      setEverythingCheck(false);
    } else {
      setEverythingCheck(true);
      setTitleCheck(false);
      setAuthorsCheck(false);
      setYearCheck(false);
    }
  }

  const handleSearch = (searchValue) => {
    if (searchValue.trim().length > 0) {
      axiosInstance.get("/studies/included/search", { params: {
        reviewId, searchType, searchValue 
      }})
      .then((response) => {
        setIncludedStudies(response.data.content)
      })
      .catch(() => {
      });
    } else {
      getStudies();
    }
  }

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    var size = pageSize;
    if (searchPerformed) {
      axiosInstance.get("/studies/included/search", { params: {
        reviewId, searchType, searchValue, page, size 
      }})
      .then((response) => {
        setIncludedStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setSearchPerformed(true);
        setCurrentPage(response.number);
      })
      .catch(() => {
      });
    } else {
      axiosInstance.get("/studies/included", { params: {
        reviewId, page, size
      }})
      .then((response) => {
        setIncludedStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setSearchPerformed(false);
        setCurrentPage(response.number);
      });
    }
  }

  useEffect(() => {
    var page = currentPage;
    var size = pageSize;
    if (searchPerformed) {
      axiosInstance.get("/studies/included/search", { params: {
        reviewId, searchType, searchValue, page, size 
      }})
      .then((response) => {
        setIncludedStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setSearchPerformed(true);
        setCurrentPage(response.number);
      })
      .catch(() => {
      });
    } else {
      axiosInstance.get("/studies/included", { params: {
        reviewId, page, size
      }})
      .then((response) => {
        setIncludedStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setSearchPerformed(false);
        setCurrentPage(response.number);
      });
    }
  }, [pageSize]);

  return (
    <div className='slrspot__review-results'>
      <div className='slrspot__review-header'>
        <h1>review results</h1>
      </div>
      
      <div className='slrspot__screening-options'>
        <div className='slrspot__screening-options-container'>

          <div className='slrspot__screening-options-search'>

            <div className='slrspot__screening-options-search-term'>
              <label onClick={ () => handleSearch(searchValue) }>Search</label>
              <input onChange={ handleSearchChange } placeholder={searchPlaceholder} />
            </div>

            <div className='slrspot__screening-options-search-checks'>
              
              <div className='slrspot__screening-options-search-checks-container'>
                <Check 
                  onChange={ handleTitleCheck } 
                  checked={ titleCheck } />
                <span>Title</span>
                <Check 
                  onChange={ handleAuthorsCheck } 
                  checked={ authorsCheck } 
                  style={{ marginLeft: '40px' }}/>
                <span>Authors</span>
              </div>

              <div className='slrspot__screening-options-search-checks-container'>
                <Check 
                  onChange={ handleYearCheck } 
                  checked={ yearCheck } />
                <span>Year</span>
                <Check 
                  onChange={ handleEverythingCheck } 
                  checked={ everythingCheck } 
                  style={{ marginLeft: '40px' }}/>
                <span>Everything</span>
              </div>

            </div>
          </div>

          { allowChanges && 
            <div className='slrspot__screening-options-container-buttons'>
              <button onClick={ () => setShowExtractMenu(true) }>extract data</button>
              <button onClick={ () => setShowExportMenu(true) }>export studies</button>
              <button onClick={ () => setShowRaportMenu(true) }>generate report</button>
            </div>
          }

        </div>
      </div>

      <div className='slrspot__review-results-list'>
        <div style={{ textAlign: 'right' }}>
          <PageChanger 
            defaultSelected={pageSize}
            options={[5,10,25]}
            changePageSize={setPageSize}
          />
        </div>
        <div className='slrspot__review-list-selectAll'>
          <Check 
            type='checkbox'
            checked={ selected.length === includedStudies.length } 
            onChange={ handleSelectAll }/>
          <p>Select all</p>
        </div>
        { includedStudies.map(study => (
          <ResultStudy 
            study={ study } 
            selected={ selected }
            handleSelect= { () => handleSelect(study) }
            allowChanges={ allowChanges } 
            reviewTags={ reviewTags } 
            triggerStudiesUpdate={ handleStudiesUpdate }/>
        ))}
        { includedStudies.length > 0 && pageCount > 1 &&
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
      { showExtractMenu && <ContentPopup content={<ExtractData selectedStudies={selected} />} triggerExit={ () => setShowExtractMenu(false)} /> }
      { showExportMenu && <ContentPopup content={<ExportStudies />} triggerExit={ () => setShowExportMenu(false)} /> }
      { showRaportMenu && <ContentPopup content={<GenerateRaport />} triggerExit={ () => setShowRaportMenu(false)} /> }
    </div>
  )
}

export default Results