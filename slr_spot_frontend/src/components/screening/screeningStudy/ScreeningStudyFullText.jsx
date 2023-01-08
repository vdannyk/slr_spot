import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../services/api';
import { INCLUSION_TYPE, EXCLUSION_TYPE } from '../criteria/CriteriaTypes';
import KeyWordHighlight from '../../keyWordHighlight/KeyWordHighlight';
import StudyTags from './studyTags/StudyTags';
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../../constants/tabs';
import ContentPopup from '../../popups/contentPopup/ContentPopup';
import StudyHistory from './studyHistory/StudyHistory';
import StudyDiscussion from './studyDiscussion/StudyDiscussion';
import AwaitingOptions from './tabSpecificOptions/AwaitingOptions';
import ConflictedOptions from './tabSpecificOptions/ConflictedOptions';
import { useSelector } from 'react-redux';
import { FULL_TEXT } from '../../../constants/studyStages';
import { useNavigate } from 'react-router-dom';


const ScreeningStudyFullText = (props) => {
  const navigate = useNavigate();
  const [study, setStudy] = useState();
  const [reviewTags, setReviewTags] = useState([]);
  const [showAbstract, setShowAbstract] = useState(false);

  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);

  const { user: currentUser } = useSelector((state) => state.auth);

  var allowChanges = true;

  const inclusionHighlights = props.highlights.filter((highlight) => highlight.type === INCLUSION_TYPE)
    .map((highlight) => highlight.name);

  const exclusionHighlights = props.highlights.filter((highlight) => highlight.type === EXCLUSION_TYPE)
    .map((highlight) => highlight.name);

  useEffect(() => {
    axiosInstance.get('/studies/' + props.studyId)
    .then((response) => {
      setStudy(response.data);
    })

    var reviewId = props.reviewId;
    axiosInstance.get("/tags", { params: {
      reviewId
    }})
    .then((response) => {
      setReviewTags(response.data);
    })
    .catch((error) => {
    });
  }, []);

  const handleDuplicate = () => {
    axiosInstance.put("/studies/" + props.studyId + "/duplicate")
    .then(() => {
      navigate('/reviews/' + props.reviewId + '/studies/duplicates');
    })
    .catch(() => {
    });
  }

  const handleVote = (vote) => {
    axiosInstance.post("/studies/" + props.studyId + "/screening_decisions", {
      reviewId: props.reviewId,
      userId: currentUser.id,
      decision: vote,
      stage: FULL_TEXT
    })
    .then(() => {
      navigate('/reviews/' + props.reviewId + '/screening/full-text');
    })
    .catch(() => {
    });
  }

  const handleRestore = () => {
    axiosInstance.put("/studies/" + props.studyId + "/restore")
    .then(() => {
      navigate('/reviews/' + props.reviewId + '/screening/full-text');
    })
    .catch(() => {
    });
  }

  function tabSpecificContent() {
    if (props.tab === TO_BE_REVIEWED) {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button 
            className='slrspot__screeningStudy-decision-button'
            onClick={ () => handleVote('EXCLUDE')}>
              exclude
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => handleVote('UNCLEAR')}>
              unclear
          </button>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => handleVote('INCLUDE')}>
              include
          </button>
        </div>
      )
    } else if (props.tab === CONFLICTED) {
      return (
        <ConflictedOptions 
          handleVote={ handleVote }
        />
      )
    } else if (props.tab === AWAITING) {
      return (
        <AwaitingOptions 
          handleVote={ handleVote }
          study={ study }
        />
      )
    } else {
      return (
        <div className='slrspot__screeningStudy-decision'>
          <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={ () => handleRestore() }>
              screen again
          </button>
        </div>
      )
    }
  }

  return study && (
    <div className='slrspot__screeningStudy'>

      <div className='slrspot__screeningStudy-state'>
        <h3>Current state: </h3>
        <span>{ props.tab }</span> 
      </div>

      { props.showHighlights 
        ? <h3>
            <KeyWordHighlight 
              text={ study.title } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </h3> 
        : <h3>{ study.title }</h3> 
      }

      <button onClick={ () => setShowAbstract(!showAbstract) } className='slrspot__screeningStudy-showAbstract-button'>
        { showAbstract ? 'hide abstract' : 'show abstract'}
      </button>

      {props.showHighlights ? (
        <>
          { showAbstract && 
            <p>
              <label>abstract:</label>
              <KeyWordHighlight 
                text={ study.documentAbstract }
                inclusionWords={ inclusionHighlights } 
                exclusionWords={ exclusionHighlights }/>
            </p> 
          }
          <p>
            <label>authors:</label><span> </span>
            <KeyWordHighlight 
              text={ study.authors } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </p>
          <p>
            <label>journal:</label><span> </span>
            <KeyWordHighlight 
              text={ study.journalTitle } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </p>
          <p>
            <label>publicationYear:</label><span> </span> 
            <KeyWordHighlight 
              text={ study.publicationYear.toString() } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </p>
          <p>
            <label>doi:</label><span> </span>
            <KeyWordHighlight 
              text={ study.doi } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </p>
          <p>
            <label>URL:</label><span> </span> 
            <KeyWordHighlight 
              text={ study.url } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </p>
          <p>
            <label>language:</label><span> </span> 
            <KeyWordHighlight 
              text={ study.language } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </p>
        </>
      ) : (
        <>
          { showAbstract && <p><label>abstract:</label> { study.documentAbstract }</p> }
          <p><label>authors:</label> { study.authors }</p>
          <p><label>journal:</label> { study.journalTitle }</p>
          <p><label>publicationYear:</label> { study.publicationYear }</p>
          <p><label>doi:</label> { study.doi }</p>
          <p><label>URL:</label> { study.url }</p>
          <p><label>language:</label> { study.language }</p>
        </>
      )}

      <StudyTags 
        studyId={ study.id } 
        reviewTags={ reviewTags } 
        allowChanges={ true }/>

      <div className='slrspot__screeningStudy-options'>
        <button onClick={ () => setShowDiscussion(true)}>discussion</button>
        <button onClick={ () => setShowHistory(true) }>history</button>
        { !(props.tab === EXCLUDED) && allowChanges && <button onClick={ handleDuplicate }>mark as duplicate</button>}
      </div>

      { allowChanges && tabSpecificContent() }
      { showDiscussion && 
        <ContentPopup 
          content={<StudyDiscussion 
                      studyId={ study.id } 
                      allowChanges={ allowChanges } />} 
          triggerExit={() => setShowDiscussion(false)}/> }
      { showHistory && 
        <ContentPopup 
          content={<StudyHistory studyId={ study.id } />} 
          triggerExit={() => setShowHistory(false)} /> }

    </div>
  )
}

export default ScreeningStudyFullText