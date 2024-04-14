import { useEffect } from 'react';
import useLikeStore from '@store/likeStore';

import LikeList from '@components/mypage/LikeList';

const LikeFestivalPage = () => {
  const { likeFestivalList, getLikeFestivalList } = useLikeStore();

  useEffect(() => {
    getLikeFestivalList();
  }, []);

  return (
    <div className='h-full bg-gray-200'>
      <LikeList contents={likeFestivalList} />
    </div>
  );
};

export default LikeFestivalPage;
