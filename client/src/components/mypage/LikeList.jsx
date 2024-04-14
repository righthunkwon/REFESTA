import { useNavigate, useLocation } from 'react-router-dom';

import LikeItem from '@components/mypage/LikeItem';

import wishlist from '@assets/wishlist.png';

const LikeList = ({ contents }) => {
  const nav = useNavigate();
  const location = useLocation();
  const pageType = location.pathname.includes('festival') ? 'festival' : 'artist';

  // 컨텐츠 클릭 -> 상세 페이지
  const handleItemClick = (id, type) => {
    if (type === 'festival') {
      nav(`/festival/${id}`);
    } else {
      nav(`/artist/${id}`);
    }
  };

  if (contents && contents.length > 0) {
    return (
      <div className='grid grid-cols-2 gap-3 p-2 bg-gray-200'>
        {contents.map((content) => {
          const modifiedContents = {
            ...content,
            id: content.festivalId || content.artistId,
            url: content.posterUrl || content.pictureUrl,
            type: content.festivalId ? 'festival' : 'artist',
          };
          return (
            <LikeItem
              key={modifiedContents.id}
              content={modifiedContents}
              onClick={() => handleItemClick(modifiedContents.id, modifiedContents.type)}
            />
          );
        })}
      </div>
    );
  } else {
    return pageType === 'festival' ? (
      <div className='absolute flex flex-col items-center w-full h-full bg-white pt-52'>
        <div className='w-24'>
          <img src={wishlist} />
        </div>
        <div className='mt-5 text-lg font-bold'>좋아하는 페스티벌이 없어요</div>
        <div className='text-xs text-gray-500'>페스티벌 상세 화면에서 하트 아이콘을 눌러</div>
        <div className='text-xs text-gray-500'>나만의 페스티벌 목록을 만들어보세요!</div>
      </div>
    ) : (
      <div className='absolute flex flex-col items-center w-full h-full bg-white t-0 pt-52'>
        <div className='w-24'>
          <img src={wishlist} />
        </div>
        <div className='mt-5 text-lg font-bold'>좋아하는 아티스트가 없어요</div>
        <div className='text-xs text-gray-500'>아티스트 상세 화면에서 하트 아이콘을 눌러</div>
        <div className='text-xs text-gray-500'>나만의 아티스트 목록을 만들어보세요!</div>
      </div>
    );
  }
};

export default LikeList;
