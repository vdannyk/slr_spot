import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../services/api';
import { useParams } from 'react-router-dom';
import { OWNER, COOWNER } from '../../../../constants/roles';
import ReactPaginate from 'react-paginate';
import { PageChanger } from '../../../../components';
import './duplicates.css';

const Duplicates = (props) => {
  const [duplicates, setDuplicates] = useState([]);
  const { reviewId } = useParams();
  var allowChanges = props.userRole && [OWNER, COOWNER].includes(props.userRole);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [pageCount, setPageCount] = useState(0);
  const [searchPerformed, setSearchPerformed] = useState(false);

  useEffect(() => {
    getDuplicates(0, pageSize);
  }, []);

  function getDuplicates(page, size) {
    axiosInstance.get("/studies/duplicates", { params: {
      reviewId, page, size
    }})
    .then((response) => {
      setDuplicates(response.data.content);
      setPageCount(response.data.totalPages);
      setCurrentPage(response.data.number);
    });
  }

  const handleStudiesUpdate = (id) => {
    setDuplicates(duplicates.filter(study => study.id !== id));
  }

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    setCurrentPage(page);
  }

  useEffect(() => {
    getDuplicates(currentPage, pageSize);
  }, [currentPage, pageSize]);

  const StudyDuplicate = ({study, allowChanges}) => {
    const [showAbstract, setShowAbstract] = useState(false);

    const handleShowAbstract = () => {
      setShowAbstract(!showAbstract);
    }

    const handleRestore = () => {
      axiosInstance.put("/studies/" + study.id + "/restore")
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

  if (duplicates.length === 0) {
    return (
      <div style={{ display: 'flex', flex:'1', alignItems: 'center', justifyContent: 'center'}}>
        <h1 style={{ textTransform: 'uppercase' }}>no duplicates marked</h1>
      </div>
    )
  }

  return (
    <div className='slrspot__review-duplicates'>
      <div className='slrspot__review-studiesDisplay-header'>
        <h1>Marked duplicates</h1>
      </div>

      <div style={{ textAlign: 'right' }}>
        { duplicates.length > 0 &&
          <PageChanger 
            defaultSelected={pageSize}
            options={[5,10,25]}
            changePageSize={setPageSize}
          />
        }
      </div>

      <div className='slrspot__review-duplicates-container'>
        { duplicates.map(study => (
          <StudyDuplicate 
            study={ study } 
            allowChanges={ allowChanges }
          />
        ))}
      </div>

      { duplicates.length > 0 && pageCount > 1 &&
        <ReactPaginate
          pageCount={pageCount}
          pageRangeDisplayed={5}
          marginPagesDisplayed={2}
          onPageChange={handlePageChange}
          forcePage={currentPage}
          containerClassName="slrspot__folder-pagination"
          activeClassName="slrspot__folder-pagination-active"
        />
      }
    </div>
  )
}

export default Duplicates