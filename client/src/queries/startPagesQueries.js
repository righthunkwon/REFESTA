import instance from '@util/token_interceptor';
import { useQuery, useMutation } from 'react-query';

const getUserProfile = async () => {
  const response = await instance.get('/members');

  return response;
};

export const useProfileQuery = () => {
  return useQuery('getProfile', getUserProfile);
};

const postUserProfile = async (formData) => {
  const response = await instance.post('/members', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });

  return response;
};

export const usePostProfileQuery = () => {
  return useMutation('postProfile', postUserProfile);
};

const postGenre = async (selectedGenre) => {
  const response = await instance.post('members/genres', { preferGenres: selectedGenre });
  return response;
};

export const useGenreQuery = () => {
  return useMutation('postGenre', postGenre);
};

const postYoutube = async () => {
  const response = await instance.post('members/playlists');
  return response;
};

export const useYoutubeConnectQuery = () => {
  return useMutation('postYoutube', postYoutube);
};
