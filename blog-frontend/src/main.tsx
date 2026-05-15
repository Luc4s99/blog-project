import { createRoot } from 'react-dom/client'
import { ToastContainer } from 'react-toastify';
import './index.css'

import App from './App.tsx'
import PostForm from './pages/PostForm.tsx'

import 'bootstrap/dist/css/bootstrap.min.css'

import { BrowserRouter, Route, Routes } from 'react-router-dom'
import PostPage from './pages/PostPage.tsx'
import PostSearch from './pages/PostSearch.tsx'
import Login from './pages/Login.tsx'
import { UserProvider } from './contexts/useAuth.tsx';
import ProtectedRoute from './routes/ProtectedRoute.tsx';
import LandingPage from './pages/LandingPage.tsx';
import Register from './pages/Register.tsx';

createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <UserProvider>
      <>
        <Routes>
          
          <Route path='/' element={<LandingPage />}></Route>
          <Route path='/login' element={<Login />}></Route>
          <Route path='/register' element={<Register />}></Route>

          <Route path='/home' element={<ProtectedRoute><App /></ProtectedRoute>}></Route>
          <Route path='/novo' element={<ProtectedRoute><PostForm/></ProtectedRoute>}></Route>
          <Route path='/editar/:id' element={<ProtectedRoute><PostForm/></ProtectedRoute>}></Route>
          <Route path='/posts/:id' element={<ProtectedRoute><PostPage /></ProtectedRoute>}></Route>
          <Route path='/posts/search' element={<ProtectedRoute><PostSearch /></ProtectedRoute>}></Route>
        </Routes>
        <ToastContainer />
      </>
    </UserProvider>
  </BrowserRouter>,
)
