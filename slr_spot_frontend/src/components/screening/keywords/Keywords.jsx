import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import EventBus from '../../../common/EventBus';
import axiosInstance from '../../../services/api';
import { EXCLUSION_TYPE, INCLUSION_TYPE } from '../criteria/CriteriaTypes';
import { useForm } from "react-hook-form";
import './keywords.css';
import ConfirmationPopup from '../../popups/confirmationPopup/ConfirmationPopup';
import NewElementField from '../newElementField/NewElementField';


const Keywords = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [keywords, setKeywords] = useState([]);
  const [showAddInclusion, setShowAddInclusion] = useState(false);
  const [showAddExclusion, setShowAddExclusion] = useState(false);
  const [showKeywordRemoveConfirmation, setShowKeywordRemoveConfirmation] = useState(false);
  const [keywordToRemove, setKeywordToRemove] = useState();
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [showTeamKeywords, setShowTeamKeywords] = useState(true);
  const { user: currentUser } = useSelector((state) => state.auth);

  const inclusionKeywords = keywords.filter((keyword) => keyword.type.name === INCLUSION_TYPE)
    .map((keyword) => 
      <p key={keyword.name}>
        <AiFillMinusCircle 
          className='slrspot__screening-keywords-table-removeIcon'
          onClick={ () => handleRemoveKeyword(keyword)}/>
        {keyword.name}
      </p>
  );

  const exclusionKeywords = keywords.filter((keyword) => keyword.type.name === EXCLUSION_TYPE)
    .map((keyword) => 
      <p key={keyword.name}>
        <AiFillMinusCircle 
          className='slrspot__screening-keywords-table-removeIcon'
          onClick={ () => handleRemoveKeyword(keyword)}/>
        {keyword.name}
      </p>
  );

  useEffect(() => {
    if (showTeamKeywords) {
      axiosInstance.get("/reviews/" + reviewId + "/keywords")
      .then((response) => {
        setKeywords(response.data);
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          EventBus.dispatch('expirationLogout');
        }
      });
    } else {
      axiosInstance.get("/users/" + currentUser.id + "/keywords")
      .then((response) => {
        setKeywords(response.data);
      })
      .catch((error) => {
        if (error.response && error.response.status === 403) {
          EventBus.dispatch('expirationLogout');
        }
      });
    }
  }, [showTeamKeywords]);

  const handleRemoveKeyword = (keyword) => {
    setShowKeywordRemoveConfirmation(true);
    setKeywordToRemove(keyword);
  }

  const onCancelRemoveKeyword = () => {
    setShowKeywordRemoveConfirmation(false);
    setKeywordToRemove();
  }

  const confirmRemoveKeyword= () => {
    console.log(keywordToRemove);
    axiosInstance.post("/reviews/" + reviewId + "/keywords/" + keywordToRemove.id + 
      "/" + keywordToRemove.type.id + "/remove")
    .then(() => {
      setShowKeywordRemoveConfirmation(false);
      window.location.reload();
    });
  }

  const onSubmitNewInclusionKeyword = (formData) => {
    console.log(formData);
    onSubmitNewCriterion(formData.inclusionKeyword, INCLUSION_TYPE);
  }

  const onSubmitNewExclusionKeyword = (formData) => {
    onSubmitNewCriterion(formData.exclusionKeyWord, EXCLUSION_TYPE);
  }

  const onSubmitNewCriterion = (name, type) => {
    console.log(name);
    axiosInstance.get("/reviews/" + reviewId + "/keywords/add", { params: {
      name, type
    }})
    .then(() => {
      window.location.reload();
    });
    console.log(name);
  }

  const isKeywordsEmpty = (keywordsList) => {
    if (keywordsList.length === 0) {
      return (
        <h1 className='slrspot__screening-keywords-empty'>empty</h1>
      )
    }
  }

  return (
    <div className='slrspot__screening-keywords'>
      <OptionHeader 
        content='Manage keywords'
        backward={ () => navigate(location.pathname.replace('/keywords', '')) }/>
      <div className='slrspot__screening-keywords-assignment-container'>
        <h2 onClick={ () => setShowTeamKeywords(true) }>Shared with team</h2>
        <h2 onClick={ () => setShowTeamKeywords(false) }>Your keywords</h2>
        <div className='slrspot__screening-keywords-container'>
          <div className='slrspot__screening-keywords-table'>
            <div className='slrspot__screening-keywords-table-header'>
              <div className='slrspot__screening-keywords-table-header-title'>
                <h2>{ INCLUSION_TYPE }</h2>
                { !showAddInclusion && 
                  <AiFillPlusCircle 
                    className='slrspot__screening-keywords-table-addIcon'
                    onClick={ () => setShowAddInclusion(true) }/> }
              </div>
              { showAddInclusion && 
                  <NewElementField 
                    register={ register }
                    fieldName='inclusionKeyword'
                    handleSubmit={ handleSubmit(onSubmitNewInclusionKeyword) }
                    handleClose={ () => setShowAddInclusion(false) } />}
            </div>
            <div className='slrspot__screening-keywords-table-items'>
              { isKeywordsEmpty(inclusionKeywords) }
              { inclusionKeywords }
            </div>
          </div>
          <div className='slrspot__screening-keywords-table-delimiter'>
          </div>
          <div className='slrspot__screening-keywords-table'>
          <div className='slrspot__screening-keywords-table-header'>
              <div className='slrspot__screening-keywords-table-header-title'>
                <h2>{ EXCLUSION_TYPE }</h2>
                { !showAddExclusion && 
                  <AiFillPlusCircle 
                    className='slrspot__screening-keywords-table-addIcon'
                    onClick={ () => setShowAddExclusion(true) }/> }
              </div>
              { showAddExclusion && 
                  <NewElementField 
                  register={ register }
                  fieldName='exclusionKeyword'
                  handleSubmit={ handleSubmit(onSubmitNewExclusionKeyword) }
                  handleClose={ () => setShowAddExclusion(false) } />}
            </div>
            <div className='slrspot__screening-keywords-table-items'>
              { isKeywordsEmpty(exclusionKeywords) }
              { exclusionKeywords }
            </div>
          </div>
        </div>
        { showKeywordRemoveConfirmation && 
          <ConfirmationPopup 
            title="remove keyword"
            message={'Do you want to remove ' + keywordToRemove.name + ' from your review?'}
            triggerConfirm={ confirmRemoveKeyword }
            triggerCancel={ onCancelRemoveKeyword }
          /> 
          }
      </div>
    </div>
  )
}

export default Keywords