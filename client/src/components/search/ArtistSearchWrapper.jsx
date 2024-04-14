import ArtistSearchList from '@components/search/ArtistSearchList';

// 검색 결과 중 아티스트 탭
const ArtistSearchWrapper = () => {
  return (
    <>
      <div className='flex flex-col'>
        <ArtistSearchList />
      </div>
    </>
  );
};
export default ArtistSearchWrapper;
