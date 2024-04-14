import React from 'react';
import { Outlet } from 'react-router-dom';

import ScrollToTop from '@pages/ScrollToTop';
import SearchInput from '@components/search/SearchInput';

const SearchLayout = () => {
  return (
    <>
      <SearchInput />
      <main>
        <ScrollToTop />
        <Outlet />
      </main>
    </>
  );
};
export default SearchLayout;
