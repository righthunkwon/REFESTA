import { useNavigate } from 'react-router-dom';
import useFestivalInfoStore from '@store/festivalInfoStore';

// 예정 페스티벌에 출력되는 예매하기 버튼
const ReservationButton = () => {
  const nav = useNavigate();
  const { festivalInfoData } = useFestivalInfoStore();

  // 예매하기 버튼을 클릭하면
  // 페스티벌 정보를 가지고 다른 페이지로 이동
  const onClickReservation = () => {
    nav('/reservation', { state: { festivalInfo: festivalInfoData } });
  };

  return (
    <div className='py-3 max-w-[500px] fixed bottom-0 w-full bg-white shadow-lg px-10'>
      <button
        className='w-full py-3 font-semibold text-white rounded-lg 0 bg-ourPink h-14'
        onClick={onClickReservation}
      >
        예매하기
      </button>
    </div>
  );
};
export default ReservationButton;
