import { create } from 'zustand';

const festivalInfoStore = create((set) => ({
  // 페스티벌 상세 정보
  festivalInfoData: '',
  setFestivalInfoData: (data) => {
    set({ festivalInfoData: data });
  },

  // 예정 페스티벌 상세 이미지 정보
  festivalInfoDetailData: '',
  setFestivalInfoDetailData: (data) => {
    set({ festivalInfoDetailData: data });
  },
}));

export default festivalInfoStore;
