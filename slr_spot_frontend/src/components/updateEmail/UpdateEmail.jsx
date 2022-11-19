import React, { useState, useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../actions/auth";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import TokenService from "../../services/token.service";
import { refreshToken } from "../../actions/auth";
import EventBus from "../../common/EventBus";
import './updateEmail.css'



const UpdateEmail = () => {
  const { confirmationToken } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const dispatch = useDispatch();
  const [counter, setCounter] = useState(5);
  const navigate = useNavigate();

  React.useEffect(() => {
    const timer =
      counter > 0 && setInterval(() => setCounter(counter - 1), 1000);
    return () => clearInterval(timer);
  }, [counter]);

  if (counter <= 0) {
    navigate('/');
  }

  useEffect(() => {
    axiosInstance.get("/users/" + currentUser.id + "/email/update/confirm", { params: {
      confirmationToken 
    }})
    .then(() => {
      EventBus.dispatch('logout');
    })
    .catch(() => {
      console.log("Error while confirming new email");
    });
  }, []);

  return (
    <div className="slrspot__updateEmail">
      <div className='slrspot__updateEmail-popup'>
        <p>You have to sign in again after changing email.</p>
        <p>You will be redirected to the home page automatically in: {counter}</p>
        <button>Redirect now</button>
      </div>
    </div>
  )
}

export default UpdateEmail