import instance from '../util/token_interceptor';
import { useQuery } from 'react-query';

const getRecommendArtistList = async (page) => {
  const response = await instance.get(`recommendations/artists?page=${page}`);
  return response;
};

// 이 구조가 맞나? 콜백의 콜백구조가 아닌지...
export const useArtistQuery = (page) => {
  return useQuery(['getRecommend', page], () => getRecommendArtistList(page));
};
