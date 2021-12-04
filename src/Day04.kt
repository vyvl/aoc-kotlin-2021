// a type alias to simplify board
typealias Board = List<List<Int>>

fun main() {

    fun readNumbersFromString(str: String, regex: Regex): List<Int> {
        return str.trim().split(regex).map { it.toInt() }
    }

    fun readBoard(lines: List<String>): Board {
        //verify board has 5 rows to catch parsing errors
        assert(lines.size == 5)
        // use regex as separator as some numbers are separated with multiple spaces in sample input
        return lines.map { readNumbersFromString(it, """\s+""".toRegex()) }
    }

    fun parseNumbersAndBoards(inputs: List<String>): Pair<List<Int>, List<Board>> {
        val numbers = readNumbersFromString(inputs.first(), ",".toRegex())
        //assume first line of input contains random numbers generated, second line is empty, next 5 lines is board, followed by empty line and so on.
        //drop numbers line and empty line, chunk 6 lines (5 board + 1 separator empty line)
        val boards = inputs.drop(2).chunked(6).map { it.dropLast(1) }.map { readBoard(it) }
        return Pair(numbers, boards)
    }

    // assume all inputs are >=0 , so mark visited number as -1
    fun markBoard(board: Board, number: Int): Board {
        return board.map { it.map { num -> if (num == number) -1 else num } }
    }

    fun hasBoardWon(board: Board): Boolean {
        val isHorizontalWin = board.any { it.all { i -> i == -1 } }
        val isVerticalWin = board.indices.any { i -> board.all { j -> j[i] == -1 } }
        return isHorizontalWin || isVerticalWin
    }


    fun getBoardScore(board: Board, winningNum: Int): Int {
        return board.flatten().filter { it != -1 }.sum() * winningNum
    }


    fun part1(inputs: List<String>): Int {
        var (nums, boards) = parseNumbersAndBoards(inputs)
        var wonBoard: Board?
        for (num in nums) {
            boards = boards.map { markBoard(it, num) }
            wonBoard = boards.find { hasBoardWon(it) }
            if (wonBoard != null) return getBoardScore(wonBoard, num)
        }
        // invalid case, happens when there is no winner
        return -1
    }

    fun part2(inputs: List<String>): Int {
        var (nums, boards) = parseNumbersAndBoards(inputs)
        for (num in nums) {
            val newBoards = boards.map { markBoard(it, num) }
            val wonBoards = newBoards.filter { hasBoardWon(it) }
            val remainingBoards = newBoards.filter { !hasBoardWon(it) }
            if (remainingBoards.isEmpty()) {
                return getBoardScore(wonBoards.first(), num)
            }
            boards = remainingBoards
        }
        // invalid case, happens when there is no last winner
        return -1
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

