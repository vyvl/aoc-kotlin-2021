fun main() {

    fun getFishCount(inputs: List<Int>, numOfDays: Int): Long {
        //a map of days counter as key and number of fish with that counter as value
        var state = inputs.groupBy { it }.mapValues { it.value.size.toLong() }
        for (i in 1..numOfDays) {
            state = (0..8).map {
                it to when (it) {
                    // num of fish with 8 days counter will be equal to number of fish with 0 counter the prev day
                    8 -> {
                        state.getOrDefault(0, 0)
                    }
                    // num of 6 day fish will be both fish which reset from 0 and which decrease from 7 day
                    6 -> {
                        state.getOrDefault(7, 0) + state.getOrDefault(0, 0)
                    }
                    else -> {
                        state.getOrDefault(it + 1, 0)
                    }
                }
            }.toMap()
        }
        return state.values.sum()
    }

    fun part1(inputs: List<Int>): Long {

        return getFishCount(inputs, 80)
    }


    fun part2(inputs: List<Int>): Long {
        return getFishCount(inputs, 256)
    }

    val testInput = readInputNumbers("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539)

    val input = readInputNumbers("Day06")
    println(part1(input))
    println(part2(input))
}
