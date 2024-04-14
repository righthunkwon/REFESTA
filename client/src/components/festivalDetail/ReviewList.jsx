import { useEffect } from 'react';
import { useParams } from 'react-router';
import useReviewStore from '@store/reviewStore';

import ReviewItem from '@components/festivalDetail/ReviewItem';

import memory from '@assets/memory.png';

const ReviewList = () => {
  const { addReviews, reviewList } = useReviewStore();
  const { id } = useParams();

  useEffect(() => {
    addReviews(id);
  }, [addReviews, id]);

  return (
    <div className='flex flex-col items-center pt-3'>
      {reviewList && reviewList.length > 0 ? (
        reviewList.map((review, index) => (
          <ReviewItem
            key={index}
            review={review}
          />
        ))
      ) : (
        <div className='flex flex-col items-center justify-center mt-20 text-center'>
          <div className='flex items-center justify-center w-20'>
            <img
              className='w-full'
              src={memory}
            />
          </div>
          <div className='mt-3 font-bold text-gray-600'>
            작성된 후기가 없어요 <br />
            후기를 작성해 추억을 공유해 보세요!
          </div>
        </div>
      )}
    </div>
  );
};

export default ReviewList;
