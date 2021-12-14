fun main() {

    fun step(pairs: Map<String, Long>, rules: Map<String, String>): Map<String, Long> {

        val result = mutableMapOf<String, Long>()

        for (pair in pairs) {
            val insert = rules[pair.key]!!
            val prefixPair = pair.key[0] + insert[0].toString()
            result[prefixPair] = result.getOrDefault(prefixPair, 0) + pair.value
            val suffixPair = insert[0].toString() + pair.key[1]
            result[suffixPair] = result.getOrDefault(suffixPair, 0) + pair.value
        }

        return result
    }

    fun transformPolymer(rules: Map<String, String>, polymer: Map<String, Long>, stepCount: Int): Map<String, Long> {
        var currentPolymer = polymer
        repeat(stepCount) {
            currentPolymer = step(currentPolymer, rules)
        }
        return currentPolymer
    }

    fun getPolymer(inputs: List<String>, stepCount: Int): Map<String, Long> {
        val rules = inputs.drop(2).map { it.split(" -> ") }.associate { it[0].trim() to it[1].trim() }
        val initialPolymer = inputs[0].windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        return transformPolymer(rules, initialPolymer, stepCount)
    }

    // we only add second char in each pair as each char is repeated twice, example NBC -> NB and BC, so B is counted twice.
    // To prevent it count second char, edge case where the very first char of input is never counted, so init map with initChar count as 1
    fun findMaxDifference(polymer: Map<String, Long>, initialChar: Char): Long {
        val charCountMap = mutableMapOf<Char, Long>()
        charCountMap[initialChar] = 1
        for (pair in polymer) {
            charCountMap[pair.key[1]] = charCountMap.getOrDefault(pair.key[1], 0) + pair.value
        }


        return charCountMap.values.maxOf { it } - charCountMap.values.minOf { it }
    }

    fun part1(inputs: List<String>): Long {
        val polymer = getPolymer(inputs, 10)

        return findMaxDifference(polymer, inputs[0][0])
    }


    fun part2(inputs: List<String>): Long {
        val polymer = getPolymer(inputs, 40)

        return findMaxDifference(polymer, inputs[0][0])
    }


    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588L)
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
