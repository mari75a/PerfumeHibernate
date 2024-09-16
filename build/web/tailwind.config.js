
module.exports = {
  content: [
    "./src/**/*.{html,js}",
    "./node_modules/tw-elements/js/**/*.js",
     "./node_modules/flowbite/**/*.js"
  ],
  plugins: [require("tw-elements/plugin.cjs"), require('flowbite/plugin')],
 
  darkMode: "class"
};