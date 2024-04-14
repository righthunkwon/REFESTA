import React, { useRef, useEffect, useState, useMemo } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import useReviewStore from '@store/reviewStore';
import festivalInfoStore from '@store/festivalInfoStore';

import Swal from 'sweetalert2';
import ReactPlayer from 'react-player';
import browserImageCompression from '@util/browserImageCompression';

import xBtn from '@assets/x_black.png';
import picture from '@assets/picture.png';

const RegisterReviewPage = ({ isOpen, onClose, selectedFile: propSelectedFile }) => {
  const inputFileRef = useRef(null);
  const nav = useNavigate();
  const { id } = useParams();
  const { festivalInfoData } = festivalInfoStore();
  const { registerReview, addReviews } = useReviewStore();
  const [isPlaying, setIsPlaying] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [newReview, setNewReview] = useState({});

  const [fileUrl, setFileUrl] = useState('');
  const [isImage, setIsImage] = useState();

  // 스크롤 막기
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'unset';
    }
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [isOpen]);

  // 모달 닫힐때 url정리
  useEffect(() => {
    return () => {
      if (fileUrl) {
        URL.revokeObjectURL(fileUrl);
        setFileUrl('');
      }
    };
  }, []);

  // newReview에 festivalId 할당
  useEffect(() => {
    setNewReview((prev) => ({
      ...prev,
      festivalId: id,
    }));
  }, [id]);

  // textarea 변경사항 세팅시키기
  const handleContentsChange = (e) => {
    const { value } = e.target;
    setNewReview((prev) => ({
      ...prev,
      contents: value,
    }));
  };

  // file 변경사항 세팅시키기
  useEffect(() => {
    if (propSelectedFile) {
      setNewReview((prev) => ({
        ...prev,
        file: propSelectedFile,
      }));
      setIsImage(propSelectedFile.type.startsWith('image'));
      const newURL = URL.createObjectURL(propSelectedFile);
      setFileUrl(newURL);
    }
  }, [propSelectedFile]);

  // 파일이 바뀌었을 때
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      setIsImage(file.type.startsWith('image'));
      const newURL = URL.createObjectURL(file);
      setFileUrl(newURL);

      if (isImage) {
        try {
          const compressedFile = await browserImageCompression(file);
          setNewReview((prev) => ({
            ...prev,
            file: compressedFile,
          }));
        } catch (error) {
          //error
        }
      } else {
        setNewReview((prev) => ({
          ...prev,
          file: file,
        }));
      }
    }
  };

  // 아이콘 클릭으로 input 파일 등록 대신하기
  const handleImageChange = () => {
    inputFileRef.current.click();
  };

  // 등록 버튼 누르기
  const handleReviewSubmit = async () => {
    if (!(newReview.contents?.trim() || '')) {
      Swal.fire({
        title: '등록 실패',
        html: '문구를 작성해 주세요.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
      return;
    }
    setIsSubmitting(true);
    try {
      await registerReview(newReview);
      await addReviews(id);
    } catch (error) {
      //error
    } finally {
      setIsSubmitting(false);
      setNewReview((prev) => ({
        ...prev,
        contents: '',
      }));
      onClose();
    }
  };

  // 프리뷰
  const preview = useMemo(() => {
    return isImage ? (
      <img key={`file-preview-${Date.now()}`} className='object-cover w-full h-full' src={fileUrl} />
    ) : (
      <ReactPlayer
        key={`file-preview-${Date.now()}`}
        url={fileUrl}
        muted={true}
        playing={isPlaying}
        playsinline={true}
        loop={true}
        controls={true}
        width='100%'
        height='100%'
        className='bg-gray-800'
      />
    );
  }, [fileUrl, isImage, isPlaying]);

  if (!isOpen) return null;
  return (
    <div className='fixed inset-0 z-50 flex items-center justify-center'>
      {isOpen && (
        <div className='w-full overflow-y-auto bg-white h-lvh'>
          <div className='flex flex-col items-center justify-between w-full h-full pb-10'>
            <div className='relative flex items-center justify-center w-full h-10 text-lg font-bold py-7 border-y-2'>
              후기 작성
              <button onClick={onClose} className='absolute right-2'>
                <img src={xBtn} />
              </button>
            </div>
            <section className='flex flex-col'>
              <div className='flex my-5 px-7'>
                <div className='overflow-hidden h-14 w-9'>
                  <img className='object-cover h-full' src={festivalInfoData.posterUrl} />
                </div>
                <div className='flex flex-col justify-center px-3'>
                  <div className='font-bold leading-5 text-left'>{festivalInfoData.name}</div>
                  <div className='flex mt-1 text-xs text-gray-500'>
                    <div>{festivalInfoData.date}</div>
                    <div className='ml-2'>| {festivalInfoData.location}</div>
                  </div>
                </div>
              </div>
              <div className='flex flex-col items-center'>
                <div className='relative flex items-center justify-center w-full overflow-hidden h-72 px-7'>
                  {preview}
                  <div
                    className='absolute flex items-center justify-center w-12 h-12 bg-gray-300 rounded-full bottom-3 right-10'
                    onClick={handleImageChange}
                  >
                    <img className='w-3/4' src={picture} />
                  </div>
                  <input
                    ref={inputFileRef}
                    className='hidden'
                    type='file'
                    name='file'
                    accept='image/*, video/*'
                    onChange={handleFileChange}
                  />
                </div>
                <textarea
                  className='w-full mt-5 overflow-y-scroll h-36 px-7 mx-7 focus:outline-none scrollbar-hide'
                  name=''
                  id=''
                  placeholder='문구 작성...'
                  onChange={handleContentsChange}
                ></textarea>
              </div>
            </section>
            <button
              className='flex items-center justify-center w-11/12 h-12 mt-3 text-white rounded-md bg-ourIndigo'
              onClick={handleReviewSubmit}
              disabled={isSubmitting}
            >
              {isSubmitting ? '등록 중...' : '등록하기'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default RegisterReviewPage;
