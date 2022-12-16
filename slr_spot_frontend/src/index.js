import React from 'react';
import { createRoot } from "react-dom/client";
import store from "./store";
import { Provider } from "react-redux";
import setupInterceptors from './services/setupInterceptors';
import App from './App'
import './index.css';


const root = createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <App />
    </Provider>
  </React.StrictMode>
);

setupInterceptors(store);
