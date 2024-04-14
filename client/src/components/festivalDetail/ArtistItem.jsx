import { useEffect } from 'react';
import useSetListStore from '@store/setListStore';

import select from '@assets/select.png';

// 셋리스트 아티스트 아이템
const ArtistItem = ({ artist }) => {
  const { selectedLineupList, setSelectedLineupList, songInfoMap, currSongList, setCurrSongList } = useSetListStore();

  // 아티스트 별 선택여부 판단
  const isSelectedArtist = selectedLineupList.includes(artist);

  // 아티스트 선택 여부를 토글하는 함수
  const toggleSelect = () => {
    if (!isSelectedArtist) {
      setSelectedLineupList([...selectedLineupList, artist]); // 추가
      const additionalCurrSongList = [];
      songInfoMap[artist.id].forEach((song) => additionalCurrSongList.push(song));
      setCurrSongList([...currSongList, ...additionalCurrSongList]);
    } else {
      setSelectedLineupList(selectedLineupList.filter((item) => item !== artist)); // 제거
      setCurrSongList(currSongList.filter((song) => !songInfoMap[artist.id].includes(song)));
    }
  };

  return (
    <li className='flex-col p-2'>
      <div className='relative mb-2 h-[40px]' onClick={toggleSelect}>
        {isSelectedArtist && <img className='absolute w-[40px] rounded-full ' src={select} alt='' />}
        <img className='w-[40px] h-full rounded-full object-cover' src={artist.pictureUrl} alt='' />
      </div>
      <div className='w-[40px] text-xs truncate'>{artist.name}</div>
    </li>
  );
};

export default ArtistItem;
