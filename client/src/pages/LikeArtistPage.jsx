import { useEffect } from 'react';
import useLikeStore from '@store/likeStore';

import LikeList from '@components/mypage/LikeList';

const LikeArtistPage = () => {
  const { likeArtistList, getLikeArtistList } = useLikeStore();

  useEffect(() => {
    getLikeArtistList();
  }, []);

  return (
    <div className='h-full bg-gray-200'>
      <LikeList contents={likeArtistList} />
    </div>
  );
};

export default LikeArtistPage;
