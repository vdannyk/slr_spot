import React, { useState } from 'react';
import { Table } from "react-bootstrap";
import { AiOutlineCheck, AiOutlineClose } from 'react-icons/ai'
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";
import axiosInstance from '../../services/api';
import Folder from './Folder';


const FolderTree = (props) => {
  const {register, handleSubmit, formState: { errors }, reset} = useForm();
  const [showInput, setShowInput] = useState(false);
  const { reviewId } = useParams();

  const handleNewFolder = (formData) => {
    axiosInstance.post("/folders", {
      name: formData.folderName,
      reviewId: reviewId
    })
    .then((response) => {
      var folder = {
        "id": response.data,
        "name": formData.folderName,
        "children": [],
        "studies": []
      };
      props.foldersChange(oldArray => [...oldArray, folder]);
      setShowInput(false);
      reset();
    })
    .catch(() => {
    });
  }

  const NewFolder = () => {
    if (showInput) {
      return (
        <div className='slrspot__folderTree-newFolder'>
          <input 
            {...register("folderName", { 
              required: true,
            })}
            placeholder='Name'
          />
          <div className='slrspot__folderTree-newFolder-buttons'>
            <span><AiOutlineCheck onClick={handleSubmit(handleNewFolder)}/></span>
            <span><AiOutlineClose onClick={ () => setShowInput(false)}/></span>
          </div>
        </div>
      )
    } else {
      return props.allowChanges && (
        <div  className='slrspot__folderTree-newFolder'>
          <button onClick={ () => setShowInput(true) }>New folder</button>
        </div>
      )
    }
  };

  return (
    <div>
      { !props.isScreening && <NewFolder /> }
      <Table>
        <tbody>
          { props.folders.map((folder) => (
            <tr key={folder.id}>
              <td>
                <Folder 
                  id={ folder.id }
                  name={ folder.name }
                  parentFolders={ props.folders }
                  children={ folder.children } 
                  triggerRemove={ props.foldersChange }
                  isScreening={ props.isScreening }
                  tab={ props.tab }
                  isFullText={ props.isFullText }
                  userRole={ props.userRole }
                  reviewTags={ props.reviewTags }
                  teamHighlights={ props.teamHighlights }
                  personalHighlights={ props.personalHighlights }
                  allowChanges={ props.allowChanges }
                />
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  )
}

export default FolderTree