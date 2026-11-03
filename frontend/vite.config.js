import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  build: {
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (!id.includes('node_modules')) return undefined;
          if (/node_modules\/(react|react-dom|react-router|scheduler)\//.test(id)) return 'react-vendor';
          if (/node_modules\/(recharts|d3-)/.test(id)) return 'charts-vendor';
          if (/node_modules\/lucide-react\//.test(id)) return 'icons-vendor';
          if (/node_modules\/bootstrap\//.test(id)) return 'bootstrap-vendor';
          return undefined;
        },
      },
    },
  },
  server: {
    port: 3000,
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/ws': {
        target: 'http://localhost:8080',
        ws: true,
      }
    }
  }
});
