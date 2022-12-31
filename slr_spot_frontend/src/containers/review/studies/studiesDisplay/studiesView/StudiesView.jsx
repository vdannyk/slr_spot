import React, { useState, useEffect } from 'react';
import Check from 'react-bootstrap/FormCheck';
import { Table } from "react-bootstrap";
import { DropdownButton, Dropdown } from 'react-bootstrap';
import axiosInstance from '../../../../../services/api';
import { useParams } from "react-router-dom";
import { AUTHORS_SEARCH, AUTHORS_YEAR_SEARCH, TITLE_AUTHORS_SEARCH, TITLE_AUTHORS_YEAR_SEARCH, TITLE_SEARCH, TITLE_YEAR_SEARCH, YEAR_SEARCH } from '../../../../../constants/searchTypes';
import ReactPaginate from 'react-paginate';
import './studiesView.css';


const StudiesView = ({allowChanges}) => {
  const [loading, setLoading] = useState(false);
  const [folders, setFolders] = useState([{'id':1, 'name':'test1'},{'id':2, 'name':'test2'}]);
  const [selected, setSelected] = useState([]);
  const [studies, setStudies] = useState([]);
  const [searchValue, setSearchValue] = useState('');
  const { reviewId } = useParams();
  const [searchType, setSearchType] = useState(TITLE_SEARCH);
  const [searchPlaceholder, setSearchPlaceholder] = useState("Title");
  const [titleCheck, setTitleCheck] = useState(true);
  const [authorsCheck, setAuthorsCheck] = useState(false);
  const [yearCheck, setYearCheck] = useState(false);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageCount, setPageCount] = useState(0);
  const [searchPerformed, setSearchPerformed] = useState(false);


  useEffect(() => {
    if (titleCheck && authorsCheck && yearCheck) {
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
      setSearchType(TITLE_SEARCH);
      setSearchPlaceholder('Title...')
      setTitleCheck(true);
    }
  }, [titleCheck, authorsCheck, yearCheck]);

  useEffect(() => {
    setLoading(true);
    axiosInstance.get("/studies", { params: {
      reviewId
    }})
    .then((response) => {
      setStudies(response.data.content);
      setPageCount(response.data.totalPages);
      setSearchPerformed(false);
      setLoading(false);
    })
    .catch(() => {
    });
  }, []);


  const handleSelect = (study) => {
    if (selected.includes(study)) {
      setSelected(selected.filter(item => item.id !== study.id));
    } else {
      setSelected(oldArray => [...oldArray, study]);
    }
  }

  const handleSelectAll = (event) => {
      if (event.target.checked) {
        setSelected(studies);
      } else {
        setSelected([]);
      }
  }

  const listTestData = studies.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>
          <Check 
            type='checkbox'
            checked={ selected.includes(item) }
            onChange={ () => handleSelect(item) } />
        </td>
        <td>{id + 1}</td>
        <td>{item.title}</td>
        <td>{item.authors}</td>
        <td>{item.publicationYear}</td>
        <td>not assigned</td>
      </tr>
    </tbody>
  );

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  }

  const handleSearch = () => {
    axiosInstance.get("/studies/search", { params: {
      reviewId, searchType, searchValue
    }})
    .then((response) => {
      setStudies(response.data.content);
      setPageCount(response.data.totalPages);
      setSearchPerformed(true);
    })
    .catch(() => {
    });
  }

  const FoldersDropdown = () => {
    const [title, setTitle] = useState('Select folder');

    const handleSelectFolder = (event) => {
      setTitle(event)
    }

    return (
      <DropdownButton
        id="slrspot__dropdown-folders"
        title={title} onSelect={ handleSelectFolder }>
        { folders.map(item => (
          <Dropdown.Item 
            key={item.id} 
            eventKey={item.name}>
            {item.name}
          </Dropdown.Item>
        ))}
      </DropdownButton>
    )
  }

  const handleTitleCheck = () => {
    setTitleCheck(!titleCheck);
  }

  const handleAuthorsCheck = () => {
    setAuthorsCheck(!authorsCheck);
  }

  const handleYearCheck = () => {
    setYearCheck(!yearCheck);
  }

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    if (searchPerformed) {
      axiosInstance.get("/studies/search", { params: {
        reviewId, searchType, searchValue, page
      }})
      .then((response) => {
        setStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setSearchPerformed(true);
      })
      .catch(() => {
      });
    } else {
      setLoading(true);
      axiosInstance.get("/studies", { params: {
        reviewId, page
      }})
      .then((response) => {
        setStudies(response.data.content);
        setPageCount(response.data.totalPages);
        setSearchPerformed(false);
        setLoading(false);
      })
      .catch(() => {
      });
    }
  }

  return (
    <div>
      <div className='slrspot__screening-options'>
        <div className='slrspot__studiesView-container'>

          <div className='slrspot__studiesView-search'>
            <div className='slrspot__screening-options-container-checks'>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '20px' }}>
                <Check
                  checked={ titleCheck } 
                  onChange={ handleTitleCheck }/>
                <label>Title</label>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check
                  checked={ authorsCheck } 
                  onChange={ handleAuthorsCheck }/>
                <label>Authors</label>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check
                  checked={ yearCheck } 
                  onChange={ handleYearCheck }/>
                <label>Year</label>
              </div>
            </div>
            <div className='slrspot__screening-options-search'>
              <label onClick={ handleSearch }>Search</label>
              <input onChange={ handleSearchChange } placeholder={ searchPlaceholder }/>
            </div>
          </div>

          <div className='slrspot__studiesView-folders'>
            <div className='slrspot__screening-options-container-checks'>
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>Show only not assigned studies</label>
              </div>
            </div>
            { allowChanges &&
              <div className='slrspot__studiesView-folders-select'>
                <FoldersDropdown />
                <label>Assign</label>
              </div>
            }
          </div>

          <div className='slrspot__studiesView-duplicate'>
            { allowChanges &&
              <div className='slrspot__studiesView-duplicate-option'>
                <label>Mark as duplicate</label>
              </div>
            }
          </div>

        </div>
      </div>
      <Table>
        <thead>
          <tr>
            <th>
              <div className='d-flex'>
                <Check 
                  type='checkbox'
                  checked={ selected.length === studies.length } 
                  onChange={ handleSelectAll }/><span>All</span>
              </div>
            </th>
            <th>#</th>
            <th>Title</th>
            <th>Authors</th>
            <th>Year</th>
            <th>Folder</th>
          </tr>
        </thead>
        { listTestData }
      </Table>

      { studies.length > 0 && pageCount > 1 &&
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
  )
}

export default StudiesView