import React, { useState } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import Check from 'react-bootstrap/FormCheck';
import { useNavigate } from "react-router-dom";
import './newReview.css'
import UsersBrowser from '../usersBrowser/UsersBrowser';


const NewReview = () => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isReviewSettings, setIsReviewSettings] = useState(true);
  const [isMembersSettings, setIsMembersSettings] = useState(false);
  const [isProtocolSettings, setIsProtocolSettings] = useState(false);
  const navigate = useNavigate();
  const [selectedMembers, setSelectedMembers] = useState([]);

  const onSubmit = (formData) => {
    setLoading(true);
    const name = formData.name;
    const researchArea = formData.researchArea;
    const description = formData.description;
    const isPublic = formData.isPublic;
    const screeningReviewers = formData.screeningReviewers;
    const reviewers = selectedMembers;
    console.log(formData);
    axiosInstance.post("/reviews/save", {
      name, researchArea, description, isPublic, screeningReviewers, reviewers
    })
    .then((response) => {
      setLoading(false);
      console.log(response.data)
      navigate("/reviews/" + response.data);
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

  const showSelectedPage = () => {
    if (isReviewSettings) {
      return (
        <div className='slrspot__newReview-reviewSettings'>
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
        </div>
      )
    }

    if (isMembersSettings) {
      return (
        <UsersBrowser setMembersList={setSelectedMembers} />
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
      <form className='slrspot__newReview-container' onSubmit={handleSubmit(onSubmit)}>
          <div className='slrspot__newReview-menu'>
            { isReviewSettings 
              ? <p className='slrspot__newReview-menu-selected'>Review</p> 
              : <p onClick={onReviewsClick}>Review</p>}
            { isMembersSettings 
              ? <p className='slrspot__newReview-menu-selected'>Members</p> 
              : <p onClick={onMembersClick}>Members</p>}
            {/* { isProtocolSettings 
              ? <p className='slrspot__newReview-menu-selected'>Protocol</p> 
              : <p onClick={onProtocolClick}>Protocol</p>} */}
          </div>

          <div className='slrspot__newReview-settings'>
            <div className='slrspot__newReview-settings-header'>
              { isReviewSettings && <h2>review information</h2>}
              { isMembersSettings && <h2>add members</h2>}
              { isProtocolSettings && <h2>prepare protocol</h2>}
            </div>
            <div className='slrspot__newReview-settings-container'>
              { showSelectedPage() }
            </div>
            { isReviewSettings && 
              <div className='slrspot__newReview-settings-buttons'>
                <button className='slrspot__newReview-settings-nextButton' onClick={onMembersClick}>next</button>
              </div>
            }
            {/* { isMembersSettings && 
              <div className='slrspot__newReview-settings-buttons'>
                <button onClick={onReviewsClick}>previous</button>
                <button onClick={onProtocolClick}>next</button>
              </div>
            } */}
            { isMembersSettings && 
              <div className='slrspot__newReview-settings-buttons'>
                <button onClick={onReviewsClick}>previous</button>
                <button type="submit">create review</button>
              </div>
            }
          </div>
      </form>
    </div>
  )
}

export default NewReview