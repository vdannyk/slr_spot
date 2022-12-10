import React, { useState } from 'react';
import { Table } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import { AiFillFolder, AiFillFileText } from 'react-icons/ai';
import { AiOutlineCheck, AiOutlineClose, AiFillPlusSquare } from 'react-icons/ai'
import './folder.css';

const Folder = (props) => {
  const {register, handleSubmit, formState: { errors }} = useForm();
  // const { name, children, childrenStudies } = props;
  const [isExpanded, setIsExpanded] = useState(false);
  const [showInput, setShowInput] = useState(false);

  const [id, setIdentifier] = useState(props.id);
  const [name, setName] = useState(props.name);
  // const [parentFolders, setParentFolders] = useState(props.parentFolders);
  const [children, setChildren] = useState(props.children);
  const [childrenStudies, setChildrenStudies] = useState(props.childrenStudies);


  const handleNewFolder = (formData) => {
    // useeffect
    console.log(formData.folderName);
    var folder = {
      "name": formData.folderName,
      "children": [],
      "childrenStudies": []
    }; 
    setChildren(oldArray => [...oldArray, folder]);
    setShowInput(false);
  }

  const handleRemoveFolder = (folder) => {
    console.log('removing')
    console.log(folder);
    console.log(props.parentFolders);
    var test = props.parentFolders.filter(item => item.id !== folder);
    console.log(test);
    props.triggerRemove(test);
  }

  const handleClick = () => {
    if (isExpanded && showInput) {
      setShowInput(false);    
    }
    setIsExpanded(!isExpanded);
  }

  const NewFolder = () => {
    return showInput && (
      <tr>
        <td>
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
        </td>
      </tr>
    )
  };

  return (
    <div>
      <div  className='slrspot__folder-item'>
        <p onClick={handleClick}>
          <AiFillFolder style={{ "margin-right": '5px'}}/>
          {name}
        </p>
        { !props.isScreening && (
          <p>
            <AiFillPlusSquare 
              className='slrspot__folder-option' 
              onClick={ () => { setShowInput(true); setIsExpanded(true); } } />
            <AiOutlineClose 
              className='slrspot__folder-option' 
              onClick={ () => handleRemoveFolder(id) } />
          </p>   
        )}
      </div>

      {isExpanded && (
        <Table>
          <tbody>

            { childrenStudies && childrenStudies.map((study, idx) => (
              <tr key={idx}>
                <td>
                  <p><AiFillFileText />{study.name}</p>
                </td>
            </tr>
            ))}

            { children && children.map((folder) => (
              <tr key={folder.id}>
                <td>
                  <Folder 
                    id={folder.id} 
                    name={folder.name}
                    parentFolders={children}
                    triggerRemove={setChildren}
                    children={folder.children} 
                    childrenStudies={folder.childrenStudies} 
                    isScreening={props.isScreening}/>
                </td>
              </tr>
            )) }

            <NewFolder />

          </tbody>
        </Table>
      )}

    </div>
  )
}

export default Folder