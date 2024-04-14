import { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useArtistStore from '@store/artistStore';

import instance from '@util/token_interceptor';

import FestivalList from '@components/artistDetail/FestivalList';

import heartFull from '@assets/heart_full.png';
import heart from '@assets/heart.png';

const ArtistDetail = () => {
  const { addArtist, artist, toggleLike, updateArtistLike } = useArtistStore();
  const { id } = useParams();
  const nav = useNavigate();

  useEffect(() => {
    // 파라미터에 문자열 입력했을 경우, 404페이지
    if (isNaN(id)) {
      nav('/Notfound');
    }

    // 아티스트 정보 가져오기
    const fetchArtist = async () => {
      try {
        await addArtist(id);
      } catch (e) {
        nav('/Notfound');
      }
    };

    fetchArtist();

    // 아티스트 상세 페이지 접근 데이터 제공
    const increaseArtistViewCount = async () => {
      try {
        const response = await instance.patch(`recommendations/artists/${id}?point=1`);
      } catch (error) {
        //console.error('아티스트 조회수 증가 오류:', error);
      }
    };

    increaseArtistViewCount();
  }, [addArtist, id]);

  // 좋아요 버튼
  const handleLikeBtn = () => {
    toggleLike();
    updateArtistLike(id);
  };

  return (
    <div className='flex-col'>
      <section className='pb-5 border-b-2'>
        <div className='flex-col justify-center mt-10 text-center'>
          <div>
            <div className='relative'>
              <div className='z-0 flex items-center justify-center mx-20 overflow-hidden rounded-full w-13 h-13'>
                <img className='object-cover h-full' src={artist.pictureUrl} alt='사진' />
              </div>
              <div className='absolute w-12 h-12 py-2.5 bottom-0 right-12' onClick={handleLikeBtn}>
                <img src={artist.liked ? `${heartFull}` : `${heart}`} className='h-full' />
              </div>
            </div>
          </div>
          <div className='mt-5'>
            <div className='mt-5 text-2xl'>{artist.name}</div>
            <div className='mt-4 text-sm text-gray-500'>
              대표 장르 :{' '}
              {artist.genreList && artist.genreList.length > 0 ? (
                artist.genreList.map((genre, index) => (
                  <span key={index}>
                    {genre}
                    {index < artist.genreList.length - 1 ? ', ' : ''}
                  </span>
                ))
              ) : (
                <span>-</span>
              )}
            </div>
          </div>
        </div>
      </section>
      <section className='mx-5 mt-5'>
        <div className='font-bold'>참여 페스티벌</div>
        <div>
          <FestivalList festivalList={artist.performanceList} />
        </div>
      </section>
    </div>
  );
};
export default ArtistDetail;
