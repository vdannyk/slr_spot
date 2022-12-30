import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { useParams } from 'react-router-dom';
import Check from 'react-bootstrap/FormCheck';
import { ContentPopup } from '../../../components'
import './results.css';
import ExtractData from './ExtractData';
import GenerateRaport from './GenerateRaport';
import ExportStudies from './ExportStudies';
import ResultStudy from './ResultStudy';
import { OWNER, MEMBER, COOWNER } from '../../../constants/roles';

const Results = (props) => {
  const [includedStudies, setIncludedStudies] = useState([]);
  const [selected, setSelected] = useState([]);
  const [showExtractMenu, setShowExtractMenu] = useState(false);
  const [showExportMenu, setShowExportMenu] = useState(false);
  const [showRaportMenu, setShowRaportMenu] = useState(false);
  const [reviewTags, setReviewTags] = useState([]);
  const { reviewId } = useParams();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  useEffect(() => {
    axiosInstance.get("/studies/included", { params: {
      reviewId
    }})
    .then((response) => {
      console.log(response.data);
      setIncludedStudies(response.data);
    });
  }, []);

  useEffect(() => {
    axiosInstance.get("/tags", { params: {
      reviewId
    }})
    .then((response) => {
      setReviewTags(response.data)
    })
    .catch(() => {
    });
  }, []);

  const handleSelectAll = (event) => {
    if (event.target.checked) {
      setSelected(includedStudies);
    } else {
      setSelected([]);
    }
  }

  const handleSelect = (study) => {
    if (selected.includes(study)) {
      setSelected(selected.filter(item => item.id !== study.id));
    } else {
      setSelected(oldArray => [...oldArray, study]);
    }
  }

  return (
    <div className='slrspot__review-results'>
      <div className='slrspot__review-header'>
        <h1>review results</h1>
      </div>
      
      <div className='slrspot__review-results-options'>
        <div className='slrspot__studiesView-container'>

          <div className='slrspot__studiesView-search'>
            <div className='slrspot__screening-options-container-checks'>
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>Title</label>
              </div>
              {/* <a>switch to review mode</a> */}
              <div className='slrspot__screening-options-check'>
                <Check />
                <label>authors</label>
              </div>
            </div>
            <div className='slrspot__screening-options-search'>
              <label>Search</label>
              <input></input>
            </div>
          </div>
        </div>
        { allowChanges && 
          <div className='slrspot__review-results-options-container'>
            <button onClick={ () => setShowExtractMenu(true) }>extract data</button>
            <button onClick={ () => setShowExportMenu(true) }>export studies</button>
            <button onClick={ () => setShowRaportMenu(true) }>generate report</button>
          </div>
        }
      </div>

      <div className='slrspot__review-results-list'>
        <div className='slrspot__review-list-selectAll'>
          <Check 
            type='checkbox'
            checked={ selected.length === includedStudies.length } 
            onChange={ handleSelectAll }/>
            <p>Select all</p>
        </div>
        { includedStudies.map(study => (
          <ResultStudy 
            study={ study } 
            selected={ selected }
            handleSelect= { () => handleSelect(study) }
            allowChanges={ allowChanges } 
            reviewTags={ reviewTags }/>
        ))}
      </div>
      { showExtractMenu && <ContentPopup content={<ExtractData selectedStudies={selected} />} triggerExit={ () => setShowExtractMenu(false)} /> }
      { showExportMenu && <ContentPopup content={<ExportStudies />} triggerExit={ () => setShowExportMenu(false)} /> }
      { showRaportMenu && <ContentPopup content={<GenerateRaport />} triggerExit={ () => setShowRaportMenu(false)} /> }
    </div>
  )
}

export default Results