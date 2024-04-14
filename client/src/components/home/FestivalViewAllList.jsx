import { useEffect, useState } from 'react';

import instance from '@util/token_interceptor';

import FestivalViewAllItem from '@components/home/FestivalViewAllItem';

const FestivalViewAllList = ({ title, state }) => {
  const dummyData = [
    {
      id: 1,
      posterUrl: 'https://ticketimage.interpark.com/Play/image/large/24/24001681_p.gif',
      name: '사운드 베리 THEATER 2024 - 1일차',
      date: new Date('2024-03-16').getTime(),
      location: '올림픽공원',
      lineup: '박재범, 쌈디, 로꼬, 드비타',
    },
    {
      id: 2,
      posterUrl: 'https://ticketimage.interpark.com/Play/image/large/24/24001880_p.gif',
      name: '2024 LOVESOME - 마음 방울 채집',
      date: new Date('2024-04-27').getTime(),
      location: '올림픽공원',
      lineup: '박재범, 쌈디, 로꼬, 드비타',
    },
    {
      id: 3,
      posterUrl:
        'https://scontent.cdninstagram.com/v/t39.30808-6/432650969_18424797904063392_192097024842722525_n.jpg?stp=dst-jpg_e15&efg=eyJ2ZW5jb2RlX3RhZyI6ImltYWdlX3VybGdlbi4yMDQ4eDIwNDguc2RyIn0&_nc_ht=scontent.cdninstagram.com&_nc_cat=100&_nc_ohc=UXelEa8niSgAX-HcGTu&edm=APs17CUAAAAA&ccb=7-5&ig_cache_key=MzMyMTgyNzU1MTMwNzIyODk4Mw%3D%3D.2-ccb7-5&oh=00_AfADS05bJlvRv8AjcwVpiV50syCkozIDyIwU0iUP8UoHPQ&oe=660042F5&_nc_sid=10d13b',
      name: '사운드 베리 THEATER 2024 - 1일차',
      date: new Date('2024-03-16').getTime(),
      location: '올림픽공원',
      lineup: '박재범, 쌈디, 로꼬, 드비타',
    },
    {
      id: 4,
      posterUrl: 'https://ticketimage.interpark.com/Play/image/large/24/24001880_p.gif',
      name: '2024 LOVESOME - 마음 방울 채집',
      date: new Date('2024-04-27').getTime(),
      location: '올림픽공원',
      lineup: '박재범, 쌈디, 로꼬, 드비타',
    },
    {
      id: 5,
      posterUrl: 'https://ticketimage.interpark.com/Play/image/large/24/24001681_p.gif',
      name: '사운드 베리 THEATER 2024 - 1일차',
      date: new Date('2024-03-16').getTime(),
      location: '올림픽공원',
      lineup: '박재범, 쌈디, 로꼬, 드비타',
    },
    {
      id: 6,
      posterUrl: 'https://ticketimage.interpark.com/Play/image/large/24/24001880_p.gif',
      name: '2024 LOVESOME - 마음 방울 채집',
      date: new Date('2024-04-27').getTime(),
      location: '올림픽공원',
      lineup: '박재범, 쌈디, 로꼬, 드비타',
    },
  ];

  const [festivalData, setFestivalData] = useState([]);

  // 추천 아티스트 정보 요청
  const getRecommendArtists = async () => {
    const response = await instance.get(`recommendations/${state}-festivals`);
    setFestivalData(response.data.data);
  };

  useEffect(() => {
    getRecommendArtists();
  }, []);

  return (
    <div className='px-4'>
      <div className='mt-2 text-2xl font-semibold text-center mb-7'>{title} 페스티벌</div>
      <div className='grid gap-y-7 mb-7'>
        {festivalData.map((item) => (
          <FestivalViewAllItem key={item.id} {...item} />
        ))}
      </div>
    </div>
  );
};
export default FestivalViewAllList;
