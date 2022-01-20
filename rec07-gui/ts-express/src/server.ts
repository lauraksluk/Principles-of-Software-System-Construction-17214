/**
 * this code will be run from within the tic.html page
 * 
 * it has access to the DOM of the web page
 */

import { Game, initializeGame, Player } from "./game";
import express from "express";
import exphbs from "express-handlebars"
import { Response } from "express-serve-static-core";

console.log("starting server")
const app = express()
const port = 8080;

//setting up to use handlebars for templates by default
app.engine('hbs', exphbs({
    defaultLayout: 'main',
    extname: '.hbs'
}));
app.set('view engine', 'hbs');




// game always holds the current version of the game
// updates on actions and when starting a new game
let game: Game = initializeGame()

function startNewGameClicked() {
    // changing the global game variable (in the outer closure)
    console.log("new game")
    game = initializeGame()
}

function clickCell(x: number, y: number): void {
    console.log(`click cell ${x} ${y}`)
    game = game.play(x, y)
}




/**
 * creates the data to fill into the template
 */
function genPage() {
    console.log("update page")

    let cells: any[] = []
    const board = game.board
    for (let x = 0; x <= 2; x++)
        for (let y = 0; y <= 2; y++) {
            let cell: any = {}
            const player = board.getCell(x, y)
            if (player === Player.PlayerX) cell.text = "X"
            if (player === Player.PlayerO) cell.text = "O"

            if (player === null) {
                cell.class = "playable"
                cell.link = `/play?x=${x}&y=${y}`
            }



            cells.push(cell)
        }


    return {
        cells: cells
    }
}

function renderPage(res: Response<any, Record<string, any>, number>) {
    res.render("main", genPage());
}

app.get("/newgame", (req, res) => {
    startNewGameClicked()
    renderPage(res)
});

app.get("/play", (req, res) => {
    if (req.query.x && req.query.y)
        clickCell(parseInt(req.query.x as string), parseInt(req.query.y as string))
    renderPage(res)
});


app.get("/", (req, res) => {
    renderPage(res)
});



// start the Express server
app.listen(port, () => {
    console.log(`server started at http://localhost:${port}`);
});

