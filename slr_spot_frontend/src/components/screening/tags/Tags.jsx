import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from "react-router-dom";
import OptionHeader from '../optionHeader/OptionHeader';
import { AiFillMinusCircle, AiFillPlusCircle, AiFillCheckSquare, AiFillCloseSquare } from "react-icons/ai";
import axiosInstance from '../../../services/api';
import { useForm } from "react-hook-form";
import ConfirmationPopup from '../../popups/confirmationPopup/ConfirmationPopup';
import { OWNER, MEMBER, COOWNER } from '../../../constants/roles';
import './tags.css';


const Tags = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { reviewId } = useParams();
  const [showTagRemoveConfirmation, setShowTagRemoveConfirmation] = useState(false);
  const [tagToRemove, setTagToRemove] = useState();
  const [showAddTag, setShowAddTag] = useState(false);
  const [tags, setTags] = useState([]);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [errorMessage, setErrorMessage] = useState();
  var allowChanges = props.userRole && [OWNER, COOWNER, MEMBER].includes(props.userRole);


  useEffect(() => {
    axiosInstance.get("/tags", { params: {
      reviewId
    }})
    .then((response) => {
      setTags(response.data);
    })
    .catch((error) => {
    });
  }, []);

  const handleRemoveTag = (tag) => {
    setShowTagRemoveConfirmation(true);
    setTagToRemove(tag);
  }

  const onCancelRemoveTag = () => {
    setShowTagRemoveConfirmation(false);
    setTagToRemove('');
  }

  const confirmRemoveTag = () => {
    axiosInstance.delete("/tags/" + tagToRemove.id)
    .then(() => {
      setShowTagRemoveConfirmation(false);
      window.location.reload();
    });
  }

  const onSubmitNewTag = (formData) => {
    const name = formData.tagName;
    axiosInstance.post("/tags", null, { params: {
      reviewId, name
    }})
    .then(() => {
      window.location.reload();
    });
  }

  const listTags = tags.map((tag) => 
    <p key={tag.name}>
      { allowChanges && 
      <AiFillMinusCircle 
        className='slrspot__screening-tags-table-removeIcon'
        onClick={ () => handleRemoveTag(tag)}
        />
      }
      {tag.name}
    </p>
  );

  const isTagsEmpty = () => {
    if (tags.length === 0) {
      return (
        <h1 className='slrspot__screening-tags-empty'>empty</h1>
      )
    }
  }

  const NewTagInput = () => {
    return (
      <div className='slrspot__screening-tags-new'>
        <input  
          {...register("tagName", { 
            required: true,
          })}
          name='tagName' 
        />
        <AiFillCheckSquare 
          className='slrspot__screening-tags-new-button' 
          color='green'
          onClick={ handleSubmit(onSubmitNewTag) }/>
        <AiFillCloseSquare 
          className='slrspot__screening-tags-new-button'
          color='red' 
          onClick={ () => setShowAddTag(false) }/>
        { errors.tagName && errors.tagName.type=== "required" && 
          <p className="slrspot__screening-tags-new-error">This field is required</p> }
        { errorMessage && 
        <p className="slrspot__screening-tags-new-error">This field is required</p> }
      </div>
    )
  }

  return (
    <div className='slrspot__screening-tags'>
      <OptionHeader 
        content='Manage tags'
        backward={ () => navigate(location.pathname.replace('/tags', '')) }/>
      <div className='slrspot__screening-tags-container'>
        <div className='slrspot__screening-tags-table'>
          <div className='slrspot__screening-tags-table-header'>
            <h2>Tags</h2>
            { showAddTag 
              ? <NewTagInput /> 
              : allowChanges && <AiFillPlusCircle 
                  className='slrspot__screening-tags-table-addIcon'
                  onClick={ () => setShowAddTag(true) }/> }
          </div>
          <div className='slrspot__screening-tags-table-items'>
            { isTagsEmpty() }
            { listTags }
          </div>
        </div>
      </div>
      { showTagRemoveConfirmation && 
        <ConfirmationPopup 
          title="remove tag"
          message={'Do you want to remove ' + tagToRemove.name + ' from your review?'}
          triggerConfirm={confirmRemoveTag}
          triggerCancel={onCancelRemoveTag}
        /> 
        }
    </div>
  )
}

export default Tags