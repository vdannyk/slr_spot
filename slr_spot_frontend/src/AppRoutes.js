import React from 'react';
import { Route, Routes } from 'react-router-dom'
import { HomePage, Profile, Reviews } from './containers';
import { NewReview, SignInPage, AccountActivation, PasswordRecovery } from './components';

export default () => (
  <Routes>
    <Route path="/" element={<HomePage />} />
    <Route path="signin" element={<SignInPage />} />
    <Route path="profile" element={<Profile />} />
    <Route path="password-recovery/:resetToken" element={<PasswordRecovery />} />
    <Route path="activate/:activationToken" element={<AccountActivation />} />
    <Route path="reviews" element={<Reviews />} />
    <Route path="reviews/new" element={<NewReview />} />
  </Routes>
);