import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../services/api';
import { useParams } from 'react-router-dom';
import { OWNER, COOWNER } from '../../../../constants/roles';
import './duplicates.css';

const Duplicates = (props) => {
  const [duplicates, setDuplicates] = useState([]);
  const { reviewId } = useParams();
  var allowChanges = props.userRole && [OWNER, COOWNER].includes(props.userRole);

  useEffect(() => {
    axiosInstance.get("/studies/duplicates", { params: {
      reviewId
    }})
    .then((response) => {
      console.log(response.data);
      setDuplicates(response.data);
    });
  }, []);

  const handleStudiesUpdate = (id) => {
    setDuplicates(duplicates.filter(study => study.id !== id));
  }

  const StudyDuplicate = ({study, allowChanges}) => {
    const [showAbstract, setShowAbstract] = useState(false);

    const handleShowAbstract = () => {
      setShowAbstract(!showAbstract);
    }

    const handleRestore = () => {
      var status = 'TITLE_ABSTRACT';
      axiosInstance.put("/studies/" + study.id + "/restore", null, { params: {
        status
      }})
      .then(() => {
        handleStudiesUpdate(study.id);
      })
      .catch(() => {
      });
    }

    const handleDelete = () => {
      axiosInstance.delete("/studies/" + study.id)
      .then(() => {
        handleStudiesUpdate(study.id);
      })
      .catch(() => {
      });
    }

    return (
      <div className='slrspot__screeningStudy'>
        <h3>{ study.title }</h3>
        <button onClick={handleShowAbstract} className='slrspot__screeningStudy-showAbstract-button'>
          { showAbstract ? 'hide abstract' : 'show abstract'}
        </button>
        { showAbstract && <p><label>abstract:</label> { study.documentAbstract }</p> }
        <p><label>authors:</label> { study.authors }</p>
        <p><label>journal:</label> { study.journalTitle }</p>
        <p><label>publicationYear:</label> { study.publicationYear }</p>
        <p><label>doi:</label> { study.doi }</p>
        <p><label>URL:</label> { study.url }</p>
        <p><label>language:</label> { study.language }</p>
          
        { allowChanges && 
          <div className='slrspot__screeningStudy-decision'>
            <button 
              className='slrspot__screeningStudy-decision-button'
              onClick={ handleRestore }>
                RESTORE TO SCREENING
            </button>
            <button 
              className='slrspot__screeningStudy-decision-button' 
              onClick={ handleDelete }>
                REMOVE
            </button>
          </div>
        }
      </div>
    )
  }

  return (
    <div className='slrspot__review-duplicates'>
      <div className='slrspot__review-studiesDisplay-header'>
        <h1>Marked duplicates</h1>
      </div>
      <div className='slrspot__review-duplicates-container'>
        { duplicates.map(study => (
          <StudyDuplicate 
            study={ study } 
            allowChanges={ allowChanges }
          />
        ))}
      </div>
    </div>
  )
}

export default Duplicates