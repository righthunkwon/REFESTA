import { Link } from 'react-router-dom';

import FestivalHomeItem from '@components/home/FestivalHomeItem';
import ListTitle from '@components/home/ListTitle';
import ItemLoading from '@components/home/loading/ItemLoading';
import { useState } from 'react';

const FestivalHomeList = ({ festivalData, isLoading }) => {
  return (
    <div className='h-[271px]'>
      <ListTitle
        title={'추천 페스티벌'}
        description={'취향에 맞는 페스티벌을 추천해드려요!'}
        btn={
          <div className='pb-5 cursor-pointer'>
            <Link to='/festival/list/scheduled'>{`전체보기 >`}</Link>
          </div>
        }
      />
      <div className='flex px-4 overflow-x-scroll scrollbar-hide whitespace-nowrap gap-x-3'>
        {isLoading ? (
          <ItemLoading type={'festival'} />
        ) : (
          festivalData.map((item) => <FestivalHomeItem key={item.id} {...item} />)
        )}
      </div>
    </div>
  );
};
export default FestivalHomeList;
