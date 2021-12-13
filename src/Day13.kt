typealias Paper = MutableList<MutableList<Boolean>>

fun main() {


    fun generatePaper(inputs: List<String>): Paper {
        val points = inputs.takeWhile { it.isNotBlank() }.map { it.split(",") }.map { it[0].toInt() to it[1].toInt() }
        val xLen = points.maxOf { it.first }
        val yLen = points.maxOf { it.second }

        val paper = MutableList(yLen + 1) { MutableList(xLen + 1) { false } }
        points.forEach {
            paper[it.second][it.first] = true
        }
        return paper
    }

    fun getInstructions(inputs: List<String>): List<Pair<String, Int>> {
        return inputs.dropWhile { it.isNotBlank() }.drop(1).map { it.split(" ")[2] }
            .map { it.split("=")[0] to it.split("=")[1].toInt() }
    }


    fun foldYAxis(paper: Paper, value: Int): Paper {

        for (i in value + 1 until paper.size) {
            for (j in 0 until paper[0].size) {
                paper[2 * value - i][j] = paper[i][j] || paper[2 * value - i][j]
            }
        }
        return paper.take(value).toMutableList()
    }

    fun foldXAxis(paper: Paper, value: Int): Paper {
        for (j in value + 1 until paper[0].size) {
            for (i in 0 until paper.size) {
                paper[i][2 * value - j] = paper[i][j] || paper[i][2 * value - j]
            }
        }
        return paper.map { it.take(value).toMutableList() }.toMutableList()
    }

    fun foldPaper(
        paper: Paper,
        instructions: List<Pair<String, Int>>
    ): Paper {
        var newPaper = paper
        instructions.forEach {
            newPaper = when (it.first) {
                "x" -> foldXAxis(newPaper, it.second)
                "y" -> foldYAxis(newPaper, it.second)
                else -> throw IllegalArgumentException("Unknown instruction ${it.first}")
            }
        }
        return newPaper
    }


    fun part1(inputs: List<String>): Int {
        val paper = generatePaper(inputs)

        val instructions = getInstructions(inputs)

        val newPaper = foldPaper(paper, instructions.take(1))

        return newPaper.flatten().count { it }
    }


    fun part2(inputs: List<String>): Int {
        var paper = generatePaper(inputs)

        val instructions = getInstructions(inputs)

        paper = foldPaper(paper, instructions)

        // print paper to identify the characters
        paper.forEach { println(it.joinToString("") { b -> if (b) "#" else "." }) }

        return paper.flatten().count { it }

    }


    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == 16)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
