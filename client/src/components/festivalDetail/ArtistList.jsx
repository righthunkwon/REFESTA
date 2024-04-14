import { useEffect } from 'react';
import useSetListStore from '@store/setListStore';

import ArtistItem from '@components/festivalDetail/ArtistItem';

import all_album from '@assets/all_album.jpg';
import select from '@assets/select.png';

// 셋리스트 아티스트 리스트
const ArtistList = () => {
  const {
    lineupList,
    selectedLineupList,
    setSelectedLineupList,
    songInfoMap,
    isAllSelected,
    setAllSelected,
    setCurrSongList,
  } = useSetListStore();

  // 라인업의 상태가 변할 때
  // 전체 선택 여부를 판단하는 함수 실행
  useEffect(() => {
    if (lineupList.length === selectedLineupList.length) {
      setAllSelected(true);
    } else {
      setAllSelected(false);
    }
  }, [lineupList, selectedLineupList, setAllSelected]);

  // 셋리스트 라인업에서 전체선택 버튼을 관리하는 함수
  const toggleAllSelect = () => {
    if (isAllSelected) {
      setSelectedLineupList([]); // 라인업 초기화
      setCurrSongList([]); // 현재 재생목록 초기화
    } else {
      setSelectedLineupList([...lineupList]); // 라인업 전체 추가
      const allSongs = lineupList.flatMap((artist) => songInfoMap[artist.id].map((song) => song));
      setCurrSongList(allSongs); // 재생목록 전체 노래 추가
    }
    setAllSelected(!isAllSelected); // 버튼 토글
  };

  return (
    <div>
      <ul className='flex p-4 overflow-x-auto text-white scrollbar-hide'>
        {/* 전체선택 */}
        <li className='flex-col p-2'>
          <div onClick={toggleAllSelect} className='relative mb-2'>
            {isAllSelected && <img className='absolute w-12 rounded-full ' src={select} alt='' />}
            <img className='w-[40px] rounded-full' src={all_album} alt='' />
          </div>
          <div className='w-[40px] text-xs truncate'>전체</div>
        </li>
        {/* 개별가수선택 */}
        {lineupList.map((artist) => (
          <ArtistItem key={artist.id} artist={artist} isSelected={selectedLineupList.includes(artist)} />
        ))}
      </ul>
    </div>
  );
};

export default ArtistList;
