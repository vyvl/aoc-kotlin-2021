fun main() {
    fun parseCommands(inputs: List<String>): List<Pair<String, Int>> {
        return inputs.map { val splits = it.split(" "); splits[0] to splits[1].toInt() }
    }

    fun part1(inputs: List<String>): Int {
        var depth = 0
        var horizontal = 0
        val commands = parseCommands(inputs)
        for (command in commands) {
            val (cmd, value) = command
            when (cmd) {
                "down" -> depth += value
                "up" -> depth -= value
                "forward" -> horizontal += value
            }
        }
        return horizontal * depth
    }


    fun part2(inputs: List<String>): Int {
        var depth = 0
        var horizontal = 0
        var aim = 0
        val commands = parseCommands(inputs)
        for (command in commands) {
            val (cmd, value) = command
            when (cmd) {
                "down" -> aim += value
                "up" -> aim -= value
                "forward" -> {
                    horizontal += value
                    depth += value * aim
                }
            }
        }
        return horizontal * depth
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 348)
    check(part2(testInput) == 2436)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
