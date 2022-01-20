# ts-express

This version of Tic Tac Toe starts a local web server using the `express` package. After compiling the project, the server can be started with `node dist/server.js` (or `npm run serve`)

The webserver listens on localhost on a given port (see command line output). It does not support multiple players at the same time, as there is no session handling (this could be added).

The implementation uses the `handlebars` template library like in HW4 for rendering the web page. The webserver listens on localhost for links to `/play`, `/newgame` and `/undo` upon which it will change the game state and return a newly rendered page.



## Tooling tips

It is often inconvenient to manually trigger compilation and restart the server all the time. The tool `nodemon` automatically watches for changed files and triggers a rebuild. The `package.json` file is configured to run `nodemon -e ts --exec "tsc & node dist/server.js"` with `npm run dev`, which will monitor for changes to ts files and will then run the compiler and restart the server.