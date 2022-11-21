import React, { useState, useEffect } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { useNavigate } from "react-router-dom";
import UsersBrowser from '../usersBrowser/UsersBrowser';
import EventBus from '../../common/EventBus';
import ReviewInfo from './reviewInfo/ReviewInfo';
import './newReview.css'


const NewReview = () => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isReviewInfo, setIsReviewInfo] = useState(true);
  const [isMembersSettings, setIsMembersSettings] = useState(false);
  const [isProtocolSettings, setIsProtocolSettings] = useState(false);
  const navigate = useNavigate();
  const [selectedMembers, setSelectedMembers] = useState([]);
  const [searchedUsers, setSearchedUsers] = useState([]);

  useEffect(() => {
    axiosInstance.get("/users/emails")
    .then((response) => {
      console.log(response.data);
      setSearchedUsers(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
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
    setIsReviewInfo(true);
    setIsMembersSettings(false);
    setIsProtocolSettings(false);
  };

  const onMembersClick = () => {
    setIsReviewInfo(false);
    setIsMembersSettings(true);
    setIsProtocolSettings(false);
  };

  const onProtocolClick = () => {
    setIsReviewInfo(false);
    setIsMembersSettings(false);
    setIsProtocolSettings(true);
  };

  const showSelectedPage = () => {
    if (isReviewInfo) {
      return (
        <ReviewInfo 
          register={register}
          errors={errors}
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
    if (isProtocolSettings) {
      return (
        <div>
          Protocol
        </div>
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
            {/* <OptionItem trigger={isProtocolSettings} name='Protocol' handleClick={onProtocolClick} /> */}
          </div>

          <div className='slrspot__newReview-settings'>
            <div className='slrspot__newReview-settings-header'>
              { isReviewInfo && <h2>review information</h2>}
              { isMembersSettings && <h2>add members</h2>}
              { isProtocolSettings && <h2>prepare protocol</h2>}
            </div>
            <div className='slrspot__newReview-settings-container'>
              { showSelectedPage() }
            </div>
            { isReviewInfo && 
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