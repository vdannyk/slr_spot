import React, { useRef, useState } from 'react'
import axiosInstance from '../../../services/api';
import FieldItem from './FieldItem';
import './results.css';

const ExtractData = (props) => {
  const fileUrl = useRef(null);
  const [selected, setSelected] = useState([]);
  const [showError, setShowError] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  const handleExtraction = () => {
    if (props.selectedStudies.length === 0) {
      setShowError(true);
      setErrorMsg('No studies selected');
    } else if (selected.length === 0) {
      setShowError(true);
      setErrorMsg('No fields selected');
    } else {
      axiosInstance.post('data_extraction', { 
        fields: selected,
        studies: props.selectedStudies
      })
      .then(response => {
        const csvData = response.data;
        const blob = new Blob([csvData], { type: 'text/csv' });
        fileUrl.current.href = URL.createObjectURL(blob);
        fileUrl.current.click()
      })
    }
  }

  return (
    <div className='slrspot__extractData'>
      <h2>Select fields to extract</h2>
      { showError && <p className='slrspot__input-error' style={{ marginBottom: '5px' }}>{errorMsg}</p>}
      <FieldItem name="authors" value='AUTHORS' selected={selected} setSelected={setSelected} />
      <FieldItem name="title" value='TITLE' selected={selected} setSelected={setSelected} />
      <FieldItem name="journal" value='JOURNAL' selected={selected} setSelected={setSelected} />
      <FieldItem name="publication year" value='PUBLICATIONYEAR' selected={selected} setSelected={setSelected} />
      <FieldItem name="abstract" value='ABSTRACT' selected={selected} setSelected={setSelected} />
      <FieldItem name="doi" value='DOI' selected={selected} setSelected={setSelected} />

      <div className='slrspot__extractData-download'>
        <a ref={fileUrl} style={{ display: 'none' }}/>
        <button onClick={ handleExtraction }>Extract data</button>
      </div>
    </div>
  )
}

export default ExtractData