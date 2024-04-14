import { useNavigate } from 'react-router-dom';

// 검색 결과로 표시할 개별 페스티벌
const FestivalSearchItem = ({ festival }) => {
  const nav = useNavigate();

  // 페스티벌 아이템을 클릭했을 때
  // 해당 페스티벌 상세보기로 이동하는 함수
  const handleClick = () => {
    nav(`/festival/${festival.id}`);
  };

  return (
    <div className='w-full' onClick={handleClick}>
      <div>
        <img className='object-cover rounded-md aspect-square' src={festival.posterUrl} alt={festival.name} />
      </div>
      <div className='mt-2 text-xs text-left'>{festival.name}</div>
    </div>
  );
};
export default FestivalSearchItem;
