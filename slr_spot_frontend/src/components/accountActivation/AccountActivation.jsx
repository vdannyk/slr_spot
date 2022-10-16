import React, { useState, useRef } from "react";
import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import './accountActivation.css';


const AccountActivation = () => {
  const { activationToken } = useParams();
  const [isLoading, setLoading] = useState(true);
  const [successful, setSuccessful] = useState(false);

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
      <div className="slrspot___passwordRecovery">
        <div className="slrspot___passwordRecovery-box">
          <h1>Loading...</h1>
        </div>
      </div>
    );
  }

  if (!successful) {
    return (
      <div className="slrspot___passwordRecovery">
        <div className="slrspot___passwordRecovery-box">
          <h1>Failed</h1>
        </div>
      </div>
    );
  }

  return (
    <div className="slrspot___accountActivation">
      <div className="slrspot___accountActivation-box">
        <h1>Account activated</h1>
        <button>Log in</button>
      </div>
    </div>
  )
}

export default AccountActivation