fun main() {


    fun part1(inputs: List<String>): Int {
        return inputs.map { it.split("|")[1].trim() }.flatMap { it.split(" ") }
            .count { setOf(2, 4, 3, 7).contains(it.length) }
    }


    fun findMapping(inputs: List<String>): Map<Set<Char>, Int> {
        val numMap = mutableMapOf<Int, Set<Char>>()
        val charSet = inputs.map { it.trim().toCharArray().toSet() }

        numMap[1] = charSet.find { it.size == 2 }!!
        numMap[4] = charSet.find { it.size == 4 }!!
        numMap[7] = charSet.find { it.size == 3 }!!
        numMap[8] = charSet.find { it.size == 7 }!!

        numMap[9] = charSet.find { it.size == 6 && (numMap[4]!! - it).isEmpty() }!!
        numMap[6] = charSet.find { it.size == 6 && (numMap[1]!! - it).isNotEmpty() }!!
        numMap[0] = charSet.find { it.size == 6 && (it != numMap[6] && it != numMap[9]) }!!
        numMap[3] = charSet.find { it.size == 5 && (numMap[1]!! - it).isEmpty() }!!
        numMap[5] = charSet.find { it.size == 5 && (it - numMap[6]!!).isEmpty() }!!
        numMap[2] = charSet.find { it.size == 5 && (it != numMap[3] && it != numMap[5]) }!!
        return numMap.entries.associate { it.value to it.key }
    }

    fun findNumber(input: String): Int {
        val (numStr, inp) = input.split("|")
        val numMap = findMapping(numStr.trim().split(" "))
        return inp.trim().split(" ").map { numMap[it.toCharArray().toSet()] }.joinToString("").toInt()
    }

    fun part2(inputs: List<String>): Int {
        return inputs.sumOf { findNumber(it) }
    }


    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
