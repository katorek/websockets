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
        val winner: Int
) {
    lateinit var gameBoard: Board

    companion object {
        val b = BoardGenerator()

        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf("Row", b.row, true, 1),
                    arrayOf("Col", b.col, true, 1),
                    arrayOf("Diagonal", b.diagnoal, true, 1),
                    arrayOf("Counter diagonal", b.cdiagnoal, true, 1),
                    arrayOf("No winner", b.noWin, false, 0)
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

}