import React, { useRef, useState } from 'react'
import axiosInstance from '../../../services/api';
import FieldItem from './FieldItem';
import { STUDY_STATUSES } from '../../../constants/studyStatuses';
import { DropdownSelect } from '../../../components';
import { useParams } from 'react-router-dom';
import './results.css';


const ExportStudies = (props) => {
  const fileUrl = useRef(null);
  const { reviewId } = useParams();
  const [selected, setSelected] = useState();
  const [showError, setShowError] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');
  const [studiesCount, setStudiesCount] = useState(0);

  const handleExport = () => {
    if (!selected) {
      setShowError(true);
      setErrorMsg('No studies selected');
    } else {
      setShowError(false);
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

  return (
    <div className='slrspot__extractData'>
      <h2>EXPORT STUDIES</h2>
      { showError && <p className='slrspot__input-error' style={{ marginBottom: '5px' }}>{errorMsg}</p>}
      <div className='slrspot__exportStudies-dropdown'>
        <DropdownSelect 
          title='Select studies type'
          options={ STUDY_STATUSES } 
          onSelect={ handleSelect } 
          value={ selected } />
      </div>

      { selected && 
        <div>
          <label>Available { selected } studies: { studiesCount }</label>
        </div>
      }

      <div className='slrspot__extractData-download' style={{ marginTop: '10px' }}>
        <a ref={fileUrl} style={{ display: 'none' }}/>
        <button onClick={ handleExport }>Export data</button>
      </div>
    </div>
  )
}

export default ExportStudies