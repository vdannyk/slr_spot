import React, { useState, useEffect } from 'react';
import Check from 'react-bootstrap/FormCheck';
import { Table } from "react-bootstrap";
import { DropdownButton, Dropdown } from 'react-bootstrap';
import axiosInstance from '../../../../../services/api';
import { useParams } from "react-router-dom";
import { AUTHORS_SEARCH, AUTHORS_YEAR_SEARCH, TITLE_AUTHORS_SEARCH, TITLE_AUTHORS_YEAR_SEARCH, TITLE_SEARCH, TITLE_YEAR_SEARCH, YEAR_SEARCH } from '../../../../../constants/searchTypes';
import ReactPaginate from 'react-paginate';
import { PageChanger } from '../../../../../components';
import { CgDuplicate } from "react-icons/cg";
import { AiOutlineSortAscending, AiOutlineSortDescending } from "react-icons/ai";
import { TITLE, AUTHORS, YEAR } from '../../../../../components/sortField/sortProperties';
import './studiesView.css';
import { ASCENDING, DESCENDING } from '../../../../../components/sortField/sortDirections';


const StudiesView = ({allowChanges}) => {
  const [loading, setLoading] = useState(false);
  const [folders, setFolders] = useState([]);
  const [selectedFolderId, setSelectedFolderId] = useState();
  const [selected, setSelected] = useState([]);
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();
  const [title, setTitle] = useState('Select folder');

  const [searchPlaceholder, setSearchPlaceholder] = useState("Title");
  const [titleCheck, setTitleCheck] = useState(true);
  const [authorsCheck, setAuthorsCheck] = useState(false);
  const [yearCheck, setYearCheck] = useState(false);

  const [searchValue, setSearchValue] = useState('');
  const [searchType, setSearchType] = useState(TITLE_SEARCH);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [pageCount, setPageCount] = useState(0);
  const [searchPerformed, setSearchPerformed] = useState(false);

  const [sortProperty, setSortProperty] = useState('TITLE');
  const [sortDirection, setSortDirection] = useState('ASC');

  const [sortTitleDirection, setSortTitleDirection] = useState('ASC');
  const [sortAuthorsDirection, setSortAuthorsDirection] = useState('ASC');
  const [sortYearDirection, setSortYearDirection] = useState('ASC');


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

  function getStudies(page, size) {
    axiosInstance.get("/studies", { params: {
      reviewId, page, size, sortProperty, sortDirection
    }})
    .then((response) => {
      setStudies(response.data.content);
      setPageCount(response.data.totalPages);
      setSearchPerformed(false);
      setLoading(false);
      setCurrentPage(response.data.number);
      setSelected([]);
    })
    .catch(() => {
    });
  }

  function getStudiesSearch(page, size) {
    axiosInstance.get("/studies/search", { params: {
      reviewId, searchType, searchValue, page, size, sortProperty, sortDirection
    }})
    .then((response) => {
      setStudies(response.data.content);
      setPageCount(response.data.totalPages);
      setSearchPerformed(true);
      setCurrentPage(response.data.number);
      setSelected([]);
    })
    .catch(() => {
    });
  }

  function getFolders() {
    axiosInstance.get("/folders", { params: {
      reviewId
    }})
    .then((response) => {
      setFolders(response.data);
    })
    .catch(() => {
    });
  }

  useEffect(() => {
    setLoading(true);
    getStudies(0, pageSize);

    getFolders();
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

  const listStudies = studies.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>
          <Check 
            type='checkbox'
            checked={ selected.includes(item) }
            onChange={ () => handleSelect(item) } />
        </td>
        { item.state === "DUPLICATES" 
          ? <><td>{id + 1}<CgDuplicate color='orange'/></td></> 
          : <td>{id + 1}</td> }
        <td>{item.title}</td>
        <td>{item.authors}</td>
        <td>{item.publicationYear}</td>
        { item.folder
          ? <td>{item.folder.name}</td> 
          : <td>--</td> }
      </tr>
    </tbody>
  );

  const handleSearchChange = (event) => {
    setSearchValue(event.target.value);
  }

  const handleSearch = () => {
    if (searchValue.trim().length > 0) {
      getStudiesSearch(0, pageSize);
    } else {
      getStudies(0, pageSize);
    }
  }

  const handleSelectFolder = (event) => {
    setTitle(folders.filter(item => item.id === parseInt(event))[0].name);
    setSelectedFolderId(parseInt(event));
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
    setCurrentPage(page);
  }

  const handleMarkDuplicates = () => {
    axiosInstance.post("/studies/duplicate", {
      studiesId: selected.map(s => s.id)
    })
    .then(() => {
      window.location.reload();
    })
    .catch(() => {
    });
  }

  const handleAssignFolder = () => {
    axiosInstance.post("/folders/" + selectedFolderId + "/studies", {
      studiesId: selected.map(s => s.id)
    })
    .then(() => {
      window.location.reload();
    })
    .catch(() => {
    });
  }

  const handleAuthorsSort = () => {
    setSortProperty(AUTHORS);
    if (sortAuthorsDirection === ASCENDING) {
      setSortAuthorsDirection(DESCENDING);
      setSortDirection(DESCENDING);
    } else {
      setSortAuthorsDirection(ASCENDING);
      setSortDirection(ASCENDING);
    }
  }

  const handleYearSort = () => {
    setSortProperty(YEAR);
    if (sortYearDirection === ASCENDING) {
      setSortYearDirection(DESCENDING);
      setSortDirection(DESCENDING);
    } else {
      setSortYearDirection(ASCENDING);
      setSortDirection(ASCENDING);
    }
  }

  const handleTitleSort = () => {
    setSortProperty(TITLE);
    if (sortTitleDirection === ASCENDING) {
      setSortTitleDirection(DESCENDING);
      setSortDirection(DESCENDING);
    } else {
      setSortTitleDirection(ASCENDING);
      setSortDirection(ASCENDING);
    }
  }

  useEffect(() => {
    if (searchPerformed) {
      getStudiesSearch(currentPage, pageSize);
    } else {
      getStudies(currentPage, pageSize);
    }
  }, [currentPage, pageSize, sortProperty, sortDirection]);

  return (
    <div>
      <div className='slrspot__screening-options'>
        <div className='slrspot__studiesView-container'>

          <div className='slrspot__screening-options-search'>

            <div className='slrspot__screening-options-search-checks-container'>
              <div className='slrspot__screening-options-check'>
                <Check
                  checked={ titleCheck } 
                  onChange={ handleTitleCheck }/>
                <span>Title</span>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check
                  checked={ authorsCheck } 
                  onChange={ handleAuthorsCheck }/>
                <span>Authors</span>
              </div>
              <div className='slrspot__screening-options-check' style={{ marginLeft: '4px' }}>
                <Check
                  checked={ yearCheck } 
                  onChange={ handleYearCheck }/>
                <span>Year</span>
              </div>
            </div>

            <div className='slrspot__screening-options-search-term' style={{ flexDirection: 'row' }}>
              <label style={{ cursor: 'pointer' }} onClick={ () => handleSearch() }>Search</label>
              <input onChange={ handleSearchChange } placeholder={ searchPlaceholder }/>
            </div>

          </div>

          <div className='slrspot__studiesView-folders'>
            { allowChanges &&
              <div className='slrspot__studiesView-folders-select'>
                <DropdownButton
                  id="slrspot__dropdown-folders"
                  title={title} onSelect={ handleSelectFolder }>
                  { folders.map(item => (
                    <Dropdown.Item 
                      key={item.name} 
                      eventKey={item.id}>
                      {item.name}
                    </Dropdown.Item>
                  ))}
                </DropdownButton>
                <label onClick={ handleAssignFolder }>Assign</label>
              </div>
            }
          </div>

          <div className='slrspot__studiesView-duplicate'>
            { allowChanges &&
              <div className='slrspot__studiesView-duplicate-option'>
                <label onClick={ handleMarkDuplicates }>Mark as duplicate</label>
              </div>
            }
          </div>

        </div>
      </div>

      <div style={{ textAlign: 'right' }}>
        { studies.length > 0 &&
          <PageChanger 
            defaultSelected={pageSize}
            options={[10,25,50]}
            changePageSize={setPageSize}
          />
        }
      </div>

      <Table>
        { studies.length > 0 ?
        <>
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
            <th onClick={ handleTitleSort } className='slrspot__studiesView-hover'>
              Title{ sortTitleDirection === ASCENDING ? <AiOutlineSortAscending size={24}/> : <AiOutlineSortDescending size={24}/>}
            </th>
            <th onClick={ handleAuthorsSort } className='slrspot__studiesView-hover'>
              Authors{ sortAuthorsDirection === ASCENDING ? <AiOutlineSortAscending size={24}/> : <AiOutlineSortDescending size={24}/>}
            </th>
            <th onClick={ handleYearSort } className='slrspot__studiesView-hover'>
              Year{ sortYearDirection === ASCENDING ? <AiOutlineSortAscending size={24}/> : <AiOutlineSortDescending size={24}/>}
            </th>
            <th>Folder</th>
          </tr>
        </thead>
        {listStudies}
        </>
        : 
        <div style={{ display: 'flex', flex:'1', alignItems: 'center', justifyContent: 'center', marginTop: '50px'}}>
          <h1 style={{ textTransform: 'uppercase' }}>Studies not found</h1>
        </div>
        }
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