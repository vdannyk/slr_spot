import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom'
import { useSelector } from "react-redux";
import { ReviewHome, ReviewTeam, ReviewSettings, HomePage, Profile, Review, Reviews, ScreeningSettings, StudiesSearch, StudiesDisplay, Screening } from './containers';
import { NewReview, SignInPage, AccountActivation, PasswordRecovery, UpdateEmail, Criteria, Tags, Keywords } from './components';


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
      <Route path="reviews/:reviewId" element={isLoggedIn ? <Review isPreview={false} page={<ReviewHome />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/preview" element={isLoggedIn ? <Review isPreview={true} page={<ReviewHome />} /> : <Navigate to='/' />} />

      <Route path="reviews/:reviewId/settings" element={isLoggedIn ? <Review page={<ReviewSettings />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/team" element={isLoggedIn ? <Review page={<ReviewTeam />} /> : <Navigate to='/' />} />

      <Route path="reviews/:reviewId/screening" element={isLoggedIn ? <Review page={<ScreeningSettings />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/criteria" element={isLoggedIn ? <Review page={<Criteria />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/tags" element={isLoggedIn ? <Review page={<Tags />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/keywords" element={isLoggedIn ? <Review page={<Keywords showAll={true} />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/keywords/personal" element={isLoggedIn ? <Review page={<Keywords showAll={false} />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/title_abstract" element={isLoggedIn ? <Review page={<Screening isFulltext={false} />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/full-text" element={isLoggedIn ? <Review page={<Screening isFulltext={true} />} /> : <Navigate to='/' />} />

      <Route path="reviews/:reviewId/studies/search" element={isLoggedIn ? <Review page={<StudiesSearch />} /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/studies/display" element={isLoggedIn ? <Review page={<StudiesDisplay />} /> : <Navigate to='/' />} />
    </Routes>
  )
}

export default AppRoutes;