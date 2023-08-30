/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./error.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {},
    screens: {
      'sm': '480px'
    },
    fontFamily: {
      'logo': ['Tilt Neon', 'cursive'],
      'roboto': ['Roboto', 'sans-serif']
    },
    container: {
      center: true,
    },
  },
  plugins: [require("@tailwindcss/forms")],
}

