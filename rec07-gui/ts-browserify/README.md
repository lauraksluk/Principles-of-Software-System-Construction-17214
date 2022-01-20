# ts-browserify

This version of Tic Tac Toe runs entirely in the browser without a server.

To get the TypeScript code into the browser (including all the packages used), we use the tool `browserify` that takes a TypeScript project and turns it into a single big js file (inlining all dependencies). That js file can then be used from within HTML and can interact with HTML. 

We have set up browserify with `npm run deploy` (see package.json for details) and it will generate `dist/tic.js`. This file is imported from `tic.html` in the root directory. 

Open the HTML file in your browser and look at the browser console to see the logging output.



## Tooling tips

It is often inconvenient to manually trigger compilation all the time. Watchify is a tool that will look for changes and will automatically rebuild the big json file, so that just reloading the page in a browser is sufficient. This is set up already with `npm run watchify` in this project.