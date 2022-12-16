import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../../services/api';
import FolderTree from '../../../../../components/folder/FolderTree';
import { BeatLoader } from "react-spinners";
import './foldersView.css';
import FolderService from '../../../../../services/folder_service';


const FoldersView = () => {
  const [loading, setLoading] = useState(false);
  const [folders, setFolders] = useState([]);

  useEffect(() => {
    setLoading(true);
    FolderService.getFolderTree()
    .then( (response) => {
      setFolders(response.data)
      setLoading(false);
    });
  }, []);

  return (
    <div>
      { loading 
        ? <BeatLoader /> 
        : <FolderTree folders={folders} foldersChange={setFolders} />}
    </div>
  );
  
}

export default FoldersView