import React, { useState } from 'react';
import axiosInstance from '../../../../services/api';

const StudiesSearch = () => {
  const [selectedFiles, setSelectedFiles] = useState(undefined);
  const [currentFile, setCurrentFile] = useState(undefined);

  const selectFile = (event) => {
    setSelectedFiles(event.target.files);
  };

  const uploadFile = (event) => {
    console.log(event.target.files);
    let data = new FormData();
    data.append("file", event.target.files[0]);

    return axiosInstance.post("/studies/save-from-csv", data)
    .then(function (response) {
      console.log(response);
    })
  }

  return (
    <div className='slrspot__review-studiesSearch'>
      <div className='slrspot__review-header'>
        <h1>Studies search</h1>
      </div >
      <input type='file' onChange={uploadFile}></input>
    </div>
  )
}

export default StudiesSearch