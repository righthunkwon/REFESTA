import { useState } from 'react';

import { useYoutubeConnectQuery } from '@queries/startPagesQueries';

import yLogo from '@assets/youtube_music_logo.png';
import connect_check from '@assets/connect_check.png';

const YoutubeConnect = ({ setStep, stepParam }) => {
  const [modalOpen, setModalOpen] = useState(false);
  const { mutate, data, isLoading, isError, error } = useYoutubeConnectQuery();

  const onClickConnect = () => {
    // 재생목록 요청 백으로 보내기
    mutate();
    if (!isLoading) {
      setModalOpen(true);
      setTimeout(() => {
        setModalOpen(false);
        setStep(stepParam.step3);
      }, 2000);
    }
    if (isError) {
      //error
    }
  };
  const onClickSkip = () => {
    setStep(stepParam.step3);
  };

  return (
    <div className='relative grid gap-y-5'>
      <div className='mb-5 text-2xl font-bold leading-9 tracking-tight text-center text-ourIndigo'>빠르게 시작하기</div>
      <img src={yLogo} />
      <div className='mb-20 text-sm text-center'>
        유튜브 재생 목록을 연동하고 <br />
        Refesta를 빠르게 시작해보세요. <br />
        취향에 맞는 페스티벌을 추천해드립니다.
      </div>
      <button
        className='flex items-center justify-center w-full font-semibold text-white rounded-md bg-ourIndigo h-14'
        onClick={onClickConnect}
      >
        연동하기
      </button>
      <div className='text-center underline cursor-pointer' onClick={onClickSkip}>
        건너뛰기
      </div>
      {modalOpen && (
        <div id='modal-container' className='absolute top-0 left-0 flex w-full h-full '>
          <div
            id='modal-content'
            className='grid content-center justify-center w-full my-32 border bg-white/95 rounded-2xl'
          >
            <div className='w-[70px] mx-auto mb-4'>
              <img className='w-full' src={connect_check} />
            </div>
            <div className='text-lg font-bold text-ourIndigo'>연동이 완료되었습니다.</div>
          </div>
        </div>
      )}
    </div>
  );
};
export default YoutubeConnect;
