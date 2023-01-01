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
import { AWAITING, CONFLICTED, EXCLUDED, TO_BE_REVIEWED } from '../../constants/tabs';
import { useSelector } from "react-redux";
import ScreeningStudyInFolder from '../screening/screeningStudy/ScreeningStudyInFolder';
import ContentPopup from '../popups/contentPopup/ContentPopup';


const Folder = (props) => {
  const {register, handleSubmit, formState: { errors }} = useForm();
  const [isExpanded, setIsExpanded] = useState(false);
  const [showInput, setShowInput] = useState(false);

  const [id, setIdentifier] = useState(props.id);
  const [name, setName] = useState(props.name);
  const [children, setChildren] = useState(props.children);
  const [childrenStudies, setChildrenStudies] = useState([]);
  const { reviewId } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageCount, setPageCount] = useState(0);

  const [showStudy, setShowStudy] = useState(false);
  const [studyToShow, setStudyToShow] = useState(false);


  function selectStudies() {
    var userId = currentUser.id;
    var stage;
    if (props.isFullText) {
      stage = "FULL_TEXT";
    } else {
      stage = "TITLE_ABSTRACT";
    }
    switch(props.tab) {
      case TO_BE_REVIEWED:
        axiosInstance.get("/studies/to-be-reviewed/by-folder/" + id, { params: {
          reviewId, userId, stage
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      case CONFLICTED:
        axiosInstance.get("/studies/conflicted/by-folder/" + id, { params: {
          reviewId, stage
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      case AWAITING:
        axiosInstance.get("/studies/awaiting/by-folder/" + id, { params: {
          reviewId, userId, stage
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      case EXCLUDED:
        axiosInstance.get("/studies/excluded/by-folder/" + id, { params: {
          reviewId, stage
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      default:
        axiosInstance.get("/studies/by-folder/" + id, { params: {
          reviewId
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
    }
  }

  useEffect(() => {
    if (isExpanded) {
      setCurrentPage(0);
      selectStudies()
    }
  }, [isExpanded, props.tab, props.isFullText]);


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
    var userId = currentUser.id;
    var stage;
    if (props.isFullText) {
      stage = "FULL_TEXT";
    } else {
      stage = "TITLE_ABSTRACT";
    }
    switch(props.tab) {
      case TO_BE_REVIEWED:
        axiosInstance.get("/studies/to-be-reviewed/by-folder/" + id, { params: {
          reviewId, userId, stage, page
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      case CONFLICTED:
        axiosInstance.get("/studies/conflicted/by-folder/" + id, { params: {
          reviewId, stage, page
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      case AWAITING:
        axiosInstance.get("/studies/awaiting/by-folder/" + id, { params: {
          reviewId, userId, stage, page
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      case EXCLUDED:
        axiosInstance.get("/studies/excluded/by-folder/" + id, { params: {
          reviewId, stage, page
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
      default:
        axiosInstance.get("/studies/by-folder/" + id, { params: {
          reviewId, page
        }})
        .then((response) => {
          setChildrenStudies(response.data.content);
          setPageCount(response.data.totalPages);
        })
        return;
    }
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

  const handleShowStudy = (study) => {
    setShowStudy(true);
    setStudyToShow(study);
  }

  const handleStudiesUpdate = (id) => {
    setChildrenStudies(childrenStudies.filter(study => study.id !== id));
    setShowStudy(false);
  }

  return (
    <div>
      <div className='slrspot__folder-item'>
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
                    <StudyFolderItem study={study} handleShowStudy={ handleShowStudy } />
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
                    isScreening={props.isScreening}
                    tab={ props.tab }
                    isFullText={ props.isFullText }
                    userRole={ props.userRole }
                    reviewTags={ props.reviewTags }
                    teamHighlights={ props.teamHighlights }
                    personalHighlights={ props.personalHighlights }
                  />
                </td>
              </tr>
            )) }

            <NewFolder />

          </tbody>
        </Table>
      )}

      { showStudy &&
        <ContentPopup
          isWide={ true }
          content={ 
            <ScreeningStudyInFolder 
              study={ studyToShow }
              triggerVote={ handleStudiesUpdate }
              tab={ props.tab }
              isFullText={ props.isFullText }
              userRole={ props.userRole }
              reviewTags={ props.reviewTags }
              teamHighlights={ props.teamHighlights }
              personalHighlights={ props.personalHighlights }
            /> 
          } 
          triggerExit={ () => setShowStudy(false) }/>
      }
    </div>
  )
}

export default Folder