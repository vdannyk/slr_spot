import React, {useEffect, useState} from 'react';
import { AiFillMinusCircle, AiFillPlusCircle, AiFillCheckSquare, AiFillCloseSquare } from "react-icons/ai";
import axiosInstance from '../../../../services/api';
import DropdownSelect from '../../../dropdownSelect/DropdownSelect';
import './studyTags.css';

const StudyTags = (props) => {
  const [tags, setTags] = useState([]);
  const [showAdd, setShowAdd] = useState(false);
  const [selectedTag, setSelectedTag] = useState('');

  useEffect(() => {
    var studyId = props.studyId;
    axiosInstance.get("/studies/" + studyId + "/tags")
    .then((response) => {
      setTags(response.data);
    })
    .catch(() => {
    });
  }, []);

  const handleRemoveTag = (tag) => {
    var studyId = props.studyId;
    axiosInstance.delete("/studies/" + studyId + "/tags/" + tag.id)
    .then(() => {
      setTags(tags.filter(item => item.id !== tag.id));
    })
    .catch(() => {
    });
  }

  const handleAddTag = () => {
    var tag = props.reviewTags.find(tag => {
      return tag.name === selectedTag
    });
    var tagId = tag.id;
    var studyId = props.studyId;
    axiosInstance.post("/studies/" + studyId + "/tags", null, { params: {
      studyId, tagId
    }})
    .then(() => {
      setTags(oldArray => [...oldArray, tag]);
      setSelectedTag('');
    })
    .catch(() => {
      setSelectedTag('');
    });
  }

  const handleSelect = (event) => {
    setSelectedTag(event);
  }

  const AddTagField = () => {
    return (
      <div className='slrspot__study-tags-new'>
        <DropdownSelect
          value={ selectedTag }
          onSelect={ handleSelect }
          options={ props.reviewTags.map(t => t.name).filter(t => !tags.map(ta => ta.name).includes(t)) }
          title="Select tag"
        />
        <AiFillCheckSquare 
          className='slrspot__study-tags-addIcon' 
          color='green'
          size={25}
          onClick={ handleAddTag }/>
        <AiFillCloseSquare 
          className='slrspot__study-tags-removeIcon' 
          color='red'
          size={24}
          onClick={ () => setShowAdd(false) }/>
      </div>
    )
  }

  return (
    <div className='slrspot__study-tags'>
      <div className='slrspot__study-tags-container'>
        { tags.length > 0 && tags.map((tag) => (
          <p key={ tag.name } className='slrspot__study-tags-tag'>
            { tag.name }
            <AiFillMinusCircle 
              className='slrspot__study-tags-removeIcon'
              onClick={ () => handleRemoveTag(tag) }/>
          </p>
        ))}
        <p className='slrspot__study-tags-tag'>
          { showAdd 
            ? <AddTagField />
            :
            <> 
              add
              <AiFillPlusCircle 
                className='slrspot__study-tags-addIcon'
                onClick={ () => setShowAdd(true) }
                />
            </>
          }

        </p>
      </div>
    </div>
  )
}

export default StudyTags