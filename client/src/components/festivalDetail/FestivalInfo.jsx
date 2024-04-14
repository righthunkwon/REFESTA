import { useState, useEffect } from 'react';
import useFestivalInfoStore from '@store/festivalInfoStore';

import instance from '@util/token_interceptor';

import heart from '@assets/heart.png';
import heart_full from '@assets/heart_full.png';

// 페스티벌 정보(예정/완료 공통)
const FestivalInfo = () => {
  const { festivalInfoData } = useFestivalInfoStore();
  const [likedFestival, setLikedFestival] = useState(festivalInfoData && festivalInfoData.liked);

  // 페스티벌 정보가 변경될 때
  // 페스티벌 좋아요 초기 정보를 설정하는 함수 실행
  useEffect(() => {
    if (festivalInfoData) {
      setLikedFestival(festivalInfoData.liked);
    }
  }, [festivalInfoData]);

  // 좋아요 상태를 토글하는 함수
  const handleLike = async () => {
    try {
      const response = await instance.patch(`festivals/${festivalInfoData.id}`);
      if (response.data.status === 'success') {
        setLikedFestival((prevLiked) => !prevLiked);
      }
    } catch (error) {
      //error
    }
  };

  return (
    <article className='m-4 mb-12 min-h-60'>
      {festivalInfoData && (
        <div className='flex'>
          <div className='flex items-center flex-1'>
            <img className='object-fill h-full' src={festivalInfoData.posterUrl} alt='' />
          </div>
          <div className='relative items-start flex-1 ml-2'>
            <div className='text-sm font-semibold'>{festivalInfoData.name}</div>
            <div className='mt-2 border-b border-b-gray-400'></div>
            <div className='p-2'>
              <div className='pb-1 text-sm '>일시</div>
              <div className='pb-4 text-xs text-gray-500'>{festivalInfoData.date}</div>
              <div className='pb-1 text-sm'>장소</div>
              <div className='pb-4 text-xs text-gray-500'>{festivalInfoData.location}</div>
              <div className='pb-1 text-sm'>가격</div>
              <div className='pb-4 text-xs text-gray-500'>
                {festivalInfoData.price === 0 ? '119,000 원' : festivalInfoData.price.toLocaleString() + ' 원'}
              </div>
            </div>
            <div className='absolute bottom-0 right-0 flex justify-end pr-1 w-9'>
              <img
                className='w-full'
                src={likedFestival ? heart_full : heart}
                alt='페스티벌 좋아요 버튼'
                onClick={handleLike}
              />
            </div>
          </div>
        </div>
      )}
    </article>
  );
};

export default FestivalInfo;
