import { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useKakaoStore from '@store/kakaoStore';

import PosterImage from '@components/reservation/PosterImage';
import BillingResult from '@components/reservation/BillingResult';

const ReservationDetailPage = () => {
  const { billingResult, getBillingResult } = useKakaoStore();
  const { id } = useParams();
  const nav = useNavigate();

  useEffect(() => {
    if (isNaN(id)) {
      nav('/Notfound');
    }

    getBillingResult(id);
  }, [id]);

  return (
    <div className='flex flex-col items-center'>
      <PosterImage posterUrl={billingResult.posterUrl} />
      <BillingResult billingResult={billingResult} />
    </div>
  );
};

export default ReservationDetailPage;
