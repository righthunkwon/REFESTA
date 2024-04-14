import FestivalSearchList from '@components/search/FestivalSearchList';

// 검색 결과 중 페스티벌 탭
const FestivalSearchWrapper = ({ festivalListData }) => {
  return (
    <>
      <div className='flex flex-col'>
        <FestivalSearchList festivalListData={festivalListData} />
      </div>
    </>
  );
};
export default FestivalSearchWrapper;
