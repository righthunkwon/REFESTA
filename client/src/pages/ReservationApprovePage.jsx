import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import useKakaoStore from '@store/kakaoStore';

import loading from '@assets/loading.png';

const ReservationApprovePage = () => {
  const { approvePayment } = useKakaoStore();
  const [searchParams] = useSearchParams();
  const pgToken = searchParams.get('pg_token');
  const nav = useNavigate();

  useEffect(() => {
    if (pgToken) {
      approvePayment(pgToken)
        .then((data) => {
          nav(`/reservation/result/${data.data.reservation_id}`);
        })
        .catch((error) => {
          //error
        });
    }
  }, []);

  return (
    <div className='flex flex-col items-center justify-center px-10 mx-0 my-auto h-[85vh]'>
      <div className='w-1/5 '>
        <img className='w-full mr-3 motion-safe:animate-spin' src={loading} />
      </div>
      <div className='my-6 text-lg font-bold text-center'>결제 진행 중</div>
    </div>
  );
};

export default ReservationApprovePage;
