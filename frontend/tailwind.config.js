/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}",
    "./src/**/*.{html}",
  ],
  theme: {
    extend: {
      colors: {
        blue: {
          600: '#004aad',
          700: '#003d91',
        },
        orange: {
          400: '#ffb400',
        }
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      },
    },
  },
  plugins: [],
}