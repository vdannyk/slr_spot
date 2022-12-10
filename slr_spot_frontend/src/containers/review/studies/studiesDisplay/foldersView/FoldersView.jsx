import React, { useState, useEffect } from 'react';
import axiosInstance from '../../../../../services/api';
import FolderTree from '../../../../../components/folder/FolderTree';
import { BeatLoader } from "react-spinners";
import './foldersView.css';


const FoldersView = () => {
  const [loading, setLoading] = useState(false);
  const [folders, setFolders] = useState([]);

  useEffect(() => {
    setLoading(true);
    axiosInstance.get("/folders")
    .then((response) => {
      console.log(response.data);
      setFolders(response.data)
      setLoading(false);
    })
    .catch(() => {
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