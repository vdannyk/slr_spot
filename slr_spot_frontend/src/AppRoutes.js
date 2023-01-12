import React from 'react';
import { Navigate, Route, Routes } from 'react-router-dom'
import { useSelector } from "react-redux";
import { ReviewHome, ReviewTeam, ReviewSettings, HomePage, Profile, Review, Reviews, ScreeningSettings, StudiesSearch, StudiesDisplay, Screening, Duplicates, Results } from './containers';
import { NewReview, SignInPage, AccountActivation, PasswordRecovery, UpdateEmail, Criteria, Tags, Keywords } from './components';
import FullTextStudy from './containers/fullTextStudy/FullTextStudy';


const AppRoutes = () => {
  const { isLoggedIn } = useSelector(state => state.auth);

  return (
    <Routes>
      <Route path="/" element={<HomePage />} />

      <Route path="signin" element={!isLoggedIn ? <HomePage isAccessPopup={true} isSignInPopup={true} isSignUpPopup={false}/> : <Navigate to='/' />} />
      <Route path="signup" element={!isLoggedIn ? <HomePage isAccessPopup={true} isSignInPopup={false} isSignUpPopup={true}/> : <Navigate to='/' />} />
      <Route path="profile" element={isLoggedIn ? <Profile /> : <Navigate to='/' />} />
      <Route path="password-recovery/:resetToken" element={<PasswordRecovery />} />
      <Route path="activate/:confirmationToken" element={<AccountActivation />} />
      <Route path="email/confirm/:confirmationToken" element={isLoggedIn ? <UpdateEmail /> : <Navigate to='/' />} />

      <Route path="reviews" element={isLoggedIn ? <Reviews /> : <Navigate to='/' />} />
      <Route path="reviews/new" element={isLoggedIn ? <NewReview />: <Navigate to='/' />} />

      <Route path="reviews/:reviewId" element={isLoggedIn ? <Review page={ ReviewHome } /> : <Navigate to='/' />} />  // done 
      <Route path="reviews/:reviewId/settings" element={isLoggedIn ? <Review page={ ReviewSettings } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/team" element={isLoggedIn ? <Review page={ ReviewTeam } /> : <Navigate to='/' />} /> // done

      <Route path="reviews/:reviewId/screening" element={isLoggedIn ? <Review page={ ScreeningSettings } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/screening/criteria" element={isLoggedIn ? <Review page={ Criteria } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/screening/tags" element={isLoggedIn ? <Review page={ Tags } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/screening/keywords" element={isLoggedIn ? <Review page={ Keywords } props={ {showAll: true} }/> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/screening/keywords/personal" element={isLoggedIn ? <Review page={ Keywords} props={ {showAll: false} } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/screening/title_abstract" element={isLoggedIn ? <Review page={ Screening } props={ {isFullText: false} } /> : <Navigate to='/' />} />
      <Route path="reviews/:reviewId/screening/full-text" element={isLoggedIn ? <Review page={ Screening } props={ {isFullText: true} } /> : <Navigate to='/' />} />

      <Route path="reviews/:reviewId/studies/:studyId/full-text/:state" element={isLoggedIn ? <Review page={ FullTextStudy } /> : <Navigate to='/' />} />

      <Route path="reviews/:reviewId/studies/search" element={isLoggedIn ? <Review page={ StudiesSearch } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/studies/display" element={isLoggedIn ? <Review page={ StudiesDisplay } /> : <Navigate to='/' />} /> // done
      <Route path="reviews/:reviewId/studies/duplicates" element={isLoggedIn ? <Review page={ Duplicates } /> : <Navigate to='/' />} /> // done

      <Route path="reviews/:reviewId/results" element={isLoggedIn ? <Review page={ Results } /> : <Navigate to='/' />} /> // done
    </Routes>
  )
}

export default AppRoutes;