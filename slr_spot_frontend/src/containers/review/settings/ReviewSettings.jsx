import React, { useState, useEffect } from 'react'
import { useForm } from "react-hook-form";
import { BeatLoader } from "react-spinners";
import Check from 'react-bootstrap/FormCheck';
import { useParams } from "react-router-dom";
import axiosInstance from '../../../services/api';
import './reviewSettings.css';


const ReviewSettings = () => {
  const [loading, setLoading] = useState(false);
  const [reviewData, setReviewData] = useState([]);
  const { reviewId } = useParams();
  const {register, handleSubmit, formState: { errors }} = useForm();

  useEffect(() => {
    console.log(reviewId);
    axiosInstance.get("/reviews/" + reviewId)
    .then((response) => {
      setReviewData(response.data.review);
    });
  }, []);

  const onSubmit = (formData) => {
    setLoading(true);
    const name = formData.name;
    const researchArea = formData.researchArea;
    const description = formData.description;
    const isPublic = formData.isPublic;
    const screeningReviewers = formData.screeningReviewers;
    console.log(formData);
    axiosInstance.post("/reviews/" + reviewId + "/update", {
      name, researchArea, description, isPublic, screeningReviewers
    })
    .then(() => {
      setLoading(false);
    });
  };

  return (
    <div className='slrspot__review-settings'>
      <div className='slrspot__review-team-header'>
        <h1>Settings</h1>
      </div>
      <form className='slrspot__review-settings-container' onSubmit={handleSubmit(onSubmit)}>

        <label>Review name</label>
        <input  
          {...register("name", { 
            required: true,
          })}
          name='name'
          defaultValue={reviewData.title}
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
          defaultValue={reviewData.researchArea}
        />
        {/* {errors.researchArea && errors.researchArea.type=== "required" && 
          <p className="slrspot__newReview-error">This field is required</p>
        } */}

        <label>Research question</label>
        <input  
          {...register("researchQuestion", { 
            required: true,
          })}
          name='researchQuestion' 
        />
        {/* {errors.researchQuestion && errors.researchQuestion.type=== "required" && 
          <p className="slrspot__newReview-error">This field is required</p>
        } */}

        <label>Description</label>
        <textarea  
          {...register("description")}
          name='description'
          defaultValue={reviewData.description}
        />

        <label>Public review</label>
        <Check {...register("isPublic")} defaultChecked={reviewData.isPublic}/>

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