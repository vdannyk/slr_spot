import React, { useState, useEffect } from 'react';
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../../constants/tabs';
import './screeningStudy.css';
import StudyTags from './studyTags/StudyTags';
import { useNavigate, useParams } from "react-router-dom";
import ContentPopup from '../../popups/contentPopup/ContentPopup';
import StudyHistory from './studyHistory/StudyHistory';
import StudyDiscussion from './studyDiscussion/StudyDiscussion';
import { useSelector } from 'react-redux';
import axiosInstance from '../../../services/api';
import FullTextField from './fullTextField/FullTextField';
import KeyWordHighlight from '../../keyWordHighlight/KeyWordHighlight';
import { INCLUSION_TYPE, EXCLUSION_TYPE } from '../criteria/CriteriaTypes';


const ScreeningStudy = ({ study, isShowAbstracts, triggerVote, triggerRefresh, 
                          tab, isFullText, reviewTags, allowChanges, showHighlights, highlights }) => {
  const [showAbstract, setShowAbstract] = useState(isShowAbstracts);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);

  const inclusionHighlights = highlights.filter((highlight) => highlight.type === INCLUSION_TYPE)
    .map((highlight) => highlight.name);

  const exclusionHighlights = highlights.filter((highlight) => highlight.type === EXCLUSION_TYPE)
    .map((highlight) => highlight.name);

  useEffect(() => {
    if (showAbstract !== isShowAbstracts) {
      setShowAbstract(!showAbstract)
    }
  }, [isShowAbstracts]);

  const handleVote = (vote) => {
    axiosInstance.post("/studies/" + study.id + "/screening_decisions", {
      reviewId: reviewId,
      userId: currentUser.id,
      decision: vote
    })
    .then(() => {
      if (tab === AWAITING ) {
        triggerRefresh();
      } else {
        triggerVote(study.id);
      }
    })
    .catch(() => {
    });
  }

  const handleRestore = () => {
    axiosInstance.put("/studies/" + study.id + "/restore")
    .then(() => {
      triggerVote(study.id);
    })
    .catch(() => {
    });
  }

  const handleDuplicate = () => {
    axiosInstance.put("/studies/" + study.id + "/duplicate")
    .then(() => {
      triggerVote(study.id);
    })
    .catch(() => {
    });
  }

  const handleShowAbstract = () => {
    setShowAbstract(!showAbstract);
  }

  const ConflictedOptions = () => {
    // to do -> check role in useState
    const [showOptions, setShowOptions] = useState(false);

    return (
      <div className='slrspot__screeningStudy-decision'>
        { showOptions 
        ? <>
            <button 
              className='slrspot__screeningStudy-decision-button'
              onClick={ () => handleVote('EXCLUDE')}>
                exclude
            </button>
            <button 
              className='slrspot__screeningStudy-decision-button' 
              onClick={ () => handleVote('INCLUDE')}>
                include
            </button>
          </>
        : <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={() => setShowOptions(true)}>
              change decision
          </button>
        }
      </div>
    )
  }

  const AwaitingOptions = () => {
    const [showOptions, setShowOptions] = useState(false);
    const [currentVote, setCurrentVote] = useState('');

    useEffect(() => {
      var userId = currentUser.id;
      axiosInstance.get("/studies/" + study.id + "/screening_decision", { params: {
        userId
      }})
      .then((response) => {
        setCurrentVote(response.data);
      })
    }, []);

    return (
      <div className='slrspot__screeningStudy-awaiting'>
        <div className='slrspot__screeningStudy-votes'>
          <p>current vote: <span><b>{currentVote}</b></span></p>
        </div>
        { showOptions 
        ? <div className='slrspot__screeningStudy-awaiting-decision'>
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
        : <button 
            className='slrspot__screeningStudy-decision-button' 
            onClick={() => setShowOptions(true)}>
              change decision
          </button>
        }
      </div>
    )
  }

  function tabSpecificContent() {
    if (tab === TO_BE_REVIEWED) {
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
    } else if (tab === CONFLICTED) {
      return (
        <ConflictedOptions />
      )
    } else if (tab === AWAITING) {
      return (
        <AwaitingOptions />
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

  return (
    <div className='slrspot__screeningStudy'>
      { showHighlights 
        ? <h3>
            <KeyWordHighlight 
              text={ study.title } 
              inclusionWords={ inclusionHighlights } 
              exclusionWords={ exclusionHighlights }/>
          </h3> 
        : <h3>{ study.title }</h3> }

      <button onClick={handleShowAbstract} className='slrspot__screeningStudy-showAbstract-button'>
        { showAbstract ? 'hide abstract' : 'show abstract'}
      </button>

      {showHighlights ? (
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

      <FullTextField 
        isFullText={ isFullText } 
        study={ study }
        allowChanges={ allowChanges } />
      
      <StudyTags 
        studyId={ study.id } 
        reviewTags={ reviewTags } 
        allowChanges={ allowChanges }/>
        
      <div className='slrspot__screeningStudy-options'>
        <button onClick={ () => setShowDiscussion(true)}>discussion</button>
        <button onClick={ () => setShowHistory(true) }>history</button>
        { !(tab === EXCLUDED) && allowChanges && <button onClick={ handleDuplicate }>mark as duplicate</button>}
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

export default ScreeningStudy