import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy } from '../../../../components';
import axiosInstance from '../../../../services/api';
import '../screening.css';

const ToBeReviewed = (props) => {
  const [studies, setStudies] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    axiosInstance.get("/studies/to-review", { params: {
      reviewId
    }})
    .then((response) => {
      console.log(response.data)
      setStudies(response.data)
    })
    .catch(() => {
    });
  }, []);

  return (
    <div className='slrspot__screening-studies'>
      { studies.map(study => (
        <ScreeningStudy study={study} isShowAbstracts={props.showAbstracts}/>
      ))}
    </div>
  )
}

export default ToBeReviewed