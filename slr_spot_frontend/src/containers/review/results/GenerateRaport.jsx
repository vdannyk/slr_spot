import React, { useState } from 'react'
import axiosInstance from '../../../services/api';
import { useParams } from 'react-router-dom';
import FileSaver from 'file-saver';
import './results.css';

const GenerateRaport = () => {
  const { reviewId } = useParams();
  const [showError, setShowError] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');


  const handleGenerate = () => {
    axiosInstance.get('/reviews/' + reviewId + "/report", { responseType: 'blob' })
    .then(response => {
      const headers = response.headers;
      const blob = new Blob([response.data], { type: 'application/pdf' });
      FileSaver.saveAs(blob, "report.pdf");
    })
  }

  return (
    <div className='slrspot__extractData'>
      <h2>GENERATE REVIEW REPORT</h2>
      { showError && <p className='slrspot__input-error' style={{ marginBottom: '5px' }}>{errorMsg}</p>}

      <div className='slrspot__extractData-download' style={{ marginTop: '10px' }}>
        <button onClick={ handleGenerate }>Generate report</button>
      </div>

    </div>
  )
}

export default GenerateRaport