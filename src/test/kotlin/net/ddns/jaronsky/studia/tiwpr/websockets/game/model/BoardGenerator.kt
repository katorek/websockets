package net.ddns.jaronsky.studia.tiwpr.websockets.game.model

import java.util.*
import kotlin.collections.ArrayList

class BoardGenerator {


    val noWin = "" +
            "1212121\n" +
            "1212121\n" +
            "1212121\n" +
            "2121212\n" +
            "2121212\n" +
            "0120102"
    val diagnoal = "" +
            "1221212\n" +
            "0112110\n" +
            "0212200\n" +
            "0011000\n" +
            "0001000\n" +
            "0002000"
    val cdiagnoal = "" +
            "1112111\n" +
            "1112100\n" +
            "1121000\n" +
            "2212000\n" +
            "2212000\n" +
            "0002022"
    val row = "" +
            "1212121\n" +
            "2121212\n" +
            "2121212\n" +
            "2111122\n" +
            "1212121\n" +
            "1000201"
    val col = "" +
            "0000000\n" +
            "0100000\n" +
            "0100000\n" +
            "0100000\n" +
            "0100000\n" +
            "1212122"

    fun toBoard(stringBoard: String): Board {
        val board: Array<IntArray> = Array(6, { IntArray(7, { 0 }) })
        var i = 0
        for (s in stringBoard.split("\n")) {
            board[i++] = (parseLine(s))
        }


        return Board(matrix = board)
    }

    private fun parseLine(s: String): IntArray {
        return s.chars().map { i -> i - 48 }.toArray()
    }
}