import { Navbar } from './components';
import AppRoutes from './AppRoutes';
import { BrowserRouter as Router } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css"
import './App.css';


function App() {
  return (
    <div className="App">
      <Router>
        <Navbar />
        <AppRoutes />
      </Router>
    </div>
  );
}

export default App;
