import { useEffect, useRef } from 'react';
import useReviewStore from '@store/reviewStore';

const DeleteReview = ({ isOpen, onClose, id }) => {
  const { removeReview } = useReviewStore();
  const modalRef = useRef();

  useEffect(() => {
    // 바깥화면 클릭시 모달 close
    const handleOutsideClick = (e) => {
      if (modalRef.current && !modalRef.current.contains(e.target)) {
        onClose();
      }
    };

    if (isOpen) {
      document.body.style.overflow = 'hidden';
      document.addEventListener('mouseover', handleOutsideClick);
    } else {
      document.body.style.overflow = 'unset';
    }

    return () => {
      document.body.style.overflow = 'unset';
      // 언마운트시 이벤트 리스너 제거해주기
      document.removeEventListener('mouseover', handleOutsideClick);
    };
  }, [isOpen, onClose]);

  const handleDelete = async () => {
    await removeReview(id);
    onClose();
  };

  if (!isOpen) return null;
  return (
    <div className='fixed inset-0 z-50 flex items-center justify-center bg-gray-800 bg-slate-500/50'>
      {isOpen && (
        <div className='w-full h-full max-w-[500px] '>
          <div className='flex items-center justify-center w-full h-full'>
            <section
              ref={modalRef}
              className='flex flex-col w-2/3 bg-white shadow-lg item h-36 rounded-3xl'
            >
              <div className='flex flex-col justify-center flex-grow text-xs text-center border-b-2'>
                <div>삭제하시겠습니까?</div>
                <div>삭제한 글은 복구할 수 없습니다.</div>
              </div>
              <div className='flex justify-center flex-1 text-sm font-bold text-center'>
                <div
                  className='flex items-center justify-center flex-1 border-r-2'
                  onClick={onClose}
                >
                  취소
                </div>
                <div
                  className='flex items-center justify-center flex-1 text-red-500'
                  onClick={() => {
                    handleDelete();
                  }}
                >
                  삭제
                </div>
              </div>
            </section>
          </div>
        </div>
      )}
    </div>
  );
};

export default DeleteReview;
