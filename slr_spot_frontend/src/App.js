import React, { useState, useCallback, useEffect } from "react";
import { Navbar, ExpirationLogout, TimeoutLogout } from './components';
import AppRoutes from './AppRoutes';
import { BrowserRouter as Router } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css"
import './App.css';
import { Footer } from './containers';
import { useDispatch, useSelector } from "react-redux";
import { logout } from "./actions/auth";
import EventBus from "./common/EventBus";



function App() {
  const dispatch = useDispatch();
  const [isExpirationLogout, setIsExpirationLogout] = useState(false); 


  const logOut = useCallback(() => {
    dispatch(logout());
  }, [dispatch]);

  useEffect(() => {
    EventBus.on("logout", () => {
      logOut();
    });

    EventBus.on("expirationLogout", () => {
      setIsExpirationLogout(true);
      logOut();
    });

    return () => {
      if (isExpirationLogout) {
        EventBus.remove("expirationLogout");
      } else {
        EventBus.remove("logout");
      }
    };
  }, [logOut]);

  return (
    <div className="slrspot__app">
      <Router>
        <Navbar />
        <AppRoutes />
        <Footer />
        { isExpirationLogout && <TimeoutLogout trigger={setIsExpirationLogout}/> }
      </Router>
    </div>
  );
}

export default App;
