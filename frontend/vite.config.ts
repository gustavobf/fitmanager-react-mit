import react from '@vitejs/plugin-react'
import { defineConfig, loadEnv } from 'vite'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const ambiente = loadEnv(mode, process.cwd(), 'API_')

  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api': {
          target: ambiente.API_PROXY_TARGET || 'http://localhost:8080',
          changeOrigin: true,
        },
      },
    },
  }
})
