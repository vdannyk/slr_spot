import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom'
import { useSelector } from "react-redux";
import { ReviewHome, ReviewTeam, ReviewSettings, HomePage, Profile, Review, Reviews } from './containers';
import { NewReview, SignInPage, AccountActivation, PasswordRecovery, UpdateEmail } from './components';


const AppRoutes = () => {
  const { isLoggedIn } = useSelector(state => state.auth);

  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="signin" element={<SignInPage />} />
      <Route path="profile" element={isLoggedIn ? <Profile /> : <Navigate to='/' />} />
      <Route path="password-recovery/:resetToken" element={<PasswordRecovery />} />
      <Route path="activate/:confirmationToken" element={<AccountActivation />} />
      <Route path="email/confirm/:confirmationToken" element={isLoggedIn ? <UpdateEmail /> : <Navigate to='/' />} />
      <Route path="reviews" element={isLoggedIn ? <Reviews /> : <Navigate to='/' />} />
      <Route path="reviews/new" element={isLoggedIn ? <NewReview />: <Navigate to='/' />} />
      <Route path="reviews/:reviewId" element={isLoggedIn ? <Review page={<ReviewHome />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/settings" element={isLoggedIn ? <Review page={<ReviewSettings />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/team" element={isLoggedIn ? <Review page={<ReviewTeam />} /> : <Navigate to='/' />} />
    </Routes>
  )
}

export default AppRoutes;