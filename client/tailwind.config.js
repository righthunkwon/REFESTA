/** @type {import('tailwindcss').Config} */
import tailwindScrollbarHide from 'tailwind-scrollbar-hide';
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        ourPink: '#F6648B',
        ourIndigo: '#061E58',
        ourBrightIndigo: '#18608C',
        ourBrightGray: '#ECECEC',
      },
      height: {
        myVh: '45vh',
      },
      width: {
        myVw: '75vw',
      },
      keyframes: {
        slideInFromLeft: {
          from: { transform: 'translateX(100%)' },
          to: { transform: 'translateX(0)' },
        },
      },
      animation: {
        modalAnimation: 'slideInFromLeft 0.2s ease-in-out',
      },
    },
    fontFamily: {
      Pretendard: ['Pretendard'],
    },
  },
  plugins: [tailwindScrollbarHide],
};
