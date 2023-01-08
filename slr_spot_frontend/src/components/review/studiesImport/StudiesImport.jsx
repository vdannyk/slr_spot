import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import DropdownSelect from '../../dropdownSelect/DropdownSelect';
import { useParams } from "react-router-dom";
import Check from 'react-bootstrap/FormCheck';
import { DOI, BASIC_INFORMATION } from '../../../constants/deduplicationTypes';
import { useSelector } from 'react-redux';
import './studiesImport.css';
import Helper from '../../helper/Helper';


const StudiesImport = (props) => {
  var options = ["OTHER", "SCOPUS", "IEEE", "WEB OF SCIENCE", "SPRINGERLINK", "ACM", "SCIENCE DIRECT"]
  const [currentFile, setCurrentFile] = useState();
  const [selectedFile, setSelectedFile] = useState('No file selected')
  const [searchValue, setSearchValue] = useState('');
  const [additionalInfo, setAdditionalInfo] = useState('');
  const [source, setSource] = useState('');
  const [otherSource, setOtherSource] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const [selectDeduplicationType, setSelectDeduplicationType] = useState(DOI);
  const [checkDoi, setCheckDoi] = useState(true);
  const [checkOther, setCheckOther] = useState(false);

  const uploadFile = () => {
    let data = new FormData();
    if (!currentFile) {
      setErrorMsg("File not selected");
      return;
    }
    data.append("file", currentFile[0]);
    data.append("reviewId", reviewId);
    data.append("searchValue", searchValue);
    if (source === "OTHER") {
      if (otherSource.trim().length === 0) {
        setErrorMsg("Source not defined");
        return;
      }
      data.append("source", otherSource);
    } else if (source.length === 0) {
      setErrorMsg("Source not selected");
      return;
    } else {
      data.append("source", source)
    }
    data.append("additionalInformations", additionalInfo);
    data.append("userId", currentUser.id);
    data.append("deduplicationType", selectDeduplicationType);

    axiosInstance.post("/imports", data)
    .then(() => {
      window.location.reload()
    })
    .catch((error) => {
      const message =
        (error.response &&
        error.response.data &&
        error.response.data.message);
      setErrorMsg(message);
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

  const handleCheck = () => {
    if (checkOther) {
      setCheckOther(false);
      setSelectDeduplicationType(DOI)
      setCheckDoi(true);
    } else {
      setCheckOther(true);
      setSelectDeduplicationType(BASIC_INFORMATION)
      setCheckDoi(false);
    }
  }

  const headers = () => {
    if (source === "OTHER") {
      return (
        <div>
          <b><label>Supported csv headers:</label></b><span style={{ marginLeft: '2px', color: 'green' }}>(Required)</span><br />
          <span>
            <span style={{ color: 'green' }}>title</span>, 
            <span style={{ color: 'green' }}>authors</span>, 
            journal title, 
            <span style={{ color: 'green' }}>publication year</span>, 
            volume, doi, url, abstract, issn, language</span>
        </div>
      )
    } else if (source === "SCOPUS") {
      return (
        <div>
          <b><label>Supported csv headers:</label></b><span style={{ marginLeft: '2px', color: 'green' }}>(Required)</span><br />
          <span>
            <span style={{ color: 'green' }}>Title</span>, 
            <span style={{ color: 'green' }}>Authors</span>, 
            Source title, 
            <span style={{ color: 'green' }}>Year</span>, 
            Volume, DOI, Link, Abstract, ISSN, Language of Original Document</span>
        </div>
      )
    } else if (source === "IEEE") {
      return (
        <div>
          <b><label>Supported csv headers:</label></b><span style={{ marginLeft: '2px', color: 'green' }}>(Required)</span><br />
          <span>
            <span style={{ color: 'green' }}>Document Title</span>, 
            <span style={{ color: 'green' }}>Authors</span>, 
            Publication Title, 
            <span style={{ color: 'green' }}>Publication Year</span>, 
            Volume, DOI, Abstract, ISSN</span>
        </div>
      )
    } else if (source === "SPRINGERLINK") {
      return (
        <div>
          <b><label>Supported csv headers:</label></b><span style={{ marginLeft: '2px', color: 'green' }}>(Required)</span><br />
          <span>
            <span style={{ color: 'green' }}>Item Title</span>, 
            <span style={{ color: 'green' }}>Authors</span>, 
            Publication Title, 
            <span style={{ color: 'green' }}>Publication Year</span>, 
            Journal Volume, Item DOI, URL</span>
        </div>
      )
    } else if (source === "WEB OF SCIENCE") {
      return (
        <div>
          <b><label>Supported csv headers:</label></b><span style={{ marginLeft: '2px', color: 'green' }}>(Required)</span><br />
          <span>
            <span style={{ color: 'green' }}>Article Title</span>, 
            <span style={{ color: 'green' }}>Authors</span>, 
            Source Title, 
            <span style={{ color: 'green' }}>Publication Year</span>, 
            Volume, DOI, DOI Link, Language, Abstract, ISSN</span>
        </div>
      )
    } else {
      return (
        <div>
          <b><label>Supported csv headers:</label></b><span style={{ marginLeft: '2px', color: 'green' }}>(Required)</span><br />
          <span>
            <span style={{ color: 'green' }}>title</span>, 
            <span style={{ color: 'green' }}>authors</span>, 
            journal title, 
            <span style={{ color: 'green' }}>publication year</span>, 
            volume, doi, url, abstract, issn, language</span>
        </div>
      )
    }
  }

  return (
    <div className='slrspot__studiesImport'>

      <div className='slrspot__studiesImport-container'>

        <div className='slrspot__studiesImport-container-fields'>

          <h1>IMPORT STUDIES</h1>

          <Helper content={ headers() } show={true}/>

          { errorMsg && <p className='slrspot__input-error' style={{ marginTop: '5px' }}>{ errorMsg }</p> }
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

          <label className='slrspot__deduplication-header'>Deduplicate using:</label>
          <div className='slrspot__deduplication-options'>
            <div className='slrspot__deduplication-option'>
              <Check 
                onChange={ handleCheck } 
                checked={ checkDoi } />
              <span>{ DOI }</span>
            </div>
            <div className='slrspot__deduplication-option'>
              <Check
                onChange={ handleCheck } 
                checked={ checkOther } />
              <span>TITLE, AUTHORS, YEAR</span>
            </div>
          </div>

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