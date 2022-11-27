import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle, AiFillCheckSquare, AiFillCloseSquare } from "react-icons/ai";
import EventBus from '../../../common/EventBus';
import axiosInstance from '../../../services/api';
import { INCLUSION_TYPE, EXCLUSION_TYPE } from './CriteriaTypes';
import { useForm } from "react-hook-form";
import NewElementField from '../newElementField/NewElementField';
import './criteria.css';
import ConfirmationPopup from '../../popups/confirmationPopup/ConfirmationPopup';


const Criteria = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [criteria, setCriteria] = useState([]);
  const [showAddInclusion, setShowAddInclusion] = useState(false);
  const [showAddExclusion, setShowAddExclusion] = useState(false);
  const [showCriterionRemoveConfirmation, setShowCriterionRemoveConfirmation] = useState(false);
  const [criterionToRemove, setCriterionToRemove] = useState();
  const {register, handleSubmit, formState: { errors }} = useForm();

  
  const inclusionCriteria = criteria.filter((criterion) => criterion.type.name === INCLUSION_TYPE)
    .map((criterion) => 
      <p key={criterion.name}>
        <AiFillMinusCircle 
          className='slrspot__screening-criteria-table-removeIcon'
          onClick={ () => handleRemoveCriterion(criterion)}/>
        {criterion.name}
      </p>
  );

  const exclusionCriteria = criteria.filter((criterion) => criterion.type.name === EXCLUSION_TYPE)
    .map((criterion) => 
      <p key={criterion.name}>
        <AiFillMinusCircle 
          className='slrspot__screening-criteria-table-removeIcon'
          onClick={ () => handleRemoveCriterion(criterion)}/>
        {criterion.name}
      </p>
  );

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId + "/criteria")
    .then((response) => {
      setCriteria(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  const isCriteriaEmpty = (criteriaList) => {
    if (criteriaList.length === 0) {
      return (
        <h1 className='slrspot__screening-criteria-empty'>empty</h1>
      )
    }
  }

  const handleRemoveCriterion = (criterion) => {
    setShowCriterionRemoveConfirmation(true);
    setCriterionToRemove(criterion);
  }

  const onCancelRemoveTag = () => {
    setShowCriterionRemoveConfirmation(false);
    setCriterionToRemove();
  }

  const confirmRemoveTag = () => {
    console.log(criterionToRemove);
    axiosInstance.post("/reviews/" + reviewId + "/criteria/" + criterionToRemove.id + 
      "/" + criterionToRemove.type.id + "/remove")
    .then(() => {
      setShowCriterionRemoveConfirmation(false);
      window.location.reload();
    });
  }

  const onSubmitNewInclusionCriterion = (formData) => {
    console.log(formData);
    onSubmitNewCriterion(formData.inclusionCriterion, INCLUSION_TYPE);
  }

  const onSubmitNewExclusionCriterion = (formData) => {
    onSubmitNewCriterion(formData.exclusionCriterion, EXCLUSION_TYPE);
  }

  const onSubmitNewCriterion = (name, type) => {
    axiosInstance.get("/reviews/" + reviewId + "/criteria/add", { params: {
      name, type
    }})
    .then(() => {
      window.location.reload();
    });
    console.log(name);
  }

  return (
    <div className='slrspot__screening-criteria'>
      <OptionHeader 
        content='Set inclusion and exclusion criteria'
        backward={ () => navigate(location.pathname.replace('/criteria', '')) }/>
      <div className='slrspot__screening-criteria-container'>
        <div className='slrspot__screening-criteria-table'>
          <div className='slrspot__screening-criteria-table-header'>
            <div className='slrspot__screening-criteria-table-header-title'>
              <h2>{ INCLUSION_TYPE }</h2>
              { !showAddInclusion && 
                <AiFillPlusCircle 
                  className='slrspot__screening-criteria-table-addIcon'
                  onClick={ () => setShowAddInclusion(true) }/> }
            </div>
            { showAddInclusion && 
              <NewElementField 
                register={ register }
                fieldName='inclusionCriterion'
                handleSubmit={ handleSubmit(onSubmitNewInclusionCriterion) }
                handleClose={ () => setShowAddInclusion(false) } /> }
          </div>
          { isCriteriaEmpty(inclusionCriteria) }
          { inclusionCriteria }
        </div>
        <div className='slrspot__screening-criteria-table-delimiter'>
        </div>
        <div className='slrspot__screening-criteria-table'>
          <div className='slrspot__screening-criteria-table-header'>
            <div className='slrspot__screening-criteria-table-header-title'>
              <h2>{ EXCLUSION_TYPE }</h2>
              { !showAddExclusion && 
                <AiFillPlusCircle 
                  className='slrspot__screening-criteria-table-addIcon'
                  onClick={ () => setShowAddExclusion(true) }/> }
            </div>
            { showAddExclusion && 
                <NewElementField 
                  register={register}
                  fieldName='exclusionCriterion'
                  handleSubmit={ handleSubmit(onSubmitNewExclusionCriterion) }
                  handleClose={ () => setShowAddExclusion(false) } /> }
          </div>
          { isCriteriaEmpty(exclusionCriteria) }
          { exclusionCriteria }
        </div>
        { showCriterionRemoveConfirmation && 
        <ConfirmationPopup 
          title="remove criterion"
          message={'Do you want to remove ' + criterionToRemove.name + ' from your review?'}
          triggerConfirm={confirmRemoveTag}
          triggerCancel={onCancelRemoveTag}
        /> 
        }
      </div>
    </div>
  )
}

export default Criteria