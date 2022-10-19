import React, { useState, useEffect } from "react";
import UserService from "../../services/user.service";

const UsersPage = () => {
  const [content, setContent] = useState([]);

  useEffect(() => {
    UserService.getAdminBoard().then(
      (response) => {
        setContent(response.data);
      },
    );
  }, []);

  return (
    <div className="container">
      <header className="jumbotron">
        <h3>{content.map((d) => <li key={d.id}>{d.email}</li>)}</h3>
      </header>
    </div>
  )
}

export default UsersPage