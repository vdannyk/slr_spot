import React from 'react';
import { AiTwotoneMail } from "react-icons/ai";

const ContactPage = () => {
  return (
    <div className="slrspot__homePage gradient__bg">

      <div className='slrspot__home-container'>

        <div className='slrspot__home-header'>
          <h1>Contact us</h1>
        </div>

        <div className='slrspot__home-content'>

          <div className='slrspot__home-features'>

            <div className='slrspot__home-feature'>
              <p><b>Feel free to say hello and share your feedback about using the application or suggest new features.</b></p>
              <p style={{ color: '#AE97FA' }}><AiTwotoneMail /> slrspot.welcome@gmail.com </p>
            </div>

            <div className='slrspot__home-feature'>
              <p><b>Have a question for the author? Are you interested in the technical aspects of the application? I would be happy to answer these questions.</b></p>
              <p style={{ color: '#AE97FA' }}><AiTwotoneMail /> dkwasniak.it@gmail.com </p>
            </div>
          </div>

        </div>
      </div>

    </div>
  )
}

export default ContactPage