enum Player {
    PlayerX = 0,
    PlayerO = 1
}
type PlayerOrEmpty = Player | null


interface Game {
    readonly board: Board
    getNextPlayer(): Player
    getWinner(): PlayerOrEmpty
    play(x: number, y: number): Game
}

interface Board {
    updateCell(x: number, y: number, player: Player): Board
    getCell(x: number, y: number): PlayerOrEmpty
}


function initializeGame(): Game {
    return newGame(newEmptyBoard(), Player.PlayerX, [])
}
function newGame(board: Board, nextPlayer: Player, history: Game[]): Game {
    return {
        board: board,
        play: function (x: number, y: number): Game {
            if (board.getCell(x,y)!==null) return this
            if (this.getWinner()!==null) return this
            const newHistory = history.slice()
            newHistory.push(this)
            return newGame(
                board.updateCell(x, y, nextPlayer),
                1 - nextPlayer,
                newHistory)
        },
        getWinner: function (): PlayerOrEmpty {
            for (let row = 0; row < 3; row++)
                if (board.getCell(row, 0) !== null && board.getCell(row, 0) === board.getCell(row, 1) && board.getCell(row, 1) === board.getCell(row, 2)) return board.getCell(row, 0)
            for (let col = 0; col < 3; col++)
                if (board.getCell(0, col) !== null && board.getCell(0, col) === board.getCell(1, col) && board.getCell(0, col) === board.getCell(2, col)) return board.getCell(0, col)
            if (board.getCell(1, 1) !== null && board.getCell(0, 0) === board.getCell(1, 1) && board.getCell(1, 1) === board.getCell(2, 2)) return board.getCell(1, 1)
            if (board.getCell(1, 1) !== null && board.getCell(0, 2) === board.getCell(1, 1) && board.getCell(1, 1) === board.getCell(2, 0)) return board.getCell(1, 1)
            return null
        },
        getNextPlayer: function () {
            return nextPlayer
        }
    }
}

function newEmptyBoard(): Board {
    return newBoard(new Array(9).fill(null, 0, 9))
}




function newBoard(cells: PlayerOrEmpty[]): Board {
    return {
        updateCell: function (x: number, y: number, player: Player): Board {
            const newCells = cells.slice()
            newCells[y * 3 + x] = player
            return newBoard(newCells)
        },
        getCell: function (x, y) {
            return cells[y * 3 + x]
        }
    }
}

export { Game, initializeGame, Player }