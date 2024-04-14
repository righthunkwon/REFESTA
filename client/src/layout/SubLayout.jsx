import React from 'react';
import { Outlet } from 'react-router-dom';

import ScrollToTop from '@pages/ScrollToTop';

const SubLayout = () => {
  return (
    <>
      <main className='flex flex-col items-center h-full'>
        <ScrollToTop />
        <Outlet />
      </main>
    </>
  );
};
export default SubLayout;
