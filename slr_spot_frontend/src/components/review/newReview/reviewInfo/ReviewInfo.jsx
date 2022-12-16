import React from 'react';
import Check from 'react-bootstrap/FormCheck';
import './reviewInfo.css';

const ReviewInfo = ({register, errors}) => {
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
      <input  
        {...register("researchQuestion", { 
          required: true,
        })}
        name='researchQuestion' 
      />
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
        {...register("screeningReviewers")}
        name='screeningReviewers'
        type='number'
        min={1}
      />

      <label>Public review</label>
      <Check {...register("isPublic")}/>

    </div>
  )
}

export default ReviewInfo