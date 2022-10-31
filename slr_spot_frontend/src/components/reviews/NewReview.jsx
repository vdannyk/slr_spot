import React, { useState } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { BeatLoader } from "react-spinners";
import Check from 'react-bootstrap/FormCheck';
import './newReview.css'


const NewReview = () => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isReviewSettings, setIsReviewSettings] = useState(true);
  const [isMembersSettings, setIsMembersSettings] = useState(false);
  const [isProtocolSettings, setIsProtocolSettings] = useState(false);
  

  const onSubmit = (formData) => {
    setLoading(true);
    const title = formData.title;
    const researchArea = formData.researchArea;
    const description = formData.description;
    const isPublic = formData.isPublic;
    const screeningReviewers = formData.screeningReviewers;
    console.log(formData);
    axiosInstance.post("/reviews/save", {
      title, researchArea, description, isPublic, screeningReviewers
    })
    .then(() => {
      setLoading(false);
    });
  };

  const onReviewsClick = () => {
    setIsReviewSettings(true);
    setIsMembersSettings(false);
    setIsProtocolSettings(false);
  };

  const onMembersClick = () => {
    setIsReviewSettings(false);
    setIsMembersSettings(true);
    setIsProtocolSettings(false);
  };

  const onProtocolClick = () => {
    setIsReviewSettings(false);
    setIsMembersSettings(false);
    setIsProtocolSettings(true);
  };

  const selectPage = () => {
    if (isReviewSettings) {
      return (
        <div className='slrspot__newReview-reviewSettings'>
          <label>Review name</label>
          <input  
            {...register("title", { 
              required: true,
            })}
            name='title' 
          />
          {errors.title && errors.title.type=== "required" && 
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
          <label>Description</label>
          <input  
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
            max={10}
          />
          <button type="submit" className='slrspot__newReview-submitBtn'>
            Create review
          </button>
          { loading && (<BeatLoader color="#AE67FA" />)}
        </div>
      )
    }

    if (isMembersSettings) {
      return (
        <div>
          Members
        </div>
      )
    }

    if (isProtocolSettings) {
      return (
        <div>
          Protocol
        </div>
      )
    }
  }

  return (
    <div className='slrspot__newReview'>
      <div className="slrspot__newReview-header">
        <h1>Create a new review</h1>
      </div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className='slrspot__newReview-container'>
          <div className='slrspot__newReview-menu'>
            { isReviewSettings 
              ? <p className='slrspot__newReview-menu-selected'>Review</p> 
              : <p onClick={onReviewsClick}>Review</p>}
            { isMembersSettings 
              ? <p className='slrspot__newReview-menu-selected'>Members</p> 
              : <p onClick={onMembersClick}>Members</p>}
            { isProtocolSettings 
              ? <p className='slrspot__newReview-menu-selected'>Protocol</p> 
              : <p onClick={onProtocolClick}>Protocol</p>}
          </div>

          <div className='slrspot__newReview-settings'>
            {selectPage()}
          </div>
        </div>
      </form>
    </div>
  )
}

export default NewReview