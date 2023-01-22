import React from 'react';
import { useState } from 'react';
import Check from 'react-bootstrap/FormCheck';
import { AiFillMinusCircle, AiFillPlusCircle } from "react-icons/ai";
import './reviewInfo.css';

const ReviewInfo = ({register, errors, questions, setQuestions}) => {
  const [question, setQuestion] = useState('');

  const listQuestions = questions.map((question, idx) => (
    <div className='slrspot__reviewInfo-question' key={idx}>
      <p>
        {question}
      </p>
      <AiFillMinusCircle 
        className='slrspot__reviewInfo-removeIcon' 
        onClick={ () => handleRemoveQuestion(question) }/>
    </div>
  ));

  const handleAddQuestion = () => {
    if (question.trim().length > 0 && questions.filter(item => item === question) < 1) {
      setQuestions(oldArray => [...oldArray, question]);
      document.getElementById("questionField").value = "";
    }
  }

  const handleRemoveQuestion = (question) => {
    setQuestions(questions.filter(item => item !== question));
  }

  const handleChangeQuestion = (event) => {
    setQuestion(event.target.value);
  }

  return (
    <div className='slrspot__newReview-reviewInfo'>

      <label>Review name</label>
      <input  
        {...register("name", { 
          required: true,
        })}
        name='name' 
      />
      {errors.name && errors.name.type=== "required" && 
        <p className="slrspot__newReview-error">This field is required</p>
      }

      <label>Area of research</label>
      <input  
        {...register("researchArea", { 
          required: true,
        })}
        name='researchArea' 
      />
      {errors.researchArea && errors.researchArea.type=== "required" && 
        <p className="slrspot__newReview-error">This field is required</p>
      }

      <label>Research question</label>
      { questions.length > 0 && listQuestions }
      <div className='slrspot__reviewInfo-question'>
        <input id="questionField" onChange={ handleChangeQuestion }/>
        <AiFillPlusCircle 
          className='slrspot__reviewInfo-addIcon'
          onClick={ () => handleAddQuestion() }/>
      </div>
      
      {errors.researchQuestion && errors.researchQuestion.type=== "required" && 
        <p className="slrspot__newReview-error">This field is required</p>
      }

      <label>Description</label>
      <textarea  
        {...register("description")}
        name='description' 
      />

      <label>Reviewers required for screening</label>
      <input
        className='slrspot__reviewInfo-reviewersNum'
        {...register("screeningReviewers")}
        name='screeningReviewers'
        type='number'
        min={1}
        defaultValue={2}
      />

      <label>Public review</label>
      <Check {...register("isPublic")}/>

    </div>
  )
}

export default ReviewInfo