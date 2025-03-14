import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import './App.css';
import Navbar from './component/Navbar';
import Reports from './pages/Reports';
import UserStore from './store/ContextStore';
import MyReports from './pages/MyReports';
import InvestigatorReport from './pages/InvestigatorReport';
import UserProfile from './pages/UserProfile';
import InvestigatorKyc from './pages/InvestigatorKyc';
import Dashboard from './pages/Dashboard';
import PageNotFound from './pages/PageNotFound';
import News from './pages/News';
import InvestigatorNews from './pages/InvestigatorNews';
import InvestigatorEmergency from './pages/InvestigatorEmergency';
import Emergency from './pages/Emergency';
import Footer from './component/Footer';


function App() {
  const location = useLocation(); 
  

  const showNavbar = ['/','/Reports','/my-reports','/InvestigatorReports','/Profile','/VerifyKyc','/Dashboard','/InvestigatorNews','/News','/InvestigatorEmergency','/Emergency'].includes(location.pathname);

  
 
  return (
    <div className="App">
     
        <UserStore>
          {showNavbar && <Navbar />} 
          <Routes>
            <Route path="*" element={<PageNotFound />} />
            <Route path="/" element={<Reports />} />
            <Route path="/Reports" element={<Reports />} />
            <Route path="/my-reports" element={<MyReports />} />
            <Route path="/InvestigatorReports" element={<InvestigatorReport />} />
            <Route path="/Profile" element={<UserProfile />} />
            <Route path="/VerifyKyc" element={<InvestigatorKyc />} />
            <Route path="/Dashboard" element={<Dashboard />} />
            <Route path="/News" element={<News />} />
            <Route path="/InvestigatorNews" element={<InvestigatorNews />} />
            <Route path="/InvestigatorEmergency" element={<InvestigatorEmergency />} />
            <Route path="/Emergency" element={<Emergency/>} />

          </Routes>
          <Footer/>
        </UserStore>
     
    </div>
  );
}

export default App;
