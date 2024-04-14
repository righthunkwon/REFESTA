import useSearchResultStore from '@store/searchResultStore';

import ArtistSearchItem from '@components/search/ArtistSearchItem';

// 검색 결과로 표시할 아티스트 목록
const ArtistSearchList = ({ isTotal, setOpenSearchTab }) => {
  const { artistList } = useSearchResultStore();

  // 통합검색의 경우 일부 결과만 출력하기 위해
  // 검색된 아티스트의 목록이 3개보다 많을 때 렌더링할 아티스트목록 수정
  let renderArtistList = artistList;
  if (isTotal && renderArtistList.length > 3) {
    renderArtistList = artistList.slice(0, 3);
  }

  // 아티스트 전체 보기를 위한 탭 이동
  const onClickTotalBtn = () => {
    setOpenSearchTab(3);
  };

  return (
    <div>
      <div className='text-base font-bold text-left'>
        <span>아티스트 &nbsp;</span>
        <span className='text-lg font-bold text-ourPink'>{artistList.length}</span>
        <span className='text-lg text-gray-400'>
          {isTotal && artistList.length > 3 && <button onClick={onClickTotalBtn}>&nbsp; &gt;</button>}
        </span>
      </div>
      {renderArtistList.length === 0 ? (
        <div className='p-4 mt-4 text-lg font-bold text-center'>아티스트 정보가 없습니다.</div>
      ) : (
        <div className=''>
          {renderArtistList.map((item) => (
            <ArtistSearchItem key={item.id} artist={item} />
          ))}
        </div>
      )}
    </div>
  );
};
export default ArtistSearchList;
