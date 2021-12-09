fun main() {


    fun isLowPoint(matrix: List<List<Int>>, point: Pair<Int, Int>): Boolean {
        val (x, y) = point
        val curr = matrix[x][y]
        val isTopHigher = x == 0 || matrix[x - 1][y] > curr
        val isBottomHigher = x == matrix.size - 1 || matrix[x + 1][y] > curr
        val isLeftHigher = y == 0 || matrix[x][y - 1] > curr
        val isRightHigher = y == matrix[x].size - 1 || matrix[x][y + 1] > curr

        return isLeftHigher && isRightHigher && isTopHigher && isBottomHigher

    }

    fun getLowPoints(matrix: List<List<Int>>) =
        matrix.indices.flatMap { i -> matrix[i].indices.map { j -> i to j } }
            .filter { (x, y) -> isLowPoint(matrix, x to y) }


    fun part1(inputs: List<String>): Int {
        val matrix = inputs.map { it.map { it - '0' } }
        return getLowPoints(matrix).sumOf { 1 + matrix[it.first][it.second] }
    }

    fun sizeOfBasin(matrix: List<List<Int>>, point: Pair<Int, Int>, visited: MutableSet<Pair<Int, Int>>): Int {
        val (x, y) = point
        if (x !in matrix.indices || y !in matrix[x].indices || matrix[x][y] == 9 || visited.contains(point)) {
            return 0
        }
        visited.add(point)

        return 1 + sizeOfBasin(matrix, Pair(x + 1, y), visited) +
                sizeOfBasin(matrix, Pair(x - 1, y), visited) +
                sizeOfBasin(matrix, Pair(x, y + 1), visited) +
                sizeOfBasin(matrix, Pair(x, y - 1), visited)

    }


    fun part2(inputs: List<String>): Int {
        val matrix = inputs.map { it.map { it - '0' } }

        return getLowPoints(matrix)
            .map { sizeOfBasin(matrix, Pair(it.first, it.second), mutableSetOf()) }
            .sorted().takeLast(3).reduce { acc, i -> acc * i }

    }


    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
