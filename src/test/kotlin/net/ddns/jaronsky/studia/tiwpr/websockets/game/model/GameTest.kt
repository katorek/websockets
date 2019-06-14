package net.ddns.jaronsky.studia.tiwpr.websockets.game.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameTest {
    val boardGenerator = BoardGenerator()
    lateinit var game: Game


    @Before
    fun setUp() {
        game = Game()
    }

    @Test
    fun t1() {
        game.board.matrix = boardGenerator.toBoard(boardGenerator.row).matrix
        // val moves = game.getPossibleMoves() //1000201 -> 1, 2, 3, 5
        game.makeMove(1, 1)
        assertArrayEquals(arrayOf(2, 3, 5), game.getPossibleMoves().toArray())
    }

    @Test
    fun t2() {
        game.board.matrix = boardGenerator.toBoard(boardGenerator.cdiagnoal).matrix
        game.makeMove(4, 1)
        assertArrayEquals(arrayOf(0, 1, 2, 4), game.getPossibleMoves().toArray())
    }

    @Test
    fun multipleMoves() {
        game.board.matrix = boardGenerator.toBoard(boardGenerator.diagnoal).matrix
        game.makeMove(2, 1)
        assertArrayEquals(arrayOf(0, 1, 2, 4, 5, 6), game.getPossibleMoves().toArray())
        game.makeMove(2, 2)
        assertArrayEquals(arrayOf(0, 1, 4, 5, 6), game.getPossibleMoves().toArray())
    }

}