import React, { useEffect } from 'react';
import { useState } from 'react';
import { Table } from "react-bootstrap";
import axiosInstance from '../../../../services/api';
import { useSelector } from "react-redux";
import { useForm } from "react-hook-form";
import './studyDiscussion.css'

const StudyDiscussion = (props) => {
  const [comments, setComments] = useState([]);
  const {register, handleSubmit, formState: { errors }} = useForm();
  const { user: currentUser } = useSelector((state) => state.auth);
  const [triggerUpdate, setTriggerUpdate] = useState(false);

  useEffect(() => {
    axiosInstance.get("/studies/" + props.studyId + "/comments")
    .then((response) => {
      setComments(response.data);
    })
    .catch((error) => {
    });
  }, [triggerUpdate]);

  const onSubmit = (formData) => {
    const comment = formData.comment;
    var userId = currentUser.id;
    axiosInstance.post("/studies/" + props.studyId + "/comments", {
      userId: userId,
      content: comment
    })
    .then(() => {
      setTriggerUpdate(!triggerUpdate);
    });
  };

  const DiscussionTable = () => {
    return (
      <Table>
        <thead>
          <th>Date</th>
          <th>Author</th>
          <th style={{borderLeft: '0.01px solid black'}}>Comment</th>
        </thead>
        <tbody>
          { comments.map(comment => (
            <tr>
              <td style={{ width: '20%'}}>{comment.date}</td>
              <td style={{ width: '30%'}}>{comment.author}</td>
              <td style={{ width: '50%', borderLeft: '0.01px solid black'}}>{comment.content}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    )
  }

  return (
    <div className='slrspot__studyDiscussion'>
      { comments.length > 0 
        ? <DiscussionTable /> 
        : <h2 style={{ alignSelf: 'center', marginTop: '40px'}}>START DISCUSSION</h2>}
      { props.allowChanges &&      
        <form className='slrspot__studyDiscussion-comment' onSubmit={handleSubmit(onSubmit)} >
          <textarea 
            {...register("comment")}
            name='comment' 
            required/>
          <button type='submit'>comment</button>
        </form>
      }
    </div>
  )
}

export default StudyDiscussion