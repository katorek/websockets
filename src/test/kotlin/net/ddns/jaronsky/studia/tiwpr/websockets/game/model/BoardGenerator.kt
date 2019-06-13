package net.ddns.jaronsky.studia.tiwpr.websockets.game.model

import java.util.*
import kotlin.collections.ArrayList

class BoardGenerator {


    val noWin = "" +
            "0000000\n" +
            "0000000\n" +
            "0000000\n" +
            "0000000\n" +
            "0000000\n" +
            "0000000"
    val diagnoal = "" +
            "1000000\n" +
            "0110000\n" +
            "0010000\n" +
            "0001000\n" +
            "0000000\n" +
            "0000000"
    val cdiagnoal = "" +
            "0000000\n" +
            "0000000\n" +
            "0001000\n" +
            "0010000\n" +
            "0100000\n" +
            "1000000"
    val row = "" +
            "0000000\n" +
            "0000000\n" +
            "0000000\n" +
            "0111100\n" +
            "0000000\n" +
            "0000000"
    val col = "" +
            "0000000\n" +
            "0100000\n" +
            "0100000\n" +
            "0100000\n" +
            "0100000\n" +
            "0000000"


    fun getNoWin(): Board  {
        return toBoard(noWin)
    }
    fun getRowWinGame(): Board  {
        return toBoard(row)
    }
    fun getColWinGame(): Board  {
        return toBoard(col)
    }
    fun getDiagWinGame(): Board  {
        return toBoard(diagnoal)
    }
    fun getCDiagColWinGame(): Board {
        return toBoard(cdiagnoal)
    }

    fun toBoard(stringBoard: String): Board {
        val board: Array<IntArray> = Array(6, { IntArray(7, { 0 }) })
        var i = 0
        for (s in stringBoard.split("\n")) {
            board[i++] = (parseLine(s))
        }


        return Board(matrix = board)
    }

    private fun parseLine(s: String): IntArray {
//        return s.split("").stream().mapToInt(Integer::parseInt).toArray()
        return s.chars().map { i -> i - 48 }.toArray()
    }
}