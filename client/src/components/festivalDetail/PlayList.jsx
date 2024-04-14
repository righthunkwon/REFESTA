import { useEffect, useRef } from 'react';
import useSetListStore from '@store/setListStore';

import PlayListItem from '@components/festivalDetail/PlayListItem';

// 셋리스트 재생목록
const PlayList = () => {
  const { selectedLineupList, songInfoMap, selectedSongInfoMap, setSelectedSongInfoMap } = useSetListStore();

  // 선택된 라인업 목록이 변경될 때마다
  // 해당하는 노래 정보를 가져와 선택된 노래 목록을 변경
  useEffect(() => {
    const updatedSelectedSongInfoMap = selectedLineupList.map((artist) => {
      return songInfoMap[artist.id] || [];
    });
    setSelectedSongInfoMap(updatedSelectedSongInfoMap);
  }, [selectedLineupList]);

  return (
    <div className='h-full m-4 bg-white'>
      {selectedSongInfoMap.length > 0 && (
        <div className='overflow-x-auto whitespace-nowrap'>
          {selectedSongInfoMap.map((songs, idx1) => (
            <ul key={idx1}>
              {songs.map((song, idx2) => (
                <PlayListItem key={idx2} song={song} />
              ))}
            </ul>
          ))}
        </div>
      )}
    </div>
  );
};

export default PlayList;
