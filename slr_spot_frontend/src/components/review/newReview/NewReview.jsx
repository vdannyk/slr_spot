import React, { useState, useEffect } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../../services/api";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import ReviewInfo from './reviewInfo/ReviewInfo';
import UsersBrowser from '../usersBrowser/UsersBrowser';
import './newReview.css'


const NewReview = () => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, watch, formState: { errors }} = useForm();
  const [isReviewInfo, setIsReviewInfo] = useState(true);
  const [isMembersSettings, setIsMembersSettings] = useState(false);
  const navigate = useNavigate();
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [searchedUsers, setSearchedUsers] = useState([]);
  const { user: currentUser } = useSelector((state) => state.auth);
  const [questions, setQuestions] = useState([]);

  useEffect(() => {
    axiosInstance.get("/users/emails")
    .then((response) => {
      setSearchedUsers(response.data);
    })
    .catch((error) => {
    });
  }, []);

  const onSubmit = (formData) => {
    setLoading(true);
    const name = formData.name;
    const researchArea = formData.researchArea;
    const description = formData.description;
    const isPublic = formData.isPublic;
    const screeningReviewers = formData.screeningReviewers;
    const reviewers = selectedMembers;
    const userId = currentUser.id;
    const researchQuestions = questions;

    axiosInstance.post("/reviews", {
      userId, name, researchArea, description, isPublic, screeningReviewers, reviewers, researchQuestions
    })
    .then((response) => {
      setLoading(false);
      navigate("/reviews/" + response.data);
    });
  };

  const onReviewsClick = () => {
    setIsReviewInfo(true);
    setIsMembersSettings(false);
  };

  const onMembersClick = () => {
    setIsReviewInfo(false);
    setIsMembersSettings(true);
  };

  const showSelectedPage = () => {
    if (isReviewInfo) {
      return (
        <ReviewInfo 
          register={register}
          errors={errors}
          questions={questions}
          setQuestions={setQuestions}
        />
      ) 
    }
    if (isMembersSettings) {
      return (
        <UsersBrowser
          searchedUsers={searchedUsers}
          setSearchedUsers={setSearchedUsers} 
          selectedMembers={selectedMembers} 
          setMembersList={setSelectedMembers} 
        />
      )
    }
  }

  const OptionItem = ({trigger, name, handleClick}) => {
    if (trigger) {
      return (
        <p className='slrspot__newReview-menu-selected'>{name}</p> 
      )
    } else {
      return (
        <p onClick={handleClick}>{name}</p>
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
            <OptionItem trigger={isReviewInfo} name='Review' handleClick={onReviewsClick} />
            <OptionItem trigger={isMembersSettings} name='Members' handleClick={onMembersClick} />
          </div>

          <div className='slrspot__newReview-settings'>
            <div className='slrspot__newReview-settings-header'>
              { isReviewInfo && <h2>review information</h2>}
              { isMembersSettings && <h2>add members</h2>}
            </div>
            <div className='slrspot__newReview-settings-container'>
              { showSelectedPage() }
            </div>
            { isReviewInfo && 
              <div className='slrspot__newReview-settings-buttons'>
                <button className='slrspot__newReview-settings-nextButton' onClick={onMembersClick}>next</button>
              </div>
            }
            { isMembersSettings && 
              <div className='slrspot__newReview-settings-buttons'>
                <button onClick={onReviewsClick}>previous</button>
                {watch('name').trim().length == 0 ? 
                  <>
                    <p className="slrspot__input-error" style={{ alignSelf: 'center', fontSize: '16px'}}>Review name is required</p>
                  </>
                  :
                  <button type="submit">create review</button>
                }
              </div>
            }
          </div>
      </form>
    </div>
  )
}

export default NewReview