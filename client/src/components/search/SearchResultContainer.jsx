import { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import useSearchResultStore from '@store/searchResultStore';

import instance from '@util/token_interceptor';

import SearchResultWrapper from '@components/search/SearchResultWrapper';
import FestivalSearchWrapper from '@components/search//FestivalSearchWrapper';
import ArtistSearchWrapper from '@components/search//ArtistSearchWrapper';

// 검색 결과 탭(통합검색/페스티벌/아티스트)
const SearchResultContainer = () => {
  const [openSearchTab, setOpenSearchTab] = useState(1);
  const [isTotal, setIsTotal] = useState(true);
  const { setFestivalList, setArtistList } = useSearchResultStore();
  const [searchParams] = useSearchParams();
  const word = searchParams.get('word');

  // 검색 결과 탭이 처음 렌더링될 때
  // 검색어를 바탕으로 결괄르 받아오는 함수 실행
  useEffect(() => {
    const getSearchResultData = async () => {
      try {
        const response = await instance.get(`searches/results`, {
          params: {
            word: word,
          },
        });
        setFestivalList(response.data.data.festivalList);
        setArtistList(response.data.data.artistList);
      } catch (error) {
        //error
      }
    };
    getSearchResultData();
  }, []);

  return (
    <div className=''>
      <ul className='flex h-[60px]'>
        {/* 통합검색 */}
        <li className={`w-1/3 text-center ${openSearchTab === 1 ? 'border-b-2 border-ourIndigo' : ''}`}>
          <button
            className={'w-full h-full text-xs px-2 py-2'}
            onClick={() => {
              setOpenSearchTab(1);
              setIsTotal(true);
            }}
          >
            <span className={openSearchTab === 1 ? 'text-ourIndigo' : 'text-gray-400'}>통합검색</span>
          </button>
        </li>
        {/* 페스티벌 */}
        <li className={`w-1/3 text-center ${openSearchTab === 2 ? 'border-b-2 border-ourIndigo' : ''}`}>
          <button
            className={'w-full h-full text-xs px-5 py-2'}
            onClick={() => {
              setOpenSearchTab(2);
              setIsTotal(false);
            }}
          >
            <span className={openSearchTab === 2 ? 'text-ourIndigo' : 'text-gray-400'}>페스티벌</span>
          </button>
        </li>
        {/* 아티스트 */}
        <li className={`w-1/3 text-center ${openSearchTab === 3 ? 'border-b-2 border-ourIndigo' : ''}`}>
          <button
            className={'w-full h-full text-xs px-5 py-2'}
            onClick={() => {
              setOpenSearchTab(3);
              setIsTotal(false);
            }}
          >
            <span className={openSearchTab === 3 ? 'text-ourIndigo' : 'text-gray-400'}>아티스트</span>
          </button>
        </li>
      </ul>
      <div className='h-full min-w-0 px-4 mt-4 break-words'>
        <div className='flex-auto'>
          <div className={openSearchTab === 1 ? 'block' : 'hidden'}>
            <SearchResultWrapper isTotal={isTotal} openSearchTab={openSearchTab} setOpenSearchTab={setOpenSearchTab} />
          </div>
          <div className={openSearchTab === 2 ? 'block' : 'hidden'}>
            <FestivalSearchWrapper isTotal={isTotal} />
          </div>
          <div className={openSearchTab === 3 ? 'block' : 'hidden'}>
            <ArtistSearchWrapper isTotal={isTotal} />
          </div>
        </div>
      </div>
    </div>
  );
};
export default SearchResultContainer;
