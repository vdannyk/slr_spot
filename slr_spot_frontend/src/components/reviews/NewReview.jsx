import React, { useState } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { BeatLoader } from "react-spinners";
import Check from 'react-bootstrap/FormCheck';
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

  const [searchTerm, setSearchTerm] = React.useState("");
  const [searchResults, setSearchResults] = React.useState([]);
  const handleChange = event => {
     setSearchTerm(event.target.value);
   };
  React.useEffect(() => {
     const results = people.filter(person =>
       person.toLowerCase().includes(searchTerm)
     );
     setSearchResults(results);
   }, [searchTerm]);
  

  const onSubmit = (formData) => {
    setLoading(true);
    const name = formData.name;
    const researchArea = formData.researchArea;
    const description = formData.description;
    const isPublic = formData.isPublic;
    const screeningReviewers = formData.screeningReviewers;
    console.log(formData);
    axiosInstance.post("/reviews/save", {
      name, researchArea, description, isPublic, screeningReviewers
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
        </div>
      )
    }

    if (isMembersSettings) {
      return (
        <div>
          Members
          <input
            type="text"
            placeholder="Search"
            value={searchTerm}
            onChange={handleChange}
          />
          <ul>
            {searchResults.map(item => (
              <li>{item}</li>
            ))}
          </ul>
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
            { isProtocolSettings 
              ? <p className='slrspot__newReview-menu-selected'>Protocol</p> 
              : <p onClick={onProtocolClick}>Protocol</p>}
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
                <button onClick={onMembersClick}>next</button>
              </div>
            }
            { isMembersSettings && 
              <div className='slrspot__newReview-settings-buttons'>
                <button onClick={onReviewsClick}>previous</button>
                <button onClick={onProtocolClick}>next</button>
              </div>
            }
            { isProtocolSettings && 
              <div className='slrspot__newReview-settings-buttons'>
                <button onClick={onMembersClick}>previous</button>
                <button type="submit">create review</button>
              </div>
            }
          </div>
      </form>
    </div>
  )
}

export default NewReview