import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ScreeningStudy, StudyDiscussion, StudyHistory } from '../../../../components';
import { EXCLUDED } from '../../../../constants/tabs';
import axiosInstance from '../../../../services/api';
import '../screening.css';

const studies = [
  {
    "documentAbstract": "blablasdasdasdasd asd asdasd asd asd asd asd asa",
    "authors": "authorsblabla",
    "journalTitle": "jorunalltitlebla",
    "publicationYear": 2012,
    "doi": "123123/12312",
    "url": "https//:test",
    "language": "polish"
  },
  {
    "documentAbstract": "blabla",
    "authors": "authorsblabla",
    "journalTitle": "jorunalltitlebla",
    "publicationYear": 2012,
    "doi": "123123/12312",
    "url": "https//:test",
    "language": "polish"
  },
  {
    "documentAbstract": "bla asd asdasd asd asd asdas das asd asd asd asdasd asd asd asd asbla",
    "authors": "authorsblabla",
    "journalTitle": "jorunalltitlebla",
    "publicationYear": 2012,
    "doi": "123123/12312",
    "url": "https//:test",
    "language": "polish"
  },
  {
    "documentAbstract": "blabla",
    "authors": "authorsblabla",
    "journalTitle": "jorunalltitlebla",
    "publicationYear": 2012,
    "doi": "123123/12312",
    "url": "https//:test",
    "language": "polish"
  },
]

const Excluded = (props) => {
  // const [studies, setStudies] = useState([]);
  const [showHistory, setShowHistory] = useState(false);
  const [showDiscussion, setShowDiscussion] = useState(false);
  const { reviewId } = useParams();

  // useEffect(() => {
  //   axiosInstance.get("/studies/to-review", { params: {
  //     reviewId
  //   }})
  //   .then((response) => {
  //     console.log(response.data)
  //     setStudies(response.data)
  //   })
  //   .catch(() => {
  //   });
  // }, []);

  return (
    <div className='slrspot__screening-studies'>
      { studies.map(study => (
        <ScreeningStudy 
          study={study} 
          isShowAbstracts={props.showAbstracts} 
          triggerHistory={setShowHistory} 
          triggerDiscussion={setShowDiscussion} 
          tab={EXCLUDED} 
          isFullText={props.isFullText} />
      ))}
      { showHistory && <StudyHistory triggerCancel={setShowHistory} /> }
      { showDiscussion && <StudyDiscussion triggerCancel={setShowDiscussion} /> }

    </div>
  )
}

export default Excluded