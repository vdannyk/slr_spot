import React from 'react'
import { useState } from 'react';
import { DownloadButton, Helper } from '../../../components';
import axiosInstance from '../../../services/api';

const ExtractData = (props) => {
  const fields = ["TITLE"];
  const [fileUrl, setFileUrl] = useState();

  const handleExtraction = () => {
    axiosInstance.post('studies/extract_data', { 
      fields: fields,
      studies: props.selectedStudies
    })
    .then(response => {
      const csvData = response.data;
      const blob = new Blob([csvData], { type: 'text/csv' });
      setFileUrl(URL.createObjectURL(blob));
    })
  }

  return (
    <div className='slrspot__extractData'>
      <p>Select fields which you want to extract</p>
      <p>title</p>
      <p>costam</p>
      <p>abstract</p>
      <DownloadButton name='Extract data' fileUrl={fileUrl} handleExtraction={handleExtraction} />
    </div>
  )
}

export default ExtractData