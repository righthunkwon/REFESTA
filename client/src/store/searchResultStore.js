import { create } from 'zustand';

const useSearchResultStore = create((set) => ({
  festivalList: [],
  setFestivalList: (data) => {
    set({ festivalList: data });
  },
  artistList: [],
  setArtistList: (data) => {
    set({ artistList: data });
  },
}));

export default useSearchResultStore;
