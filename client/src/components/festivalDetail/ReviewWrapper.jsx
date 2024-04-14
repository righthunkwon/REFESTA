import { useRef, useState } from 'react';

import browserImageCompression from '@util/browserImageCompression';

import RegisterReview from '@pages/RegisterReviewPage';
import ReviewList from '@components/festivalDetail/ReviewList';

import plus from '@assets/plus.png';

const ReviewWrapper = () => {
  const [isModalOpen, setModalOpen] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);
  const fileInputRef = useRef(null);

  const handlePlusClick = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.type.startsWith('image')) {
        try {
          const compressedFile = await browserImageCompression(file);
          setSelectedFile(compressedFile);
        } catch (error) {
          //error
        }
      } else {
        setSelectedFile(file);
      }
      setModalOpen(true);
    }
  };

  return (
    <div>
      <ReviewList />
      {isModalOpen ? (
        <></>
      ) : (
        <div className='fixed w-10 bg-gray-400 rounded-full opacity-60 bottom-3 right-3' onClick={handlePlusClick}>
          <img src={plus} />
          <input
            ref={fileInputRef}
            className='hidden'
            type='file'
            accept='image/*, video/*'
            onChange={handleFileChange}
          />
        </div>
      )}

      <RegisterReview isOpen={isModalOpen} onClose={() => setModalOpen(false)} selectedFile={selectedFile} />
    </div>
  );
};

export default ReviewWrapper;
