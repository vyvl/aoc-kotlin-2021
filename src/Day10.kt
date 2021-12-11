import java.util.*


enum class LineType {
    COMPLETE, INCOMPLETE, CORRUPT
}


fun main() {

    fun getLineType(line: String): LineType {
        val stk = Stack<Char>()
        val chunkMap = mapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
        for (c in line) {
            if ("([{<".contains(c)) {
                stk.push(c)
            } else if (c != chunkMap[stk.pop()]) {
                return LineType.CORRUPT
            }
        }
        return if (stk.isEmpty()) LineType.COMPLETE else LineType.INCOMPLETE
    }

    fun getCorruptedSyntaxScoreOfLine(line: String): Int {
        val stk = Stack<Char>()
        val chunkMap = mapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
        val scoreMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

        var score = 0
        for (c in line) {
            if ("([{<".contains(c)) {
                stk.push(c)
            } else if (c != chunkMap[stk.pop()]) {
                score += scoreMap[c]!!
            }
        }
        return score
    }


    fun getIncompleteSyntaxScoreOfLine(line: String): Long {
        val stk = Stack<Char>()
        val chunkMap = mapOf('(' to ')', '{' to '}', '[' to ']', '<' to '>')
        val scoreMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

        var score = 0L
        for (c in line) {
            if ("([{<".contains(c)) {
                stk.push(c)
            } else if (c != chunkMap[stk.pop()]) {
                return 0
            }
        }
        while (stk.isNotEmpty()) {
            score = score * 5 + scoreMap[chunkMap[stk.pop()]]!!
        }
        return score
    }


    fun part1(inputs: List<String>): Int {
        return inputs.filter { getLineType(it) == LineType.CORRUPT }.sumOf { getCorruptedSyntaxScoreOfLine(it) }
    }


    fun part2(inputs: List<String>): Long {
        val scores = inputs.filter { getLineType(it) == LineType.INCOMPLETE }.map { getIncompleteSyntaxScoreOfLine(it) }
            .sorted()
        return scores[scores.size / 2]

    }


    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
