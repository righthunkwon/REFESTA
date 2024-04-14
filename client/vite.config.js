import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { resolve } from 'path';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: [
      { find: '@', replacement: resolve('src') },
      { find: '@assets', replacement: resolve('src/assets') },
      { find: '@components', replacement: resolve('src/components') },
      { find: '@hooks', replacement: resolve('src/hooks') },
      { find: '@layout', replacement: resolve('src/layout') },
      { find: '@pages', replacement: resolve('src/pages') },
      { find: '@queries', replacement: resolve('src/queries') },
      { find: '@store', replacement: resolve('src/store') },
      { find: '@util', replacement: resolve('src/util') },
    ],
  },
});
