import { Header, Features, Footer, WhatSLRSpot } from './containers';
import { Navbar } from './components';

import './App.css';

function App() {
  return (
    <div className="App">
      <div className='gradient__bg'>
        <Navbar />
        <Header />
      </div>
      <WhatSLRSpot />
      <Features />
      <Footer />
    </div>
  );
}

export default App;
