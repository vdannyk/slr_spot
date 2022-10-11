import React, { useState, useEffect } from "react";
import UserService from "../../services/user.service";
import { Navbar } from '../index';


const UsersPage = () => {
  const [content, setContent] = useState([]);

  useEffect(() => {
    UserService.getAdminBoard().then(
      (response) => {
        setContent(response.data);
      },
      (error) => {
        const _content =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();

        setContent(_content);
      }
    );
  }, []);

  return (
    <div className="container">
      <Navbar />
      <header className="jumbotron">
        <h3>{content.map((d) => <li key={d.id}>{d.email}</li>)}</h3>
      </header>
    </div>
  )
}

export default UsersPage