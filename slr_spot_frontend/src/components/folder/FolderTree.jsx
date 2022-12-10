import React, { useState, useEffect } from 'react';
import { Table } from "react-bootstrap";
import { AiOutlineCheck, AiOutlineClose } from 'react-icons/ai'
import { useForm } from "react-hook-form";
import Folder from './Folder';

const FolderTree = (props) => {
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [showInput, setShowInput] = useState(false);

  const handleNewFolder = (formData) => {
    // useeffect
    console.log(formData.folderName);
    var folder = { 
      "name": formData.folderName,
      "children": [],
      "childrenStudies": []
    };
    props.foldersChange(oldArray => [...oldArray, folder]);
    setShowInput(false);
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
      return (
        <div  className='slrspot__folderTree-newFolder'>
          <button onClick={ () => setShowInput(true) }>New folder</button>
        </div>
      )
    }
  };

  return (
    <div>
      <NewFolder />
      <Table>
        <tbody>
          { props.folders.map((folder) => (
            <tr key={folder.id}>
              <td>
                <Folder 
                  id={folder.id}
                  name={folder.name}
                  parentFolders={props.folders}
                  children={folder.children} 
                  childrenStudies={folder.childrenStudies} 
                  triggerRemove={props.foldersChange}/>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  )
}

export default FolderTree