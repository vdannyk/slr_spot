import React, { useState, useEffect } from 'react';
import { Document, Page, pdfjs } from 'react-pdf';
import { BeatLoader } from 'react-spinners';

const PdfReader = (props) => {
  const [numPages, setNumPages] = useState(null);
  const { pdfUrl } = props;


  useEffect(() => {
    pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;
  }, []);

  function onDocumentLoadSuccess({ numPages }) {
    setNumPages(numPages);
  }

  return (
    <div>
      { pdfUrl 
      ? (
      <Document
      file={pdfUrl}
      onLoadSuccess={onDocumentLoadSuccess}>

        {Array.from(new Array(numPages), (el, index) => (
          <Page key={`page_${index + 1}`} pageNumber={index + 1} />
        ))}

      </Document>
      ) 
      : (
        <BeatLoader color="#AE67FA"/>
      )}
      
    </div>
  );
}

export default PdfReader