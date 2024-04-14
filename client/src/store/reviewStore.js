import { create } from 'zustand';

import instance from '@util/token_interceptor';

const baseURL = `${import.meta.env.VITE_PUBLIC_API_SERVER}`;

const useReviewStore = create((set) => ({
  // 페스티벌 후기 리스트
  reviewList: [],
  addReviews: async (festivalId) => {
    try {
      const response = await instance.get(`${baseURL}/festivals/${festivalId}/reviews`);
      set((state) => ({
        reviewList: response.data.data,
      }));
    } catch (error) {
      //error
    }
  },
  // 후기 작성
  registerReview: async (newReview, onSuccess) => {
    try {
      const formData = new FormData();
      formData.append('file', newReview.file);
      formData.append('festivalId', newReview.festivalId);
      formData.append('contents', newReview.contents);

      const response = await instance.post(`${baseURL}/reviews`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });
      if (onSuccess) onSuccess();
    } catch (error) {
      //error
      return null;
    }
  },

  // 내가쓴 후기 리스트
  myReviewList: [],
  addMyReviews: async () => {
    try {
      const response = await instance.get(`${baseURL}/members/reviews`);
      set((state) => ({
        myReviewList: response.data.data.reviewList,
      }));
    } catch (error) {
      //error
    }
  },

  // 후기 삭제
  removeReview: async (id) => {
    try {
      const response = await instance.delete(`${baseURL}/reviews/${id}`);
      set((state) => ({
        ...state,
        myReviewList: state.myReviewList.filter((review) => review.reviewId !== id),
      }));
    } catch (error) {
      //error
    }
  },
}));

export default useReviewStore;
