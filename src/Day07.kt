import kotlin.math.abs

fun main() {


    // bruteforce
    fun part1(inputs: List<Int>): Int {
        return (inputs.minOrNull()!!..inputs.maxOrNull()!!).minOf { x -> inputs.sumOf { abs(it - x) } }
    }


    fun part2(inputs: List<Int>): Int {
        return (inputs.minOrNull()!!..inputs.maxOrNull()!!).minOf { x ->
            inputs.sumOf {
                val v = abs(it - x)
                (v * (v + 1)) / 2
            }
        }
    }

    val testInput = readInputNumbers("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInputNumbers("Day07")
    println(part1(input))
    println(part2(input))
}
