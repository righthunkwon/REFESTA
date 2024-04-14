import { useEffect, useRef, useState } from 'react';
import ReactPlayer from 'react-player';

import DeleteReview from '@components/mypage/DeleteReview';

const ReviewItem = ({ review }) => {
  const { reviewId, name, date, location, contents, attachmentUrl, type } = review;
  const [isPlaying, setPlaying] = useState(false);
  const [isModalOpen, setModalOpen] = useState(false);

  // Intersection Observer
  const videoRef = useRef(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        const [entry] = entries;
        if (entry.isIntersecting) {
          setPlaying(true);
        } else {
          setPlaying(false);
        }
      },
      {
        threshold: 0.5,
      }
    );
    if (videoRef.current) {
      observer.observe(videoRef.current);
    }

    return () => {
      if (videoRef.current) {
        observer.unobserve(videoRef.current);
      }
    };
  }, [videoRef]);

  return (
    <div className='flex flex-col pb-5 mx-5 mb-5 border-b-2'>
      <div className='flex justify-between'>
        <div className='flex flex-col'>
          <div className='text-lg font-bold leading-5'>{name}</div>
          <div className='flex my-2 text-xs text-gray-400'>
            <div className='mr-3'>{review.festivalDate}</div>
            <div>{`|`}</div>
            <div className='ml-3'>{location}</div>
          </div>
        </div>
        <button
          className='flex items-center justify-center h-12 px-1 text-xs text-center text-gray-400 w-14'
          onClick={(e) => {
            e.stopPropagation();
            setModalOpen(true);
          }}
        >
          삭제
        </button>
      </div>
      <div
        className='flex items-center justify-center w-full mt-2 overflow-hidden h-96'
        ref={videoRef}
      >
        {type === 'IMAGE' ? (
          <img
            className='object-cover w-full h-full'
            src={attachmentUrl}
          />
        ) : (
          <ReactPlayer
            url={attachmentUrl}
            muted={true}
            playing={isPlaying}
            playsinline={true}
            loop={true}
            controls={true}
            width='100%'
            height='100%'
            className='bg-gray-800'
          />
        )}
      </div>
      <div className='px-1 mt-2 text-sm'>{contents}</div>
      <DeleteReview
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        id={reviewId}
      />
    </div>
  );
};

export default ReviewItem;
