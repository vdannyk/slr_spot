import React, { useState, useEffect } from 'react';
import FolderTree from '../../../../../components/folder/FolderTree';
import { BeatLoader } from "react-spinners";
import './foldersView.css';
import FolderService from '../../../../../services/folder_service';
import { useParams } from "react-router-dom";


const FoldersView = ({allowChanges}) => {
  const [loading, setLoading] = useState(false);
  const [folders, setFolders] = useState([]);
  const { reviewId } = useParams();

  useEffect(() => {
    setLoading(true);
    FolderService.getFolderTree(reviewId)
    .then( (response) => {
      setFolders(response.data)
      setLoading(false);
    });
  }, []);

  return (
    <div>
      { loading 
        ? <BeatLoader /> 
        : <FolderTree folders={folders} foldersChange={setFolders} allowChanges={ allowChanges }/>}
    </div>
  );
  
}

export default FoldersView