import FestivalItem from '@components/artistDetail/FestivalItem';

import artistfesta from '@assets/artistfesta.png';

const FestivalList = ({ festivalList }) => {
  if (festivalList && festivalList.length > 0) {
    return (
      <div className='grid grid-cols-2 gap-4 mt-4'>
        {festivalList.map((festival, index) => (
          <FestivalItem
            key={index}
            festival={festival}
          />
        ))}
      </div>
    );
  } else {
    return (
      <div className='flex flex-col items-center justify-center w-full mt-16 text-center'>
        <div className='w-20'>
          <img src={artistfesta} />
        </div>
        <div className='mt-3 font-bold'>참여한 페스티벌이 없습니다.</div>
      </div>
    );
  }
};

export default FestivalList;
