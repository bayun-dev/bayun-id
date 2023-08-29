import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: '../main/resources/ui',
    rollupOptions: {
      input: {
        login: './login.html',
        signup: './signup.html',
        index: './index.html'
      }
    }
  },
  server: {
    proxy: {
      // api proxy
      '/api': 'http://localhost:8181',

      // view proxy
      '^\/login(?!\.html)': {
          target: 'http://localhost:5173',
          rewrite: path => 'login.html'
      },
      '^\/signup(?!\.html)': {
        target: 'http://localhost:5173',
        rewrite: path => 'signup.html'
      },
      '/avatar': 'http://localhost:8181'

    }
  }
})
