import useSetListStore from '@store/setListStore';

// 셋리스트 재생목록의 각 노래
const PlayListItem = ({ song }) => {
  const { currSong, setCurrSong } = useSetListStore();

  // 노래를 선택했을 때 현재 노래를 변경하는 함수
  const handleSongSelect = () => {
    setCurrSong(song);
  };

  return (
    <div
      className={`border-b border-y-gray-400 ${currSong === song ? 'bg-gray-200 transform scale-103' : ''}`}
      onClick={handleSongSelect}
    >
      <li className='flex items-center py-2 ml-4'>
        <div className='relative'>
          <img className={`w-12 rounded-md min-w-12`} src={song.imageUrl} alt='' />
          {currSong === song && (
            <div>
              <div
                className='absolute right-[2px] w-[3px] h-[12px] bottom-1 bg-gradient-to-tr from-transparent to-ourPink animate-bounce'
                style={{ animationDelay: '400ms' }}
              >
                &nbsp;
              </div>
              <div
                className='absolute w-[3px] h-[10px] bottom-1 right-[6px] bg-gradient-to-tr from-transparent to-ourPink animate-bounce'
                style={{ animationDelay: '10ms' }}
              >
                &nbsp;
              </div>
              <div
                className='absolute w-[3px] h-[14px] bottom-1 right-[10px] bg-gradient-to-tr from-transparent to-ourPink animate-bounce'
                style={{ animationDelay: '200ms' }}
              >
                &nbsp;
              </div>
            </div>
          )}
        </div>
        <div>
          <div className='w-56 ml-2 overflow-hidden text-sm text-left whitespace-nowrap text-ellipsis'>
            {song.title}
          </div>
          <div className='mt-1 ml-2 text-xs text-left text-gray-400'>{song.singer}</div>
        </div>
      </li>
    </div>
  );
};

export default PlayListItem;
