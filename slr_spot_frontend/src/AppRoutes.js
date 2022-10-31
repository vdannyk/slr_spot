import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom'
import { useSelector } from "react-redux";
import { HomePage, Profile, Reviews } from './containers';
import { NewReview, SignInPage, AccountActivation, PasswordRecovery } from './components';


const AppRoutes = () => {
  const { isLoggedIn } = useSelector(state => state.auth);

  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="signin" element={<SignInPage />} />
      <Route path="profile" element={isLoggedIn ? <Profile /> : <Navigate to='/' />} />
      <Route path="password-recovery/:resetToken" element={<PasswordRecovery />} />
      <Route path="activate/:confirmationToken" element={<AccountActivation />} />
      <Route path="reviews" element={isLoggedIn ? <Reviews /> : <Navigate to='/' />} />
      <Route path="reviews/new" element={isLoggedIn ? <NewReview />: <Navigate to='/' />} />
    </Routes>
  )
}

export default AppRoutes;