import { Link } from 'react-router-dom';

import SetListHomeColItem from '@components/home/SetListHomeColItem';
import ListTitle from '@components/home/ListTitle';
import ItemLoading from '@components/home/loading/ItemLoading';

const SetListHomeList = ({ setListData, isLoading }) => {
  // 데이터를 2개씩 자른 새로운 배열을 만든다
  const chunkArray = (array, chunkSize) => {
    const chunkedArray = [];
    for (let i = 0; i < array.length; i += chunkSize) {
      chunkedArray.push(array.slice(i, i + chunkSize));
    }
    return chunkedArray;
  };

  return (
    <div className='h-[330px]'>
      <ListTitle
        title={'추천 셋리스트'}
        description={'셋리스트로 페스티벌을 찾아보세요!'}
        btn={
          <div className='pb-5 cursor-pointer'>
            <Link className='cursor-pointer' to='/festival/list/ended'>
              {`전체보기 >`}
            </Link>
          </div>
        }
      />
      <div className='flex px-4 overflow-x-scroll gap-x-4 scrollbar-hide whitespace-nowrap'>
        {isLoading ? (
          <ItemLoading type={'setList'} />
        ) : (
          chunkArray(setListData, 2).map((chunkItem, idx) => (
            // 2개씩 끊어서 보내줘야지
            <SetListHomeColItem key={idx} chunkItem={chunkItem} />
          ))
        )}
      </div>
    </div>
  );
};
export default SetListHomeList;
