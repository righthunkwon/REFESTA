import { useState } from 'react';
import useSetListStore from '@store/setListStore';
import useFestivalInfoStore from '@store/festivalInfoStore';

import Swal from 'sweetalert2';
import instance from '@util/token_interceptor';

import youtube_music_logo_white from '@assets/youtube_music_logo_white.png';
import loading from '@assets/loading.png';

// 셋리스트 재생목록의 각 노래
const PlayListCreateButton = () => {
  const { currSongList } = useSetListStore();
  const { festivalInfoData } = useFestivalInfoStore();
  const [isLoading, setLoading] = useState(false);

  const showConfirmDialog = async () => {
    Swal.fire({
      title: '재생목록 추가',
      html: '현재 재생목록을 <br>유튜브에 저장하시겠습니까?',
      showCancelButton: true,
      confirmButtonColor: '#061E58',
      cancelButtonColor: '#CACACA',
      confirmButtonText: '확인',
      cancelButtonText: '취소',
    }).then((response) => {
      if (response.isConfirmed) {
        try {
          createYoutubePlaylist();
        } catch (error) {
          //error
        }
      }
    });
  };

  const createYoutubePlaylist = async () => {
    setLoading(true);
    const audioUrlList = currSongList.map((song) => song.audioUrl);
    const response = await instance.post(`festivals/playlists`, {
      festivalName: `${festivalInfoData.name}`,
      audioUrlList: audioUrlList,
    });
    setLoading(false);
    if (response.data.data === 'OK') {
      // 성공 모달 띄우기
      Swal.fire({
        title: '추가 완료',
        html: '현재 재생목록이 <br>유튜브에 업데이트 되었습니다.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
    } else if (response.data.data === 'ACCEPTED') {
      // 실패 모달 띄우기
      Swal.fire({
        title: '추가 실패',
        html: '권한이 없습니다.',
        confirmButtonColor: '#061E58',
        confirmButtonText: '확인',
      });
    }
  };

  return (
    <div>
      {isLoading && (
        <div className='fixed top-0 left-0 z-50 flex items-center justify-center w-full h-full bg-black bg-opacity-50'>
          <div className='flex flex-col items-center px-10 py-4 pt-10 bg-white rounded-lg bg-opacity-90'>
            <div className='w-16'>
              <img className='w-full motion-safe:animate-spin' src={loading} alt='로딩 중' />
            </div>
            <div className='my-4 text-center text-gray-800'>정보 가져오는 중</div>
          </div>
        </div>
      )}
      {currSongList.length > 1 && (
        <div className='p-2 mx-4 my-4 text-white rounded-md bg-ourPink' onClick={showConfirmDialog}>
          <button>
            <div className='flex text-sm'>
              <span>현재 재생목록&nbsp;</span>
              <img className='w-[48px] h-full my-auto' src={youtube_music_logo_white} alt='' />
              <span>&nbsp;에 저장하기</span>
            </div>
          </button>
        </div>
      )}
    </div>
  );
};

export default PlayListCreateButton;
