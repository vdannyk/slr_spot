import React from 'react'
import { useState } from 'react';
import { DownloadButton, Helper } from '../../../components';
import axiosInstance from '../../../services/api';
import { AiFillCheckCircle } from "react-icons/ai";

const ExtractData = (props) => {
  const [fileUrl, setFileUrl] = useState();
  const [selected, setSelected] = useState([]);

  const handleExtraction = () => {
    axiosInstance.post('data_extraction', { 
      fields: selected,
      studies: props.selectedStudies
    })
    .then(response => {
      const csvData = response.data;
      const blob = new Blob([csvData], { type: 'text/csv' });
      setFileUrl(URL.createObjectURL(blob));
    })
  }

  const FieldItem = (props) => {
    const [isSelected, setIsSelected] = useState(false);

    const handleClick = () => {
      if (selected.includes(props.value)) {
        setSelected(selected.filter(item => item !== props.value));
      } else {
        setSelected(oldArray => [...oldArray, props.value]);
      }
    }

    return (
      <div className='slrspot__extractData-field' onClick={ () => { handleClick(); setIsSelected(!isSelected)}}>
        <p>{props.name}{ isSelected ? <AiFillCheckCircle color='green' style={{ marginLeft: '5px' }}/> 
                                    : <AiFillCheckCircle style={{ marginLeft: '5px' }} />} </p>
      </div>
    )
  }

  return (
    <div className='slrspot__extractData'>
      <p>Select fields which you want to extract</p>
      <FieldItem name="authors" value='AUTHORS'/>
      <FieldItem name="title" value='TITLE'/>
      <FieldItem name="journal" value='JOURNAL'/>
      <FieldItem name="publication year" value='PUBLICATIONYEAR'/>
      <FieldItem name="abstract" value='ABSTRACT'/>
      <FieldItem name="doi" value='DOI'/>

      <DownloadButton name='Extract data' fileUrl={fileUrl} handleExtraction={handleExtraction} />
    </div>
  )
}

export default ExtractData