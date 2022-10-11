import React from 'react';
import { Route, Routes } from 'react-router-dom'
import UsersPage from "./components/usersPage/UsersPage";
import App from './App';

export default () => (
  <Routes>
    <Route path="/" element={<App />}>
      <Route path="test" element={<UsersPage />} />
    </Route>
    <Route path="users" element={<UsersPage />} />
  </Routes>
);