package net.ddns.jaronsky.studia.tiwpr.websockets.game.model

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class BoardTest(
        val testName: String,
        val board: String,
        val shouldWin: Boolean,
        val winner: Int,
        val possibleMoves: ArrayList<Int>
) {
    lateinit var gameBoard: Board

    companion object {
        val b = BoardGenerator()

        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0}")
        fun data(): Collection<Array<out Any>> {
            return listOf(
                    arrayOf("Row winner", b.row, true, 1, arrayListOf(1, 2, 3, 5)),
                    arrayOf("Column winner", b.col, true, 1, arrayListOf<Int>()),
                    arrayOf("Diagonal winner", b.diagnoal, true, 1, arrayListOf(0, 1, 2, 4, 5, 6)),
                    arrayOf("Count diag winner", b.cdiagnoal, true, 1, arrayListOf(0, 1, 2, 4)),
                    arrayOf("No winner", b.noWin, false, 0, arrayListOf(0, 3, 5))
            )
        }
    }

    @Before
    fun setUp() {
        gameBoard = b.toBoard(board)
    }

    @Test
    fun testWin() {
        assertEquals(shouldWin, gameBoard.isWin())
    }

    @Test
    fun testWinner() {
        assertEquals(winner, gameBoard.getWinner())
    }

    @Test
    fun testPossibleMoves() {
        assertArrayEquals(possibleMoves.toArray(), gameBoard.possibleMoves().toArray())
    }

}