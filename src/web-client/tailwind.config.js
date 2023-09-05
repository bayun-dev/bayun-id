/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./error.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      spacing: {
        'xs': '360px',
        'sm': '480px',
        'md': '720px',
        'lg': '960px',
      },
    },
    screens: {
      'xs': '360px',
      'sm': '480px',
      'md': '720px',
      'lg': '960px',
    },
    fontFamily: {
      'logo': ['Tourney', 'cursive'],
      'roboto': ['Roboto', 'sans-serif']
    },
    container: {
      center: true,
    },
  },
  plugins: [require("@tailwindcss/forms")],
}

