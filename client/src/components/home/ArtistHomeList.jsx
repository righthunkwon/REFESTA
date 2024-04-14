import { useEffect, useState } from 'react';

import instance from '@util/token_interceptor';
import { useArtistQuery } from '@queries/homeListQueries';

import ArtistHomeItem from '@components/home/ArtistHomeItem';
import ListTitle from '@components/home/ListTitle';
import ItemLoading from '@components/home/loading/ItemLoading';

import refresh from '@assets/refresh.png';

const ArtistHomeList = () => {
  // 페이지 번호
  const [page, setPage] = useState(1);
  const { data, isLoading, isError, error } = useArtistQuery(page);

  const [artistData, setArtistData] = useState([]);

  const onClickRefresh = () => {
    setPage(page + 1);
  };

  useEffect(() => {
    if (!isLoading) setArtistData(data.data.data.artistInfoList);
    //error
  }, [data]);

  return (
    <div className='h-[270px]'>
      <ListTitle
        title={'추천 아티스트'}
        description={'아티스트로 페스티벌을 찾아보세요!'}
        btn={
          <div className='w-[25px] ml-auto mb-2 cursor-pointer '>
            <img src={refresh} onClick={onClickRefresh} />
          </div>
        }
      />
      <div className='flex px-4 overflow-x-scroll gap-x-3 scrollbar-hide'>
        {isLoading ? (
          <ItemLoading type={'artist'} />
        ) : (
          artistData.map((item) => <ArtistHomeItem key={item.id} {...item} />)
        )}
      </div>
    </div>
  );
};
export default ArtistHomeList;
