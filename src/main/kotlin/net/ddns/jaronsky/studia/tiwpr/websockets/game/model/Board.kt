package net.ddns.jaronsky.studia.tiwpr.websockets.game.model

import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.streams.toList

class Board(
        var matrix: Array<IntArray> = Array(6, { IntArray(7, { 0 }) })
) {

    fun testWin(): Pair<Boolean, Int> {
        return IntStream.range(0, 4)
                .mapToObj(this::check)
                .filter(Pair<Boolean, Int>::first)
                .findFirst().orElse(Pair(false, 0))
    }

    fun isWin(): Boolean {
        return testWin().first
    }

    fun getWinner(): Int {
        return testWin().second
    }

    private fun check(i: Int): Pair<Boolean, Int> {
        when (i) {
            0 -> return checkColumn()
            1 -> return checkRow()
            2 -> return checkDiagonal()
            else -> return checkCounterDiagonal()
        }
    }

    private fun checkColumn(): Pair<Boolean, Int> {
        for (i in 0..matrix.size - 4) {
            for (j in 0..matrix[i].size - 1) {
                val el = matrix[i][j]
                if (el != 0 &&
                        el == matrix[i + 1][j] &&
                        el == matrix[i + 2][j] &&
                        el == matrix[i + 3][j]) {
                    return Pair(true, el)
                }
            }
        }
        return Pair(false, 0)
    }

    private fun checkRow(): Pair<Boolean, Int> {
        for (i in 0..matrix.size - 1) {
            for (j in 0..matrix[i].size - 4) {
                val el = matrix[i][j]
                if (el != 0 &&
                        el == matrix[i][j + 1] &&
                        el == matrix[i][j + 2] &&
                        el == matrix[i][j + 3]) {
                    return Pair(true, el)
                }
            }
        }
        return Pair(false, 0)
    }

    private fun checkDiagonal(): Pair<Boolean, Int> {
        for (i in 0..matrix.size - 4) {
            for (j in 0..matrix[i].size - 4) {
                val el = matrix[i][j]
                if (el != 0 &&
                        el == matrix[i + 1][j + 1] &&
                        el == matrix[i + 2][j + 2] &&
                        el == matrix[i + 3][j + 3]) {
                    return Pair(true, el)
                }
            }
        }
        return Pair(false, 0)
    }

    private fun checkCounterDiagonal(): Pair<Boolean, Int> {
        for (i in 0..matrix.size - 4) {
            for (j in 3..matrix[i].size - 1) {
                val el = matrix[i][j]
                if (el != 0 &&
                        el == matrix[i + 1][j - 1] &&
                        el == matrix[i + 2][j - 2] &&
                        el == matrix[i + 3][j - 3]) {
                    return Pair(true, el)
                }
            }
        }
        return Pair(false, 0)
    }

    private fun getColumn(column: Int): List<Int> {
        return Arrays.stream(matrix).map { arr -> arr.get(column) }.toList()
    }

    fun possibleMoves(): ArrayList<Int> {
        val moves = arrayListOf<Int>()
        for ((idx, arr) in matrix[matrix.size - 1].withIndex()) {
            if (arr == 0) moves.add(idx)
        }
        return moves
    }

    fun makeMove(column: Int, player: Int) {
        val col = getColumn(column)
        for ((idx, value) in col.withIndex()) {
            if (value == 0) {
                matrix[idx][column] = player
                break
            }
        }
    }

    override fun toString(): String {
        val x = Arrays.stream(matrix)
                .map { it!!.joinToString("") }
                .collect(Collectors.joining(""))

        return Arrays.stream(matrix)
                .map { it!!.joinToString("") }
                .collect(Collectors.joining(""))

//        return Arrays.stream(matrix).map { Arrays.stream(it).map(it.toString()) }.reduce()
    }

}