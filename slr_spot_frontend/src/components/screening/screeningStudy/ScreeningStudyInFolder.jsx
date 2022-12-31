import React, { useState, useEffect } from 'react';
import StudyHistory from './studyHistory/StudyHistory';
import StudyDiscussion from './studyDiscussion/StudyDiscussion';
import Check from 'react-bootstrap/FormCheck';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import axiosInstance from '../../../services/api';
import KeyWordHighlight from '../../keyWordHighlight/KeyWordHighlight';
import { EXCLUDED, TO_BE_REVIEWED, CONFLICTED, AWAITING } from '../../../constants/tabs';
import FullTextField from './fullTextField/FullTextField';
import StudyTags from './studyTags/StudyTags';
import { INCLUSION_TYPE, EXCLUSION_TYPE } from '../criteria/CriteriaTypes';
import { OWNER, MEMBER, COOWNER } from '../../../constants/roles';
import AwaitingOptions from './tabSpecificOptions/AwaitingOptions';
import ConflictedOptions from './tabSpecificOptions/ConflictedOptions';
import './screeningStudy.css';


const ScreeningStudyInFolder = (props) => {
  const [showAbstract, setShowAbstract] = useState(false);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const [showHistory, setShowHistory] = useState(false);
  const [showTeamHighlights, setShowTeamHighlights] = useState(false);
  const [showPersonalHighlights, setShowPersonalHighlights] = useState(false);
  const [showHighlights, setShowHighlights] = useState(showTeamHighlights || showPersonalHighlights);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);
  const [highlights, setHighlights] = useState([]);

  const inclusionHighlights = highlights.filter((highlight) => highlight.type === INCLUSION_TYPE)
    .map((highlight) => highlight.name);

  const exclusionHighlights = highlights.filter((highlight) => highlight.type === EXCLUSION_TYPE)
    .map((highlight) => highlight.name);

  const handleShowAbstract = () => {
    setShowAbstract(!showAbstract);
  }

  const handleDuplicate = () => {
    axiosInstance.put("/studies/" + props.study.id + "/duplicate")
    .then(() => {
      props.triggerVote(props.study.id);
    })
    .catch(() => {
    });
  }

  const handleVote = (vote) => {
    axiosInstance.post("/studies/" + props.study.id + "/screening_decisions", {
      reviewId: reviewId,
      userId: currentUser.id,
      decision: vote
    })
    .then(() => {
      // if (props.tab === AWAITING ) {
      //   // triggerRefresh();
      // } else {
        props.triggerVote(props.study.id);
      // }
    })
    .catch(() => {
    });
  }

  const handleRestore = () => {

  }

  const handleShowTeamHighlightsChange = () => {
    if (showTeamHighlights) {
      setShowTeamHighlights(false);
    } else {
      setShowTeamHighlights(true);
      setHighlights(props.teamHighlights);
      setShowPersonalHighlights(false);
    }
  }

  const handleShowPersonalHighlightsChange = () => {
    if (showPersonalHighlights) {
      setShowPersonalHighlights(false);
    } else {
      setShowPersonalHighlights(true);
      setHighlights(props.personalHighlights);
      setShowTeamHighlights(false);
    }
  }

  useEffect(() => {
    setShowHighlights(showTeamHighlights || showPersonalHighlights);
  }, [showTeamHighlights, showPersonalHighlights]);

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
          study={ props.study }
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

  if (showDiscussion) {
    return (
      <div>
        <StudyDiscussion 
          studyId={ props.study.id } 
          allowChanges={ allowChanges } />
        <button onClick={ () => setShowDiscussion(false) }>Back</button>
      </div>
    )
  }

  else if (showHistory) {
    return (
      <div>
        <StudyHistory studyId={ props.study.id } />
        <button onClick={ () => setShowHistory(false) }>Back</button>
      </div>
    )
  } 

  else {
    return (
      <div>
        { props.tab && 
          <div className='slrspot__screening-options'>
            <div className='slrspot__screening-options-container'>
              <div className='slrspot__screening-options-container-checks'>
                <div className='slrspot__screening-options-check'>
                  <Check 
                    onChange={ handleShowTeamHighlightsChange } 
                    checked={ showTeamHighlights } />
                  <label>team highlights</label>
                </div>
                <div className='slrspot__screening-options-check'>
                  <Check
                    onChange={ handleShowPersonalHighlightsChange } 
                    checked={ showPersonalHighlights } />
                  <label>personal highlights</label>
                </div>
              </div>
            </div>
          </div>
        }

        <div className='slrspot__screeningStudyInFolder'>

        { showHighlights 
          ? <h3>
              <KeyWordHighlight 
                text={ props.study.title } 
                inclusionWords={ inclusionHighlights } 
                exclusionWords={ exclusionHighlights }/>
            </h3> 
          : <h3>{ props.study.title }</h3> }

          <button onClick={handleShowAbstract} className='slrspot__screeningStudy-showAbstract-button'>
            { showAbstract ? 'hide abstract' : 'show abstract'}
          </button>
          
          {showHighlights ? (
            <>
              { showAbstract && 
                <p>
                  <label>abstract:</label>
                  <KeyWordHighlight 
                    text={ props.study.documentAbstract }
                    inclusionWords={ inclusionHighlights } 
                    exclusionWords={ exclusionHighlights }/>
                </p> 
              }
              <p>
                <label>authors:</label><span> </span>
                <KeyWordHighlight 
                  text={ props.study.authors } 
                  inclusionWords={ inclusionHighlights } 
                  exclusionWords={ exclusionHighlights }/>
              </p>
              <p>
                <label>journal:</label><span> </span>
                <KeyWordHighlight 
                  text={ props.study.journalTitle } 
                  inclusionWords={ inclusionHighlights } 
                  exclusionWords={ exclusionHighlights }/>
              </p>
              <p>
                <label>publicationYear:</label><span> </span> 
                <KeyWordHighlight 
                  text={ props.study.publicationYear.toString() } 
                  inclusionWords={ inclusionHighlights } 
                  exclusionWords={ exclusionHighlights }/>
              </p>
              <p>
                <label>doi:</label><span> </span>
                <KeyWordHighlight 
                  text={ props.study.doi } 
                  inclusionWords={ inclusionHighlights } 
                  exclusionWords={ exclusionHighlights }/>
              </p>
              <p>
                <label>URL:</label><span> </span> 
                <KeyWordHighlight 
                  text={ props.study.url } 
                  inclusionWords={ inclusionHighlights } 
                  exclusionWords={ exclusionHighlights }/>
              </p>
              <p>
                <label>language:</label><span> </span> 
                <KeyWordHighlight 
                  text={ props.study.language } 
                  inclusionWords={ inclusionHighlights } 
                  exclusionWords={ exclusionHighlights }/>
              </p>
            </>
          ) : (
            <>
              { showAbstract && <p><label>abstract:</label> { props.study.documentAbstract }</p> }
              <p><label>authors:</label> { props.study.authors }</p>
              <p><label>journal:</label> { props.study.journalTitle }</p>
              <p><label>publicationYear:</label> { props.study.publicationYear }</p>
              <p><label>doi:</label> { props.study.doi }</p>
              <p><label>URL:</label> { props.study.url }</p>
              <p><label>language:</label> { props.study.language }</p>
            </>
          )}

          <FullTextField 
            isFullText={ props.isFullText } 
            study={ props.study }
            allowChanges={ allowChanges } />
          
          <StudyTags 
            studyId={ props.study.id } 
            reviewTags={ props.reviewTags } 
            allowChanges={ allowChanges }/>
            
          <div className='slrspot__screeningStudy-options'>
            <button onClick={ () => setShowDiscussion(true)}>discussion</button>
            <button onClick={ () => setShowHistory(true) }>history</button>
            { !(props.tab === EXCLUDED) && allowChanges && <button onClick={ handleDuplicate }>mark as duplicate</button>}
          </div>
          { allowChanges && tabSpecificContent() }
        </div>
      </div>
    )
  }
}

export default ScreeningStudyInFolder