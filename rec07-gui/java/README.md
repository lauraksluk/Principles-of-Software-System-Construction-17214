# Java

This version of Tic Tac Toe starts a local web server using the NanoHTTPD dependency. After compiling the project, the server can be started with by running `main` in `game/App.java`, or from the command line with `mvn exec:exec`.

The webserver listens on localhost on a given port (8080 by default). It does not support multiple players at the same time, as there is no session handling (this could be added).

The implementation uses the `handlebars` template library like in HW4 for rendering the web page. The webserver listens on localhost for links to `/play`, `/newgame` and `/undo` upon which it will change the game state and return a newly rendered page.
