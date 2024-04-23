/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {}
  },
  daisyui: {
    themes: [
      {
        mytheme: {
          primary: "#06b6d4",
          secondary: "#666666",
          accent: "#111827",
          neutral: "#44403c",
          "base-100": "#333333",
          info: "#60a5fa",
          success: "#16a34a",
          warning: "#facc15",
          error: "#ef4444"
        }
      }
    ]
  },
  plugins: [require("daisyui")]
}
