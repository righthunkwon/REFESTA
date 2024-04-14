import useSearchResultStore from '@store/searchResultStore';

import FestivalSearchItem from '@components/search/FestivalSearchItem';

// 검색 결과로 표시할 페스티벌 목록
const FestivalSearchList = ({ isTotal, setOpenSearchTab }) => {
  const { festivalList } = useSearchResultStore();

  // 통합검색의 경우 일부 결과만 출력하기 위해
  // 검색된 페스티벌의 목록이 6개보다 많을 때 렌더링할 페스티벌목록 수정
  let renderFestivalListData = festivalList;
  if (isTotal && renderFestivalListData.length > 6) {
    renderFestivalListData = festivalList.slice(0, 6);
  }

  // 페스티벌 전체 보기를 위한 탭 이동
  const onClickTotalBtn = () => {
    setOpenSearchTab(2);
  };

  return (
    <div className='mb-8'>
      <div className='text-base font-bold text-left'>
        <span>페스티벌 &nbsp;</span>
        <span className='text-lg font-bold text-ourPink'>{festivalList.length}</span>
        <span className='text-lg text-gray-400'>
          {isTotal && festivalList.length > 6 && <button onClick={onClickTotalBtn}>&nbsp; &gt;</button>}
        </span>
      </div>
      {renderFestivalListData.length === 0 ? (
        <div className='p-4 mt-4 text-lg font-bold text-center '>페스티벌 정보가 없습니다.</div>
      ) : (
        <div className='flex justify-center'>
          <div className='grid grid-cols-3 gap-2'>
            {renderFestivalListData.map((festival) => (
              <FestivalSearchItem key={festival.id} festival={festival} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
};
export default FestivalSearchList;
