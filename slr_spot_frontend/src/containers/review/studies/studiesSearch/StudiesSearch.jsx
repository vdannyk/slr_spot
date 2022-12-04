import React, { useState } from 'react';
import axiosInstance from '../../../../services/api';
import { useNavigate, useParams } from "react-router-dom";
import { Table } from "react-bootstrap";
import { AiOutlineClose } from "react-icons/ai";
import Helper from '../../../../components/helper/Helper';
import './studiesSearch.css';

const testImprots = [
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  },
  {
    "importedAt": "xx/xx/xxxx",
    "performedBy": "tester",
    "searchValue": "bobry",
    "source": "Web of science",
    "studiesAdded": 0,
    "duplicatesRemoved": "Not found",
  }
]


const StudiesSearch = () => {
  const [showHelper, setShowHelper] = useState(true);
  const [selectedFiles, setSelectedFiles] = useState(undefined);
  const [currentFile, setCurrentFile] = useState(undefined);
  const { reviewId } = useParams();
  const navigate = useNavigate();

  const listTestData = testImprots.map((item, id) => 
    <tbody key={id}>
      <tr>
        <td>{id + 1}</td>
        <td>{item.importedAt}</td>
        <td>{item.performedBy}</td>
        <td>{item.searchValue}</td>
        <td>{item.source}</td>
        <td>{item.studiesAdded}</td>
        <td>{item.duplicatesRemoved}</td>
        <td><AiOutlineClose /></td>
      </tr>
    </tbody>
  );

  const selectFile = (event) => {
    setSelectedFiles(event.target.files);
  };

  const uploadFile = () => {
    console.log(currentFile[0]);
    let data = new FormData();
    data.append("file", currentFile[0]);
    data.append("searchValue", "test");
    data.append("source", "IEEE");

    return axiosInstance.post("/studies/load-from-file", data)
    .then(function (response) {
      console.log(response);
    })
  }

  const handleOnFileChange = (event) => {
    setCurrentFile(event.target.files);
  }

  const helperContent = () => {
    return (
      <>
      <p>Studies can be imported using one of given formats</p>
        <ul>
          <li>csv - specific for chosen database or default</li>
          <li>RIS</li>
          <li>BibTeX</li>
        </ul>
      </>
    )
  }

  return (
    <div className='slrspot__review-studiesSearch'>
      <div className='slrspot__review-header'>
        <h1>Studies search</h1>
      </div >

      <Helper content={ helperContent() } show={ true }/> 
      
      <div className='slrspot__review-studiesSearch-import'>
        <h3>Import references</h3>
        <input type='file' onChange={ handleOnFileChange }></input>
        <button onClick={ uploadFile }>Import</button>
      </div>

      <div className='slrspot__review-studiesSearch-imports-list'>
        <h3>Import history</h3>
        <Table>
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
        </Table>
        <button onClick={ () => navigate("/reviews/" + reviewId + "/studies/display") }>Show all studies</button>
      </div>
    </div>
  )
}

export default StudiesSearch