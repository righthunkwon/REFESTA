import React from 'react';
import { Outlet } from 'react-router-dom';

import ScrollToTop from '@pages/ScrollToTop';
import Header from '@components/common/Header';

const MainLayout = () => {
  return (
    <>
      <Header />
      <main className='h-full'>
        <ScrollToTop />
        <Outlet />
      </main>
    </>
  );
};
export default MainLayout;
