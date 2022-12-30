import React, { useState, useEffect } from 'react';
import { Table } from 'react-bootstrap';
import { useForm } from "react-hook-form";
import { AiFillFolder, AiFillFileText } from 'react-icons/ai';
import { AiOutlineCheck, AiOutlineClose, AiFillPlusSquare } from 'react-icons/ai';
import axiosInstance from '../../services/api';
import { useParams } from "react-router-dom";
import EventBus from '../../common/EventBus';
import './folder.css';
import StudyFolderItem from '../study/studyFolderItem/StudyFolderItem';
import ReactPaginate from 'react-paginate';

const Folder = (props) => {
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isExpanded, setIsExpanded] = useState(false);
  const [showInput, setShowInput] = useState(false);

  const [id, setIdentifier] = useState(props.id);
  const [name, setName] = useState(props.name);
  const [children, setChildren] = useState(props.children);
  const [childrenStudies, setChildrenStudies] = useState([]);
  const { reviewId } = useParams();

  const [currentPage, setCurrentPage] = useState(0);
  const [pageCount, setPageCount] = useState(0);

  useEffect(() => {
    if (isExpanded) {
      setCurrentPage(0);
      axiosInstance.get("/studies/by-folder/" + id, { params: {
        reviewId
      }})
      .then((response) => {
        console.log(response)
        setChildrenStudies(response.data.content);
        setPageCount(response.data.totalPages);
      })
      .catch(() => {
      });
    }
  }, [isExpanded]);


  const handleNewFolder = (formData) => {
    axiosInstance.post("/folders", {
      name: formData.folderName,
      parentId: id,
      reviewId: reviewId
    })
    .then((response) => {
      var folder = {
        "id": response.data,
        "name": formData.folderName,
        "children": [],
        "studies": []
      }; 
      setChildren(oldArray => [...oldArray, folder]);
      setShowInput(false);
    })
    .catch(() => {
    });
  }

  const handleRemoveFolder = (folderId) => {
    axiosInstance.delete("/folders/" + folderId)
    .then(() => {
      props.triggerRemove(props.parentFolders.filter(item => item.id !== folderId));
    })
    .catch((error) => {
      if (error.response && error.response.status === 403) {
        EventBus.dispatch('expirationLogout');
      }
    });
  }

  const handleClick = () => {
    if (isExpanded && showInput) {
      setShowInput(false);    
    }
    setIsExpanded(!isExpanded);
  }

  const handlePageChange = (studyPage) => {
    var page = studyPage.selected;
    setCurrentPage(page);
    axiosInstance.get("/studies/by-folder/" + id, { params: {
      reviewId, page
    }})
    .then((response) => {
      setChildrenStudies(response.data.content);
    })
    .catch(() => {
    });
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
          <AiFillFolder style={{ "marginRight": '5px'}}/>
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

            { childrenStudies.length > 0 && childrenStudies.map((study, idx) => (
              <tr key={idx}>
                  <td>
                    <StudyFolderItem study={study} />
                  </td>
              </tr>
            ))}
            { childrenStudies.length > 0 && pageCount > 1 &&
              <ReactPaginate
                pageCount={pageCount}
                pageRangeDisplayed={5}
                marginPagesDisplayed={2}
                onPageChange={handlePageChange}
                forcePage={currentPage}
                containerClassName="slrspot__folder-pagination"
                activeClassName="slrspot__folder-pagination-active"
              />
            }

            { children && children.map((folder) => (
              <tr key={folder.id}>
                <td>
                  <Folder 
                    id={folder.id}
                    name={folder.name}
                    parentFolders={children}
                    triggerRemove={setChildren}
                    children={folder.children} 
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