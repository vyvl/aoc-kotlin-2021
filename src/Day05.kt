import kotlin.math.abs

typealias Graph = MutableList<MutableList<Int>>
typealias Point = Pair<Int, Int>
typealias Line = Pair<Point, Point>


fun main() {

    fun readGraphLine(input: String): Line {
        val pointsStr = input.split("->")

        val points = pointsStr.map { it.trim().split(",").map { it.toInt() } }.map { it[0] to it[1] }

        return points.first() to points.last()
    }

    fun generatePointsForHorizontalVerticalLines(line: Line): List<Point> {
        val (x1, y1) = line.first
        val (x2, y2) = line.second

        if (x1 == x2) {
            val yStart = minOf(y1, y2)
            val yEnd = maxOf(y1, y2)
            return (yStart..yEnd).map { x1 to it }
        } else if (y1 == y2) {
            val xStart = minOf(x1, x2)
            val xEnd = maxOf(x1, x2)
            return (xStart..xEnd).map { it to y1 }
        }

        return emptyList()
    }

    fun generatePointsForDiagonals(line: Line): List<Point> {
        val (x1, y1) = line.first
        val (x2, y2) = line.second

        if (abs(x1 - x2) == abs(y1 - y2)) {
            val numOfPoints = abs(x1 - x2) + 1
            val xDir = if (x1 < x2) 1 else -1
            val yDir = if (y1 < y2) 1 else -1
            val xCoords = (0 until numOfPoints).map { x1 + xDir * it }
            val yCoords = (0 until numOfPoints).map { y1 + yDir * it }
            return xCoords.zip(yCoords).map { it.first to it.second }
        }
        return emptyList()
    }

    fun generateGraph(rows: Int, cols: Int): Graph {

        return MutableList(rows) { MutableList(cols) { 0 } }

    }

    fun part1(inputs: List<String>): Int {
        val lines = inputs.map { readGraphLine(it) }


        val graph = generateGraph(1000, 1000)


        val allPoints = lines.flatMap { generatePointsForHorizontalVerticalLines(it) }

        allPoints.forEach { graph[it.first][it.second]++ }

        return graph.flatten().count { it > 1 }
    }

    fun part2(inputs: List<String>): Int {

        val graph = generateGraph(1000, 1000)

        val lines = inputs.map { readGraphLine(it) }

        val horizontalVerticalPoints = lines.flatMap { generatePointsForHorizontalVerticalLines(it) }
        horizontalVerticalPoints.forEach { graph[it.first][it.second]++ }

        val diagonalPoints = lines.flatMap { generatePointsForDiagonals(it) }
        diagonalPoints.forEach { graph[it.first][it.second]++ }

        return graph.flatten().count { it > 1 }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

