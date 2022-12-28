import React, { useRef, useState } from 'react'
import axiosInstance from '../../../services/api';
import FieldItem from './FieldItem';
import { STUDY_STATUSES } from '../../../constants/studyStatuses';
import { DropdownSelect } from '../../../components';
import { useParams } from 'react-router-dom';
import FileSaver from 'file-saver';
import './results.css';


const ExportStudies = () => {
  const { reviewId } = useParams();
  const [selected, setSelected] = useState();
  const [selectedFormat, setSelectedFormat] = useState();
  const [showError, setShowError] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [studiesCount, setStudiesCount] = useState();

  function getFilenameFormat() {
    if (selectedFormat === "CSV") {
      return '.csv';
    } else if (selectedFormat === "BIB") {
      return '.bib';
    } else {
      return '';
    }
  }

  const handleExport = () => {
    if (!selected) {
      setShowError(true);
      setErrorMsg('Select studies status to export');
    } else if (!selectedFormat) {
      setShowError(true);
      setErrorMsg('Select file format to export');
    } else if (studiesCount === 0) {
      setShowError(true);
      setErrorMsg('No studies available to export');
    } else {
      axiosInstance.get('studies/' + selected + "/" + selectedFormat, { params: {
        reviewId
      }})
      .then(response => {
        const headers = response.headers;
        const csvData = response.data;
        const blob = new Blob([csvData], { type: headers['Content-Type'] });
        FileSaver.saveAs(blob, "studies" + getFilenameFormat());
      })
    }
  }

  const handleSelect = (event) => {
    setSelected(event);
    var status = event;
    axiosInstance.get("/studies/count", { params: {
      reviewId, status
    }})
    .then((response) => {
      setStudiesCount(response.data)
    })
    .catch(() => {
    });
  }

  const handleSelectFormat = (event) => {
    setSelectedFormat(event);
  }

  return (
    <div className='slrspot__extractData'>
      <h2>EXPORT STUDIES</h2>
      { showError && <p className='slrspot__input-error' style={{ marginBottom: '5px' }}>{errorMsg}</p>}
      <div className='slrspot__exportStudies-dropdown'>
        <DropdownSelect 
          title='Select status'
          options={ STUDY_STATUSES } 
          onSelect={ handleSelect } 
          value={ selected } />
      </div>
      <div className='slrspot__exportStudies-dropdown'>
        <DropdownSelect 
          title='Select format'
          options={ ['CSV', 'BIB'] } 
          onSelect={ handleSelectFormat } 
          value={ selectedFormat } />
      </div>

      { selected && 
        <div>
          <label>Available { selected } studies: { studiesCount }</label>
        </div>
      }

      <div className='slrspot__extractData-download' style={{ marginTop: '10px' }}>
        <button onClick={ handleExport }>Export data</button>
      </div>
    </div>
  )
}

export default ExportStudies