import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../services/api';
import { useNavigate, useParams } from "react-router-dom";
import { Table } from "react-bootstrap";
import { AiOutlineClose } from "react-icons/ai";
import Helper from '../../../../components/helper/Helper';
import './studiesSearch.css';
import { ConfirmationPopup, StudiesImport } from '../../../../components';
import EventBus from '../../../../common/EventBus';


const StudiesSearch = () => {
  const [imports, setImports] = useState([]);
  const [showNewImport, setShowNewImport] = useState(false);
  const { reviewId } = useParams();
  const navigate = useNavigate();
  const [showImportRemoveConfirmation, setShowImportRemoveConfirmation] = useState(false);
  const [importToRemove, setImportToRemove] = useState();

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/imports")
    .then((response) => {
      setImports(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  const helperContent = () => {
    return (
      <>
      <p>Studies can be imported using files with given formats:</p>
        <ul>
          <li>CSV</li>
          <li>RIS</li>
          <li>BibTeX</li>
          <li>XLS</li>
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

  const listTestData = imports.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id + 1}</td>
        <td>{item.date}</td>
        <td>{item.performedBy}</td>
        <td>{item.searchValue}</td>
        <td>{item.source}</td>
        <td>xx</td>
        <td>xx</td>
        <td>
          <AiOutlineClose 
            onClick={ () => handleRemoveImport(item) } 
            style={ {color: 'red', cursor: 'pointer'} } />
        </td>
      </tr>
    </tbody>
  );

  return (
    <div className='slrspot__review-studiesSearch'>
      <div className='slrspot__review-header'>
        <h1>Studies search</h1>
      </div >

      <Helper content={ helperContent() } show={ true }/> 
      
      <div className='slrspot__review-studiesSearch-import'>
        <h3>Import references</h3>

        { showNewImport && <StudiesImport triggerCancel={ setShowNewImport } /> }

        <button onClick={ () => setShowNewImport(true) }>New import</button>
      </div>

      <div className='slrspot__review-studiesSearch-imports-list'>
        <h3>Import history</h3>
        { imports.length == 0 
          ? <p className='slrspot__imports-empty'>NO IMPORTS AVAILABLE</p> 
          : <Table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Imported at</th>
                  <th>Performed by</th>
                  <th>Search value</th>
                  <th>Source</th>
                  <th>Studies added</th>
                  <th>Duplicates removed</th>
                  <th>Remove</th>
                </tr>
              </thead>
              { listTestData }
            </Table>}

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
    </div>
  )
}

export default StudiesSearch