import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import useKakaoStore from '@store/kakaoStore';

import Swal from 'sweetalert2';

import PosterImage from '@components/reservation/PosterImage';

const Reservation = () => {
  const location = useLocation();
  const festivalInfo = location.state?.festivalInfo;
  const nav = useNavigate();
  const { kakaoPay } = useKakaoStore();

  // 주문 수량
  const [count, setCount] = useState(1);
  useEffect(() => {
    if (count > 4) {
      Swal.fire({
        title: '예매 개수 초과',
        html: '최대 4매까지 예약 가능합니다.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
      setCount(4);
    }
  }, [count]);

  // 천단위 콤마 넣기
  const addComma = (price) => {
    let returnString = price?.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    return returnString;
  };

  // 결제
  // TODO: approve_url 에 /reservation/result
  const handleKakaoPay = async () => {
    const redirectUrl = await kakaoPay(festivalInfo.id, count);

    if (redirectUrl) {
      window.location.href = redirectUrl;
    } else {
      Swal.fire({
        title: '결제 요청 실패',
        html: '결제에 실패하였습니다.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
    }
  };

  return (
    <div className='flex flex-col'>
      <PosterImage posterUrl={festivalInfo.posterUrl} />
      <section className='flex flex-col mx-5 my-4'>
        <div>
          <div className='text-xs text-gray-400'>{festivalInfo.name.split(' - ')[0]}</div>
          <div className='text-xl font-bold'>{festivalInfo.name}</div>
        </div>
        <div className='mt-4 font-semibold'>
          <div className='flex justify-between'>
            <div>일시</div>
            <div>{festivalInfo.date}</div>
          </div>
          <div className='flex justify-between mt-2'>
            <div>장소</div>
            <div>{festivalInfo.location}</div>
          </div>
          <div className='flex justify-between mt-2'>
            <div>가격</div>
            <div>성인 1매 {addComma(festivalInfo.price)}원</div>
          </div>
        </div>
        <div className='flex justify-between px-3 py-2 mt-10 bg-gray-200 rounded-md'>
          <div className='text-sm'>주문 수량</div>
          <div className='flex px-2 bg-white'>
            <button
              className='px-1'
              onClick={() => {
                count > 1 ? setCount(count - 1) : count;
              }}
            >
              -
            </button>
            <div className='px-2'>{count}</div>
            <button
              className='px-1'
              onClick={() => {
                setCount(count + 1);
              }}
            >
              +
            </button>
          </div>
        </div>
        <div className='flex justify-between mt-3 font-bold'>
          <div>총 상품 금액 :</div>
          <div>{addComma(count * festivalInfo.price)}원</div>
        </div>
        <div className='flex justify-between w-full mt-3 min-h-14'>
          <button
            className='flex-1 mr-1 bg-gray-300 rounded-md'
            onClick={() => {
              nav(-1);
            }}
          >
            취소
          </button>
          <button className='flex-1 ml-1 bg-yellow-300 rounded-md' onClick={handleKakaoPay}>
            카카오결제
          </button>
        </div>
      </section>
    </div>
  );
};

export default Reservation;
