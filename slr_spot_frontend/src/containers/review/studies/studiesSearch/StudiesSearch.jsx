import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../services/api';
import { useNavigate, useParams } from "react-router-dom";
import { Table } from "react-bootstrap";
import { AiOutlineClose } from "react-icons/ai";
import Helper from '../../../../components/helper/Helper';
import './studiesSearch.css';
import { ConfirmationPopup, ImportDetails, StudiesImport } from '../../../../components';
import EventBus from '../../../../common/EventBus';
import { OWNER, COOWNER, MEMBER } from '../../../../constants/roles';
import ReactPaginate from 'react-paginate';


const StudiesSearch = (props) => {
  const [imports, setImports] = useState([]);
  const [showNewImport, setShowNewImport] = useState(false);
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const [showImportRemoveConfirmation, setShowImportRemoveConfirmation] = useState(false);
  const [importToRemove, setImportToRemove] = useState();
  const [showImportDetails, setShowImportDetails] = useState(false);
  const [importDetails, setImportDetails] = useState();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [pageCount, setPageCount] = useState(0);

  useEffect(() => {
    axiosInstance.get("/imports", { params: {
      reviewId
    }})
    .then((response) => {
      setImports(response.data.content);
      setPageCount(response.data.totalPages);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    axiosInstance.get("/imports", { params: {
      reviewId, page
    }})
    .then((response) => {
      setImports(response.data.content);
      setPageCount(response.data.totalPages);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }

  const helperContent = () => {
    return (
      <>
      <p>Studies can be imported using files with given formats:</p>
        <ul>
          <li>CSV</li>
          {/* <li>RIS</li> */}
          <li>BibTeX</li>
          {/* <li>XLS</li> */}
        </ul>
      </>
    )
  }

  const handleRemoveImport = (studyImport) => {
    setShowImportRemoveConfirmation(true);
    setImportToRemove(studyImport);
  }

  const onCancelRemoveImport = () => {
    setShowImportRemoveConfirmation(false);
    setImportToRemove();
  }

  const confirmRemoveImport = () => {
    axiosInstance.delete("/imports/" + importToRemove.id)
    .then(() => {
      setShowImportRemoveConfirmation(false);
      window.location.reload();
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }

  const handleShowImportDetails = (importDetails) => {
    setShowImportDetails(true);
    setImportDetails(importDetails)
  }

  const handleExitImportDetails = () => {
    setShowImportDetails(false);
    setImportDetails()
  }

  const listTestData = imports.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id + 1}</td>
        <td>{item.date}</td>
        <td>{item.numOfImportedStudies}</td>
        <td>{item.numOfRemovedDuplicates}</td>
        <td>
          <a 
            className='slrspot__review-studiesSearch-imports-showMore'
            onClick={() => handleShowImportDetails(item)}>
            SHOW
          </a>
        </td>
        { allowChanges &&
          <td>
            <AiOutlineClose 
              onClick={ () => handleRemoveImport(item) } 
              style={ {color: 'red', cursor: 'pointer'} } />
          </td>
        }
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__review-studiesSearch'>
      <div className='slrspot__review-header'>
        <h1>Studies search</h1>
      </div >

      <Helper content={ helperContent() } show={ true }/> 
      
      { allowChanges && 
        <div className='slrspot__review-studiesSearch-import'>
          <h3>Import references</h3>

          { showNewImport && <StudiesImport triggerCancel={ setShowNewImport } /> }

          <button onClick={ () => setShowNewImport(true) }>New import</button>
        </div>
      }

      <div className='slrspot__review-studiesSearch-imports-list'>
        <h3>Import history</h3>
        { imports.length == 0 
          ? <p className='slrspot__imports-empty'>NO IMPORTS AVAILABLE</p> 
          : <Table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Imported at</th>
                  {/* <th>Performed by</th> */}
                  {/* <th>Search value</th> */}
                  {/* <th>Source</th> */}
                  <th>Studies added</th>
                  <th>Duplicates removed</th>
                  <th>Details</th>
                  { allowChanges && <th>Remove</th> }
                </tr>
              </thead>
              { listTestData }
            </Table>}

        { imports.length > 0 && pageCount > 1 &&
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
        <button onClick={ () => navigate("/reviews/" + reviewId + "/studies/display") }>Show all studies</button>
      </div>
      { showImportRemoveConfirmation && 
        <ConfirmationPopup 
          title="remove import"
          message={'Do you want to remove this import? This action will remove all connected studies.'}
          triggerConfirm={ confirmRemoveImport }
          triggerCancel={ onCancelRemoveImport }
        /> 
      }
      { showImportDetails && 
        <ImportDetails 
          importDetails={ importDetails }
          triggerCancel={ handleExitImportDetails}
        />
      }
    </div>
  )
}

export default StudiesSearch