import React from 'react';

const AboutPage = () => {
  return (
    <div className="slrspot__homePage gradient__bg">

      <div className='slrspot__home-container'>

        <div className='slrspot__home-header'>
          <h1>WHAT IS SLR SPOT?</h1>
        </div>

        <div className='slrspot__home-content'>

          <div className='slrspot__home-features' style={ { width: '75%' }}>

            <div className='slrspot__home-feature'>
              <p><b>Systematic literature review</b></p>
              <p>A commonly used method of literature review. It has a organized structure and allows for a critical evaluation and summary of research, ultimately resulting in finding answers to clearly formulated research questions that pertain to a specific area of study. A properly conducted SLR is repeatable and objective.</p>
            </div>
            <div className='slrspot__home-feature'>
              <p><b>Thesis</b></p>
              <p>The project was created as a thesis, the subject of which was the implementation of a web application supporting the systematic literature review process. Thesis was completed at Faculty of Electronics and Information Technology, Warsaw University of Technology.</p>
            </div>
          </div>


        </div>
      </div>

    </div>
  )
}

export default AboutPage