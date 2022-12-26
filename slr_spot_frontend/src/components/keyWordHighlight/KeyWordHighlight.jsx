import React from 'react';
import './keyWordHighlight.css';

const KeyWordHighlight = ({ inclusionWords, exclusionWords, text }) => {
  const words = text && text.length > 0 ? text.split(" ") : [];
  let inclusionWordsList = inclusionWords.map((word) => word.toLowerCase());
  let exclusionWordsList = exclusionWords.map((word) => word.toLowerCase());

  const highlightedText = words.map((word) => {
    if (inclusionWordsList.includes(word.toLowerCase())) {
      return <span className='inclusion_highlight'>{word} </span>
    } else if (exclusionWordsList.includes(word.toLowerCase())) {
      return <span className='exclusion_highlight'>{word} </span>
    } else {
      return word + " ";
    }
  });

  return highlightedText;
}

export default KeyWordHighlight