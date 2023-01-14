import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import axiosInstance from '../../../services/api';
import { INCLUSION_TYPE, EXCLUSION_TYPE } from './CriteriaTypes';
import { useForm } from "react-hook-form";
import NewElementField from '../newElementField/NewElementField';
import './criteria.css';
import ConfirmationPopup from '../../popups/confirmationPopup/ConfirmationPopup';
import { OWNER, MEMBER, COOWNER } from '../../../constants/roles';


const Criteria = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [criteria, setCriteria] = useState([]);
  const [showAddInclusion, setShowAddInclusion] = useState(false);
  const [showAddExclusion, setShowAddExclusion] = useState(false);
  const [showCriterionRemoveConfirmation, setShowCriterionRemoveConfirmation] = useState(false);
  const [criterionToRemove, setCriterionToRemove] = useState();
  const {register, handleSubmit, formState: { errors }} = useForm();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);

  
  const inclusionCriteria = criteria.filter((criterion) => criterion.type === INCLUSION_TYPE)
    .map((criterion) => 
      <p key={criterion.name}>
        { allowChanges && 
          <AiFillMinusCircle 
            className='slrspot__screening-criteria-table-removeIcon'
            onClick={ () => handleRemoveCriterion(criterion)}/>
        }
        {criterion.name}
      </p>
  );

  const exclusionCriteria = criteria.filter((criterion) => criterion.type === EXCLUSION_TYPE)
    .map((criterion) => 
      <p key={criterion.name}>
        { allowChanges && 
          <AiFillMinusCircle 
            className='slrspot__screening-criteria-table-removeIcon'
            onClick={ () => handleRemoveCriterion(criterion)}/>
        }
        {criterion.name}
      </p>
  );

  useEffect(() => {
    axiosInstance.get("/criteria", { params: {
      reviewId
    }})
    .then((response) => {
      setCriteria(response.data);
    })
    .catch((error) => {
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

  const onCancelRemoveCriterion = () => {
    setShowCriterionRemoveConfirmation(false);
    setCriterionToRemove();
  }

  const confirmRemoveCriterion = () => {
    axiosInstance.delete("/criteria/" + criterionToRemove.id, { params: {
      reviewId
    }})
    .then(() => {
      setShowCriterionRemoveConfirmation(false);
      window.location.reload();
    });
  }

  const onSubmitNewInclusionCriterion = (formData) => {
    onSubmitNewCriterion(formData.inclusionCriterion, INCLUSION_TYPE);
  }

  const onSubmitNewExclusionCriterion = (formData) => {
    onSubmitNewCriterion(formData.exclusionCriterion, EXCLUSION_TYPE);
  }

  const onSubmitNewCriterion = (name, type) => {
    axiosInstance.post("/criteria", { 
      reviewId: reviewId,
      name: name,
      type: type
    })
    .then(() => {
      window.location.reload();
    });
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
              { allowChanges && !showAddInclusion && 
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
              { allowChanges && !showAddExclusion && 
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
          triggerConfirm={confirmRemoveCriterion}
          triggerCancel={onCancelRemoveCriterion}
        /> 
        }
      </div>
    </div>
  )
}

export default Criteria