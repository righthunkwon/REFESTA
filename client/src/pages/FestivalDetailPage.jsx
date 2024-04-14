import { useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useSetListStore from '@store/setListStore';
import useFestivalInfoStore from '@store/festivalInfoStore';

import instance from '@util/token_interceptor';

import FestivalInfo from '@components/festivalDetail/FestivalInfo';
import FestivalInfoDetail from '@components/festivalDetail/FestivalInfoDetail';
import ReservationButton from '@components/festivalDetail/ReservationButton';
import FestivalInfoContainer from '@components/festivalDetail/FestivalInfoContainer';

// 페스티벌 상세보기 페이지
const FestivalDetailPage = () => {
  const { id } = useParams();
  const nav = useNavigate();
  const { festivalInfoData, setFestivalInfoData, setFestivalInfoDetailData } = useFestivalInfoStore();
  const {
    lineupList,
    addLineupList,
    setSelectedLineupList,
    songInfoMap,
    addSongInfoMap,
    sortedSongInfoMap,
    sortSongInfoMapByLineupList,
    setSelectedSongInfoMap,
    setCurrSong,
    setCurrSongList,
    setPlaying,
  } = useSetListStore();

  // 페이지가 처음 렌더링 될 때
  // 페스티벌 정보를 가져오는 함수 실행
  useEffect(() => {
    const getFestivalInfoData = async () => {
      try {
        const response = await instance.get(`festivals/${id}`);
        if (response.data.status === 'success' && response.data.data !== null) {
          setFestivalInfoData(response.data.data);
        } else {
          nav('/Notfound');
        }
      } catch (error) {
        //error
      }
    };

    // 페스티벌 조회수 증가
    const increaseFestivalViewCount = async () => {
      try {
        const response = await instance.patch(`recommendations/festivals/${id}?point=1`);
      } catch (e) {
        //error
      }
    };

    getFestivalInfoData();
    increaseFestivalViewCount();
  }, []);

  // 파라미터에 정상적인 값이 들어가지 않았을 때
  // 404페이지로 이동
  useEffect(() => {
    if (isNaN(id)) {
      nav('/Notfound');
    }
  }, [id]);

  // 페스티벌 정보를 바탕으로
  // 예정 페스티벌과 완료 페스티벌에 따라
  // 필요한 정보를 가져오는 함수를 각각 분기처리하여 실행
  useEffect(() => {
    // 예정 페스티벌의 경우에 처리할 함수
    if (festivalInfoData && !festivalInfoData.ended) {
      const getFestivalInfoDetailData = async () => {
        try {
          const response = await instance.get(`festivals/${id}/info`);
          setFestivalInfoDetailData(response.data.data.infoImgUrl);
        } catch (error) {
          //error
        }
      };
      getFestivalInfoDetailData();
    }
    // 완료 페스티벌의 경우에 처리할 함수
    else if (festivalInfoData && festivalInfoData.ended) {
      const getSetListData = async () => {
        try {
          const response = await instance.get(`festivals/${id}/songs`);
          addLineupList(response.data.data.lineupList);
          addSongInfoMap(response.data.data.songInfoMap);
          setSelectedLineupList(response.data.data.lineupList);
        } catch (error) {
          //error
        }
      };
      getSetListData();
    }
  }, [festivalInfoData]);

  // 페스티벌 라인업과 노래 정보를 바탕으로
  // 노래 정보를 순서에 맞춰 정렬하는 함수 실행
  useEffect(() => {
    if (lineupList.length > 0) {
      sortSongInfoMapByLineupList(lineupList, songInfoMap);
    }
  }, [lineupList]);

  // 정렬된 노래 정보를 바탕으로
  // 초기 재생할 노래와 현재 재생 목록을 설정하는 함수 실행
  useEffect(() => {
    if (lineupList.length > 0) {
      setSelectedSongInfoMap(sortedSongInfoMap);
      setCurrSong(sortedSongInfoMap[0][0]);
      const allSongs = lineupList.flatMap((artist) => songInfoMap[artist.id].flatMap((song) => song));
      setCurrSongList(allSongs);
    }
  }, [sortedSongInfoMap]);

  // 페이지가 unmount될 때
  // 재생 여부를 false로 초기화
  useEffect(() => {
    return () => {
      setPlaying(false);
    };
  }, []);

  return (
    <div>
      {festivalInfoData && !festivalInfoData.ended ? (
        // 예정 페스티벌
        <div>
          <FestivalInfo /> {/* 페스티벌 기본 정보 */}
          <FestivalInfoDetail /> {/* 예정 페스티벌 이미지 정보 */}
          <ReservationButton /> {/* 예매 버튼 */}
        </div>
      ) : (
        // 완료 페스티벌
        <div>
          <FestivalInfo /> {/* 페스티벌 기본 정보 */}
          <FestivalInfoContainer /> {/* 페스티벌 셋리스트 & 후기게시판 탭 */}
        </div>
      )}
    </div>
  );
};

export default FestivalDetailPage;
