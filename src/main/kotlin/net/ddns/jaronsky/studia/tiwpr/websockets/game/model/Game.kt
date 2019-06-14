package net.ddns.jaronsky.studia.tiwpr.websockets.game.model

class Game {
    val board = Board()

    fun getPossibleMoves(): ArrayList<Int> {
        return board.possibleMoves()
    }

    fun makeMove(column: Int, player: Int) {
        board.makeMove(column, player)
    }

}