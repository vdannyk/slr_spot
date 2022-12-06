import React, { useState } from 'react';
import axiosInstance from '../../../services/api';
import DropdownSelect from '../../dropdownSelect/DropdownSelect';
import { useNavigate, useParams } from "react-router-dom";
import './studiesImport.css';


const StudiesImport = (props) => {
  var options = ["OTHER", "SCOPUS", "IEEE", "WEB OF SCIENCE"]
  const [currentFile, setCurrentFile] = useState();
  const [selectedFile, setSelectedFile] = useState('No file selected')
  const [searchValue, setSearchValue] = useState('');
  const [additionalInfo, setAdditionalInfo] = useState('');
  const [source, setSource] = useState('');
  const [otherSource, setOtherSource] = useState('');
  const { reviewId } = useParams();

  const uploadFile = () => {
    let data = new FormData();
    data.append("file", currentFile[0]);
    data.append("reviewId", reviewId);
    data.append("searchValue", searchValue);
    if (source === "OTHER") {
      data.append("source", otherSource);
    } else {
      data.append("source", source)
    }
    data.append("additionalInformations", additionalInfo);

    axiosInstance.post("/imports", data)
    .then(function (response) {
      console.log(response);
    })
    .catch((error) => {
    });
  }

  const handleOnFileChange = (event) => {
    setCurrentFile(event.target.files);
    setSelectedFile(event.target.files[0].name)
  }

  const handleSearchValueChange = (event) => {
    setSearchValue(event.target.value);
  }

  const handleOtherSourceChange = (event) => {
    setOtherSource(event.target.value);
  }

  const handleAdditionalInfoChange = (event) => {
    setAdditionalInfo(event.target.value);
  }

  const handleSelect = (event) => {
    setSource(event);
  }

  return (
    <div className='slrspot__studiesImport'>
      <div className='slrspot__studiesImport-container'>
        <div className='slrspot__studiesImport-container-fields'>
          <h1>IMPORT STUDIES</h1>
          <p className='slrspot__input-error'>test error</p>
          <input 
            onChange={ handleSearchValueChange }
            placeholder='Search value' 
            name='searchValue' />

          <DropdownSelect
            value={ source }
            onSelect={ handleSelect }
            options={ options }
            title="Select source"
          />
          { source == "OTHER" && 
            <input 
              placeholder='Other source' 
              name='otherSource'
              onChange={ handleOtherSourceChange }/> }

          <textarea 
            className='slrspot__studiesImport-textarea'
            placeholder='Additional informations' 
            name='additionalInformation' 
            onChange={ handleAdditionalInfoChange } />

          <label className='custom-file-upload'>
            Select file
            <input type="file" onChange={ handleOnFileChange } />
          </label>
          <span> { selectedFile }</span>

        </div>
        <div className='slrspot__studiesImport-container-buttons'>
          <button className='slrspot__studiesImport-confirmButton' onClick={ uploadFile }>
            import
          </button>
          <button 
            className='slrspot__studiesImport-cancelButton' 
            onClick={() => props.triggerCancel(false)}>cancel</button>
        </div>
      </div>
    </div>
  )
}

export default StudiesImport