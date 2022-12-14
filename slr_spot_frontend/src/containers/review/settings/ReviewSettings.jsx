import React, { useState, useEffect } from 'react'
import { useForm } from "react-hook-form";
import Check from 'react-bootstrap/FormCheck';
import { useParams } from "react-router-dom";
import axiosInstance from '../../../services/api';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import { OWNER, COOWNER } from '../../../constants/roles';
import './reviewSettings.css';


const ReviewSettings = (props) => {
  const [loading, setLoading] = useState(false);
  const [reviewData, setReviewData] = useState([]);
  const { reviewId } = useParams();
  const {register, handleSubmit, watch, formState: { errors }, reset} = useForm();
  var allowChanges = props.userRole && [OWNER, COOWNER].includes(props.userRole);

  const [researchQuestions, setResearchQuestions] = useState([]);
  const [question, setQuestion] = useState('');

  const listQuestions = researchQuestions.map((question, idx) => (
    <div className='slrspot__reviewInfo-question' key={idx}>
      <p>
        {question}
      </p>
      <AiFillMinusCircle 
        className='slrspot__reviewInfo-removeIcon'
        onClick={ () => handleRemoveQuestion(question) } />
    </div>
  ));

  useEffect(() => {
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data.review);
      setResearchQuestions(response.data.review.researchQuestions.map(q => q.name));
      reset();
    });
  }, []);

  const handleAddQuestion = () => {
    if (question.trim().length > 0  && researchQuestions.filter(item => item === question) < 1) {
      setResearchQuestions(oldArray => [...oldArray, question]);
      document.getElementById("questionField").value = "";
    }
  }

  const handleRemoveQuestion = (question) => {
    setResearchQuestions(researchQuestions.filter(item => item !== question));
  }

  const handleChangeQuestion = (event) => {
    setQuestion(event.target.value);
  }

  const onSubmit = (formData) => {
    setLoading(true);
    var name = formData.name;
    var researchArea = formData.researchArea;
    var description = formData.description;
    var isPublic = formData.isPublic;
    var screeningReviewers = formData.screeningReviewers;
    axiosInstance.put("/reviews/" + reviewId, {
      name, researchArea, description, isPublic, screeningReviewers, researchQuestions
    })
    .then(() => {
      setLoading(false);
      window.location.reload();
    });
  };

  if (!allowChanges) {
    return (
      <div className='slrspot__review-unauthorized'>
        <h1>Access not allowed</h1>
      </div>
    )
  }

  return (
    <div className='slrspot__review-settings'>
      <div className='slrspot__review-team-header'>
        <h1>Settings</h1>
      </div>
      <form className='slrspot__review-settings-container' onSubmit={handleSubmit(onSubmit)}>

        <label>Review name</label>
        {errors.name && errors.name.type === "validate" &&  
          <p className="slrspot__input-error">Review name can't be empty</p>
        }
        <input  
          {...register("name", { 
            defaultValue:reviewData.title, 
            validate: () => {
              return watch('name').trim().length !== 0;}   
            })
          }
          name='name'
          defaultValue={reviewData.title}
          
        />

        <label>Area of research</label>
        <input  
          {...register("researchArea", { defaultValue:reviewData.researchArea })}
          name='researchArea'
          defaultValue={reviewData.researchArea}
        />

        <label>Research question</label>
        { researchQuestions.length > 0 && listQuestions }
        <div className='slrspot__reviewInfo-question'>
          <input id="questionField" onChange={ handleChangeQuestion } />
          <AiFillPlusCircle 
            className='slrspot__reviewInfo-addIcon'
            onClick={ () => handleAddQuestion() } />
        </div>

        <label>Description</label>
        <textarea  
          {...register("description", { defaultValue: reviewData.description })}
          name='description'
          defaultValue={reviewData.description}
        />

        <label>Public review</label>
        <Check {...register("isPublic", { defaultValue: reviewData.isPublic })} defaultChecked={reviewData.isPublic}/>

        <label hidden>Show votes on conflicts</label>
        <Check {...register("showConflictVotes")} defaultChecked={reviewData.isPublic} hidden/>

        <label>Reviewers required for screening</label>
        <input
          {...register("screeningReviewers")}
          name='screeningReviewers'
          type='number'
          min={1}
          defaultValue={reviewData.screeningReviewers}
        />
        <button type='submit'>save</button>
      </form>
    </div>
  )
}

export default ReviewSettings