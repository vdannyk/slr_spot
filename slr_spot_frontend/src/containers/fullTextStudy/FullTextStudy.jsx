import React from 'react'
import PdfReader from '../../components/pdfReader/PdfReader';
import samplePDF from './testpd.pdf';
import OptionHeader from '../../components/screening/optionHeader/OptionHeader';
import './fullTextStudy.css';

const FullTextStudy = () => {
  return (
    <div className='slrspot__fullTextStudy'>
      <OptionHeader content='Study full text'/>
      <div className='all-page-container'>
        <PdfReader pdf={samplePDF}/>
      </div>
    </div>
  )
}

export default FullTextStudy