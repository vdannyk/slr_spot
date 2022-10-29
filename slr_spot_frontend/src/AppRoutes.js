import React from 'react';
import { Route, Routes } from 'react-router-dom'
import UsersPage from "./components/usersPage/UsersPage";
import PasswordRecovery from './components/passwordRecovery/PasswordRecovery';
import { HomePage, Reviews } from './containers';
import AccountActivation from './components/accountActivation/AccountActivation';
import SignInPage from './components/signIn/SignInPage';

export default () => (
  <Routes>
    <Route path="/" element={<HomePage />} />
    <Route path="signin" element={<SignInPage />} />
    <Route path="users" element={<UsersPage />} />
    <Route path="password-recovery/:resetToken" element={<PasswordRecovery />} />
    <Route path="activate/:activationToken" element={<AccountActivation />} />
    <Route path="reviews" element={<Reviews />} />
  </Routes>
);