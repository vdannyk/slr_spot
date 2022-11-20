import React, { useState, useEffect } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { BeatLoader } from "react-spinners";
import Check from 'react-bootstrap/FormCheck';
import TeamMemberField from './TeamMemberField';
import EventBus from '../../common/EventBus';
import { useNavigate } from "react-router-dom";
import './newReview.css'

const people = [
  "Danny",
  "Daniel",
  "Dawid",
  "Dominik",
  "Krzysztof",
  "Stefan",
  "Szymon"
];

const NewReview = () => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isReviewSettings, setIsReviewSettings] = useState(true);
  const [isMembersSettings, setIsMembersSettings] = useState(false);
  const [isProtocolSettings, setIsProtocolSettings] = useState(false);
  const [isAddMemberEmpty, setIsAddMemberEmpty] = useState(false);
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();

  const [searchTerm, setSearchTerm] = React.useState("");
  const [searchResults, setSearchResults] = React.useState([]);
  const [contributors, setContributors] = useState([]);
  const [contributor, setContributor] = useState('');
  
  // const handleChange = event => {
  //    setSearchTerm(event.target.value);
  //  };

  useEffect(() => {
    axiosInstance.get("/users/emails")
    .then((response) => {
      console.log(response.data);
      setUsers(response.data);
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }, []);

  useEffect(() => {
     const results = users.filter(person =>
       person.toLowerCase().includes(searchTerm)
     );
     setSearchResults(results);
   }, [searchTerm, users]);
  

  const onSubmit = (formData) => {
    setLoading(true);
    const name = formData.name;
    const researchArea = formData.researchArea;
    const description = formData.description;
    const isPublic = formData.isPublic;
    const screeningReviewers = formData.screeningReviewers;
    const reviewers = contributors;
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

  const onAddContributor = (newContributor) => {
    if (newContributor.trim().length === 0) {
      setIsAddMemberEmpty(true);
      return;
    }
    setContributors(oldArray => [...oldArray, newContributor]);
    console.log(contributors);
  }

  const handleRemoveMember = (member) => {
    setContributors(contributors.filter(item => item !== member))
    setUsers(oldArray => [...oldArray, member]);
  }

  const handleContributorChange = (event) => {
    if (event.target.value.trim().length !== 0) {
      setIsAddMemberEmpty(false);
    }
    setSearchTerm(event.target.value);
    setContributor(event.target.value);
  }

  const handleUsernameClick = (username) => {
    setContributors(oldArray => [...oldArray, username]);
    setUsers(users.filter(item => item !== username))
  }

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
        <div className='slrspot__newReview-members'>
          <div className='slrspot__newReview-members-add'>
              <div className='slrspot__newReview-members-add-field'>
                <input
                  type="text"
                  placeholder="Search by email"
                  value={searchTerm}
                  onChange={handleContributorChange}
                />
                <ul>
                  {searchResults.map(item => (
                    <li onClick={ () => handleUsernameClick(item) }>{ item }</li>
                  ))}
                </ul>
              </div>
              {/* <button type="button" onClick={() => onAddContributor(contributor)}>add</button> */}
              {/* {isAddMemberEmpty && 
                <p className="slrspot__input-error">Invalid username</p>
              } */}
          </div>
          <div className='slrspot__newReview-members-list'>
            {contributors.map(item => (
              <TeamMemberField username={item} triggerRemove={handleRemoveMember}/>
            ))}
          </div>
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