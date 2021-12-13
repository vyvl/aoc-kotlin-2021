fun main() {

    fun validPoint(matrix: MutableList<MutableList<Int>>, point: Pair<Int, Int>): Boolean {
        val (x, y) = point
        return x in matrix.indices && y in matrix[x].indices
    }

    fun flash(
        matrix: MutableList<MutableList<Int>>, point: Pair<Int, Int>
    ) {
        val x = point.first
        val y = point.second
        matrix[x][y] = 0
        for (dx in -1..1) {
            for (dy in -1..1) {
                val newPoint = Pair(x + dx, y + dy)
                val (nx, ny) = newPoint
                if ((dx == 0 && dy == 0) || !validPoint(
                        matrix, newPoint
                    ) || matrix[nx][ny] == 0 || matrix[nx][ny] == 10
                ) continue

                matrix[nx][ny]++
            }
        }
    }

    fun step(matrix: MutableList<MutableList<Int>>) {
        for (x in matrix.indices) {
            for (y in matrix[x].indices) {
                matrix[x][y]++
            }
        }
        while (matrix.flatten().any { it == 10 }) {
            for (x in matrix.indices) {
                for (y in matrix[x].indices) {
                    if (matrix[x][y] == 10) {
                        flash(matrix, Pair(x, y))
                    }
                }
            }
        }
    }

    fun part1(inputs: List<String>): Int {
        val matrix = inputs.map { it.map { it - '0' }.toMutableList() }.toMutableList()
        var count = 0

        repeat(100) {
            step(matrix)
            count += matrix.flatten().count { it == 0 }
        }
        return count
    }


    fun part2(inputs: List<String>): Int {
        val matrix = inputs.map { it.map { it - '0' }.toMutableList() }.toMutableList()
        var index = 0
        while (true) {
            index++
            step(matrix)
            if (matrix.flatten().all { it == 0 }) {
                return index
            }
        }
    }


    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
