import { useNavigate } from 'react-router-dom';

// 검색 결과로 표시할 개별 아티스트
const ArtistSearchItem = ({ artist }) => {
  const nav = useNavigate();

  // 아티스트 아이템을 클릭했을 때
  // 해당 아티스트 상세보기로 이동하는 함수
  const handleClick = () => {
    nav(`/artist/${artist.id}`);
  };

  return (
    <div className='flex mx-1 my-4' onClick={handleClick}>
      <div className='max-w-20'>
        <img className='object-cover rounded-full min-w-20 aspect-square' src={artist.pictureUrl} alt={artist.name} />
      </div>
      <div className='mt-5 ml-3'>
        <div className='text-sm text-left truncate max-w-56'>{artist.name}</div>
        <div className='mt-1 text-xs text-left text-gray-400'>{artist.genre[0] || '미정'}</div>
      </div>
    </div>
  );
};
export default ArtistSearchItem;
