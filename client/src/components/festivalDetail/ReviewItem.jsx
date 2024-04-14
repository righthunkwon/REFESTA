import { useEffect, useRef, useState } from 'react';

import ReactPlayer from 'react-player';

const ReviewItem = ({ review }) => {
  const { writer, profileUrl, attachmentUrl, mediaType, contents } = review;
  const [isPlaying, setPlaying] = useState(false);

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
    <div className='flex flex-col items-center pb-5 mx-5 mb-5 border-b-2'>
      <div className='flex items-center w-full'>
        <div className='overflow-hidden rounded-full h-7 w-7'>
          <img
            className='object-cover w-full h-full'
            src={profileUrl}
          />
        </div>
        <div className='ml-2 text-sm font-bold'>{writer}</div>
      </div>
      <div
        className='flex items-center justify-center mt-2 overflow-hidden w-80 h-80'
        ref={videoRef}
      >
        {mediaType === 'IMAGE' ? (
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
      <div className='px-1 mt-2 text-sm text-left w-80'>{contents}</div>
    </div>
  );
};

export default ReviewItem;
