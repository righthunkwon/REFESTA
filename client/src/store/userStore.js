import { create } from 'zustand';

import instance from '@util/token_interceptor';

const token =
  'eyJhbGciOiJIUzUxMiJ9.eyJhdXRoIjpbIlJPTEVfQURNSU4iXSwiYXVkIjoiaHR0cHM6Ly9qMTBhNjAxLnAuc3NhZnkuaW8vIiwic3ViIjoiMTA3OTQ4MDU4MzM1NzA4NjY1MjYwIiwiaXNzIjoiaHR0cHM6Ly9qMTBhNjAxLnAuc3NhZnkuaW8vIiwiaWF0IjoxNzExMDA0MzM2LCJleHAiOjE3MTI4MDQzMzZ9.vZzSd4v5rA1pGDeWNpkFt5HpMzYzAdFZUJIHKKwIl80KaI6rheI1fXiaQIQV9RGvDDdy-snwVOSmmZ2a0CAoUA';
const accessToken = `Bearer ${token}`;
const headers = {
  'Content-Type': 'application/json',
  Authorization: accessToken,
};
const baseURL = `${import.meta.env.VITE_PUBLIC_API_SERVER}`;

const useUserStore = create((set) => ({
  // 마이페이지 유저 정보
  userInfo: {},
  getUserInfo: async () => {
    try {
      const response = await instance.get(`${baseURL}/members`);
      set((state) => ({
        userInfo: response.data.data,
      }));
    } catch (error) {
      //error
    }
  },

  // 마이페이지 예약내역
  bookingList: [],
  getBookingList: async () => {
    try {
      const response = await instance.get(`${baseURL}/members/reservations`);
      set((state) => ({
        bookingList: response.data.data.reservationList,
      }));
    } catch (error) {
      //error
    }
  },
}));

export default useUserStore;
