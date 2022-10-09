import React from 'react';
import { createRoot } from "react-dom/client";
import './index.css';
import store from "./store";
import { Provider } from "react-redux";
import { BrowserRouter as Router } from "react-router-dom";
import AppRoutes from './AppRoutes';
import setupInterceptors from './services/setupInterceptors';


const root = createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <Router>
        <AppRoutes />
      </Router>
    </Provider>
  </React.StrictMode>
);

setupInterceptors(store);
