import { useState, useEffect } from 'react';
import useSetListStore from '@store/setListStore';

import instance from '@util/token_interceptor';
import ReactPlayer from 'react-player';

import play_btn from '@assets/play_btn.png';
import pause_btn from '@assets/pause_btn.png';
import previous_btn from '@assets/previous_btn.png';
import next_btn from '@assets/next_btn.png';
import default_album_poster from '@assets/default_album_poster.jpg';

// 셋리스트 음악 플레이어
const SetListPlayer = () => {
  const { playing, setPlaying, currSong, setCurrSong, currSongList } = useSetListStore();
  const [currSongIndex, setCurrSongIndex] = useState(0);

  // 현재 재생목록 및 현재 재생 노래가 변경될 때
  // 현재 재생하는 노래의 인덱스를 변경하는 함수 실행
  useEffect(() => {
    if (currSongList && currSongList.length > 0) {
      const index = currSongList.findIndex((song) => song === currSong);
      setCurrSongIndex(index >= 0 ? index : 0);
    }
  }, [currSongList, currSong]);

  // 재생 & 일시정지 함수
  const onClickPlayButton = () => {
    setPlaying(!playing);
  };

  // 이전 곡을 재생하는 함수
  const onClickPrevButton = () => {
    if (currSongList.length > 1) {
      let newIndex = currSongIndex - 1;
      if (newIndex < 0) {
        newIndex = currSongList.length - 1;
      }
      setCurrSongIndex(newIndex);
      setCurrSong(currSongList[newIndex]);
    }
  };

  // 다음 곡을 재생하는 함수
  const onClickNextButton = () => {
    if (currSongList.length > 1) {
      let newIndex = currSongIndex + 1;
      if (newIndex >= currSongList.length) {
        newIndex = 0;
      }
      setCurrSongIndex(newIndex);
      setCurrSong(currSongList[newIndex]);
    }
  };

  // 음악을 30초 이상 들을 때마다 해당 노래의 선호도를 증가시키는 함수
  const increaseSongPreference = async () => {
    try {
      await instance.patch(`recommendations/songs/${currSong.id}`);
    } catch (error) {
      //error
    }
  };

  return (
    <div className='flex justify-between mx-4 bg-white rounded-md'>
      <div className='flex m-2'>
        <div>
          <img className='w-[40px] rounded-md min-w-[40px]' src={currSong.imageUrl || default_album_poster} alt='' />
        </div>
        <div className='ml-3'>
          <div className='text-left'>
            <div className='text-sm truncate max-w-28'>{currSong.title}</div>
            <div className='text-xs text-gray-400 truncate max-w-28'>{currSong.singer}</div>
          </div>
        </div>
      </div>
      <div className='m-2'>
        <ReactPlayer
          className='react-player'
          width='0px'
          height='0px'
          url={currSong.audioUrl} // 현재 재생 곡
          playing={playing} // 재생 여부
          onProgress={(progress) => {
            if (parseInt(progress.playedSeconds) !== 0 && parseInt(progress.playedSeconds) % 30 === 0) {
              increaseSongPreference(); // 재생 시간 별 선호도 증가
            }
          }}
        />
      </div>
      <div className='flex justify-between pr-2 my-auto mr-2 min-w-16'>
        {/* 이전 곡 재생 */}
        <button className='w-[12px]' onClick={onClickPrevButton}>
          <img src={previous_btn} />
        </button>
        {/* 재생&일시정지 */}
        <button onClick={onClickPlayButton} className='w-[16px]'>
          {playing ? <img src={pause_btn} /> : <img src={play_btn} />}
        </button>
        {/* 다음 곡 재생 */}
        <button className='w-[12px]' onClick={onClickNextButton}>
          <img src={next_btn} />
        </button>
      </div>
    </div>
  );
};

export default SetListPlayer;
