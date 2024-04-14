import { useEffect } from 'react';
import useFestivalInfoStore from '@store/festivalInfoStore';

// 예정 페스티벌의 상세 이미지 정보
const FestivalInfoDetail = () => {
  const { festivalInfoDetailData, setFestivalInfoDetailData } = useFestivalInfoStore();

  // 컴포넌트가 unmount될 때
  // festivalInfoDetailData를 초기화하는 함수 실행
  useEffect(() => {
    return () => {
      setFestivalInfoDetailData();
    };
  }, [setFestivalInfoDetailData]);

  return (
    <>
      {festivalInfoDetailData ? (
        <div>
          <div className='pt-10 pb-4 pl-4 text-2xl font-bold r'>
            <span className='border-l-8 border-black'></span>
            <span className='pl-3'>공연 상세 & 출연진 정보</span>
          </div>
          <img src={festivalInfoDetailData} alt='페스티벌 상세 정보' />
          <div className='py-10'></div>
        </div>
      ) : (
        <div>
          <div className='pt-10 pb-4 pl-4 text-xl font-bold r'>
            <span className='border-l-8 border-black'></span>
            <span className='pl-3'>공연 상세 & 출연진 정보</span>
          </div>{' '}
          <div className='p-4 mt-16 text-lg font-bold text-center '>페스티벌 정보가 없습니다.</div>
          <div className='py-10'></div>
        </div>
      )}
    </>
  );
};

export default FestivalInfoDetail;
