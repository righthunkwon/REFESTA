import FestivalSearchList from '@components/search/FestivalSearchList';
import ArtistSearchList from '@components/search/ArtistSearchList';

// 통합검색 결과 탭
const SearchResultWrapper = ({ isTotal, openSearchTab, setOpenSearchTab }) => {
  return (
    <>
      <div className='flex flex-col'>
        <FestivalSearchList isTotal={isTotal} openSearchTab={openSearchTab} setOpenSearchTab={setOpenSearchTab} />
        <ArtistSearchList isTotal={isTotal} openSearchTab={openSearchTab} setOpenSearchTab={setOpenSearchTab} />
      </div>
    </>
  );
};
export default SearchResultWrapper;
