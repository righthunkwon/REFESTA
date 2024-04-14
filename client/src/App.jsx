import './index.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/LoginPage';
import RegisterInfo from './pages/RegisterInfoPage';
import Home from './pages/HomePage';
import FestivalList from './pages/FestivalListPage';
import FestivalDetail from './pages/FestivalDetailPage';
import Notfound from './pages/NotfoundPage';
import Google_Login from './components/start/Google_login';
import ArtistDetail from './pages/ArtistDetailPage';
import Reservation from './pages/ReservationPage';
import ReservationResult from './pages/ReservationResultPage';
import ReservationDetail from './pages/ReservationDetailPage';
import LikeArtist from './pages/LikeArtistPage';
import LikeFestival from './pages/LikeFestivalPage';
import Search from './pages/SearchPage';
import SearchResult from './pages/SearchResultPage';
import MyReview from './pages/MyReviewPage';
import MainLayout from './layout/MainLayout';
import SearchLayout from './layout/SearchLayout';
import SubLayout from './layout/SubLayout';
import ReservationApprove from './pages/ReservationApprovePage';
import AboutPage from './pages/AboutPage';
import PrivacyPolicyPage from '@pages/PrivacyPolicyPage';

function App() {
  return (
    <div className='flex flex-col h-full'>
      <BrowserRouter>
        <Routes>
          <Route element={<SubLayout />}>
            <Route path='/login' element={<Login />} />
            <Route path='/google-login' element={<Google_Login />} />
            <Route path='/regist' element={<RegisterInfo />} />
            <Route path='/about' element={<AboutPage />} />
            <Route path='/reservation/approve' element={<ReservationApprove />} />
            <Route path='/privacy_policy' element={<PrivacyPolicyPage />} />
          </Route>

          <Route element={<MainLayout />}>
            <Route path='/' element={<Home />} />
            <Route path='/festival/list/:state' element={<FestivalList />} />
            <Route path='/festival/:id' element={<FestivalDetail />} />
            <Route path='/artist/:id' element={<ArtistDetail />} />
            <Route path='/reservation' element={<Reservation />} />
            <Route path='/reservation/result/:id' element={<ReservationResult />} />
            <Route path='/reservation/detail/:id' element={<ReservationDetail />} />
            <Route path='/like/artist' element={<LikeArtist />} />
            <Route path='/like/festival' element={<LikeFestival />} />
            <Route path='my-review' element={<MyReview />} />
          </Route>

          <Route element={<SearchLayout />}>
            <Route path='/search' element={<Search />} />
            <Route path='/search/result' element={<SearchResult />} />
          </Route>
          <Route path='*' element={<Notfound />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
