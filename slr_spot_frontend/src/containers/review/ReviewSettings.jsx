import React, { useState, useEffect } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { BeatLoader } from "react-spinners";
import Check from 'react-bootstrap/FormCheck';
import { useNavigate } from "react-router-dom";
import './home.css';


const ReviewSettings = () => {
  const {register, handleSubmit, formState: { errors }} = useForm();

  return (
    <div className='slrspot__review-home'>
      <div className='slrspot__review-home-title'>
        <h1>Settings</h1>
      </div>
      <div className='slrspot__newReview-reviewSettings'>
        <label>Review name</label>
        <input  
          {...register("name", { 
            required: true,
          })}
          name='name' 
        />
        {/* {errors.name && errors.name.type=== "required" && 
          <p className="slrspot__newReview-error">This field is required</p>
        } */}
        <label>Area of research</label>
        <input  
          {...register("researchArea", { 
            required: true,
          })}
          name='researchArea' 
        />
        {/* {errors.researchArea && errors.researchArea.type=== "required" && 
          <p className="slrspot__newReview-error">This field is required</p>
        } */}
        <label>Description</label>
        <textarea  
          {...register("description")}
          name='description' 
        />
        <label>Public review</label>
        <Check {...register("isPublic")}/>
        <label>Reviewers required for screening</label>
        <input  
          {...register("screeningReviewers")}
          name='screeningReviewers'
          type='number'
          min={1}
        />
        <button>UPDATE</button>
      </div>
    </div>
  )
}

export default ReviewSettings