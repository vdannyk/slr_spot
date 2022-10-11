import React from 'react';
import { Route, Routes } from 'react-router-dom'
import UsersPage from "./components/usersPage/UsersPage";
import PasswordRecovery from './components/passwordRecovery/PasswordRecovery';
import { HomePage } from './containers';

export default () => (
  <Routes>
    <Route path="/" element={<HomePage />} />
    <Route path="users" element={<UsersPage />} />
    <Route path="password-recovery" element={<PasswordRecovery />} />
  </Routes>
);