import React, { useState } from 'react'
import { useForm } from "react-hook-form";
import axiosInstance from "../../services/api";
import { BeatLoader } from "react-spinners";


const NewReview = () => {
  const [loading, setLoading] = useState(false);
  const {register, handleSubmit, formState: { errors }} = useForm();

  const onSubmit = (formData) => {
    setLoading(true);
    const title = formData.title;

    axiosInstance.post("/reviews/save", {
      title
    })
    .then(() => {
      setLoading(false);
    });
  };

  return (
    <div className='slrspot__newReview'>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className='slrspot__newReview-container'>
          <div className="slrspot__newReview-header">
            <h1>Create a new review</h1>
          </div>
            <div className="slrspot___newReview-fields">
              <div className="slrspot__newReview-field">
                <label>Title</label>
                <input  
                  {...register("title", { 
                    required: true,
                  })}
                  name='title' 
                />
                {errors.title && errors.title.type=== "required" && 
                  <p className="slrspot__newReview-error">This field is required</p>
                }
              </div>
            </div>

            <div className="slrspot___newReview-submit">
              <button type="submit" className='slrspot__newReview-submitBtn'>
                Create review
              </button>
              { loading && (<BeatLoader color="#AE67FA" />)}
            </div>
        </div>
      </form>
    </div>
  )
}

export default NewReview