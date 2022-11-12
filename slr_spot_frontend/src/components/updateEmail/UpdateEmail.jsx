import React, { useState, useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { logout } from "../../actions/auth";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../../services/api";
import TokenService from "../../services/token.service";
import { refreshToken } from "../../actions/auth";



const UpdateEmail = () => {
  const { confirmationToken } = useParams();
  const { user: currentUser } = useSelector((state) => state.auth);
  const dispatch = useDispatch();

  useEffect(() => {
    axiosInstance.get("/users/" + currentUser.id + "/email/update/confirm", { params: {
      confirmationToken 
    }})
    .then(() => {
      console.log("New email confirmed");
    })
    .catch(() => {
      console.log("Error while confirming new email");
    });
  }, []);

  return (
    <div>UpdateEmail</div>
  )
}

export default UpdateEmail