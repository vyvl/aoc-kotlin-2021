import java.util.*

fun main() {


    fun dijkstra(matrix: List<List<Int>>): List<List<Int>> {

        val shortestPaths = MutableList(matrix.size) { MutableList(matrix[0].size) { Int.MAX_VALUE } }
        val stk = PriorityQueue<Pair<Int, Int>>(compareBy { shortestPaths[it.first][it.second] })
        shortestPaths[0][0] = 0
        stk.add(Pair(0, 0))

        while (stk.isNotEmpty()) {
            val (x, y) = stk.remove()
            if (x == matrix.size - 1 && y == matrix[x].size - 1) continue
            val currVal = shortestPaths[x][y]
            listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y - 1), Pair(x, y + 1))
                .filter { it.first in matrix.indices && it.second in matrix[0].indices }
                .filter { shortestPaths[it.first][it.second] > currVal + matrix[it.first][it.second] }
                .forEach {
                    shortestPaths[it.first][it.second] = currVal + matrix[it.first][it.second]
                    stk.add(it)
                }
        }

        return shortestPaths

    }

    fun part1(inputs: List<String>): Int {
        val matrix = inputs.map { it.map { it - '0' }.toMutableList() }.toMutableList()
        val shortestPaths = dijkstra(matrix)
        return shortestPaths[matrix.size - 1][matrix[0].size - 1]
    }

    fun generate5DMatrix(matrix: List<List<Int>>): List<List<Int>> {
        val columnIncrease = matrix.map { row ->
            (0 until 5).map { i -> row.map { if (it + i > 9) it + i - 9 else it + i } }.flatten()
        }
        return (0 until 5).flatMap { i -> columnIncrease.map { it.map { if (it + i > 9) it + i - 9 else it + i } } }
    }


    fun part2(inputs: List<String>): Int {
        val matrix = generate5DMatrix(inputs.map { it.map { it - '0' }.toMutableList() }.toMutableList())
        val shortestPaths = dijkstra(matrix)
        return shortestPaths[matrix.size - 1][matrix[0].size - 1]
    }


    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
