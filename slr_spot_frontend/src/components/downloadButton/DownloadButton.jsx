import React from 'react'

const DownloadButton = (props) => {
  return (
    <a
      href={ props.fileUrl }
      download
      onClick={ props.handleExtraction }
    >
      { props.name }
    </a>
  )
}

export default DownloadButton