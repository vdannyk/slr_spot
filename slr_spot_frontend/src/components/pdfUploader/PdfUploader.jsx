import React, { useRef, useState } from 'react';
import axiosInstance from '../../services/api';
import './pdfUploader.css';

const PdfUploader = (props) => {
  const fileInput = useRef(null);
  const [selectedFile, setSelectedFile] = useState('Select file');
  const [progress, setProgress] = useState(0);

  const handleSubmit = (event) => {
    event.preventDefault();

    // setSelectedFile(event.target.files[0].name)

    const formData = new FormData();
    formData.append('file', fileInput.current.files[0]);

    axiosInstance.post('/studies/' + props.studyId + "/full-text", formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: (event) => {
        setProgress(Math.round((event.loaded * 100) / event.total));
      }
    })
    .then(response => {
      props.setIsLoaded(true);
    })
  }

  return (
    <div>
      <label className='custom-file-upload'>
        { selectedFile }
        <input type="file" ref={fileInput} onChange={ handleSubmit } />
      </label>
      {progress > 0 && progress < 100 && (
        <div className="progress-bar">
          <div
            className="progress"
            style={{ width: `${progress}%` }}
          />
        </div>
      )}
    </div>
  )
}

export default PdfUploader