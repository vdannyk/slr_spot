import { Navbar } from './components';
import AppRoutes from './AppRoutes';
import { BrowserRouter as Router } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css"
import './App.css';
import { Footer } from './containers';


function App() {
  return (
    <div className="slrspot__app">
      <Router>
        <Navbar />
        <AppRoutes />
        <Footer />
      </Router>
    </div>
  );
}

export default App;
