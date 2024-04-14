import { useEffect } from 'react';
import useReviewStore from '@store/reviewStore';

import ReviewItem from '@components/mypage/ReviewItem';

import memory from '@assets/memory.png';

const ReviewList = () => {
  const { myReviewList, addMyReviews } = useReviewStore();

  useEffect(() => {
    addMyReviews();
  }, [addMyReviews]);

  return (
    <div className='flex flex-col'>
      {myReviewList && myReviewList.length > 0 ? (
        myReviewList.map((review) => (
          <ReviewItem
            key={review.reviewId}
            review={review}
          />
        ))
      ) : (
        <div className='absolute flex flex-col items-center w-full h-full bg-white pt-52'>
          <div className='w-24'>
            <img src={memory} />
          </div>
          <div className='mt-5 text-lg font-bold'>작성한 후기가 없어요</div>
          <div className='text-xs text-gray-500'>REFESTA와 함께</div>
          <div className='text-xs text-gray-500'>나만의 다양한 추억을 쌓아보세요!</div>
        </div>
      )}
    </div>
  );
};

export default ReviewList;
