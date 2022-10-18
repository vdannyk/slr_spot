import React, { useState, useRef } from "react";
import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import { IoMdCheckmarkCircleOutline } from "react-icons/io";
import { RiErrorWarningLine } from "react-icons/ri";
import { BeatLoader } from "react-spinners";
import './accountActivation.css';


const AccountActivation = () => {
  const { activationToken } = useParams();
  const [isLoading, setLoading] = useState(true);
  const [successful, setSuccessful] = useState(false);
  const navigate = useNavigate();

  const onHomeClick = () => {
    navigate('/');
  };

  useEffect(() => {
    axiosInstance.get("/user/confirm", { params: {
      activationToken
    }})
    .then(() => {
      setLoading(false);
      setSuccessful(true);
    })
    .catch(() => {
      setLoading(false);
    });
  }, []);

  if (isLoading) {
    return (
      <div className="slrspot___accountActivation">
        <div className="slrspot___accountActivation-loading">
          <BeatLoader size={100} color="#AE67FA" />
        </div>
      </div>
    );
  }

  if (!successful) {
    return (
      <div className="slrspot___accountActivation">
        <div className="slrspot___accountActivation-box">
          <RiErrorWarningLine size={150} color='red' style={{ "margin-top": '50px'}}></RiErrorWarningLine>
          <h1>Account confirmation failed!</h1>
          <p>We were unable to activate the account using this link.</p>
          <button style={{ background:'red'}} onClick={onHomeClick}>Home</button>
        </div>
      </div>
    );
  }

  return (
    <div className="slrspot___accountActivation">
      <div className="slrspot___accountActivation-box">
        <IoMdCheckmarkCircleOutline size={150} color='#2ae158' style={{ "margin-top": '50px'}}></IoMdCheckmarkCircleOutline>
        <h1>Success!</h1>
        <p>You have successfuly confirmed account.</p>
        <button>Log in</button>
      </div>
    </div>
  )
}

export default AccountActivation