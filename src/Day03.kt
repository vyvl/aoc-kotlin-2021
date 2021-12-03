fun main() {

    /**
     * return significant bit of that position in list of strings. If both 0 and 1 bit count are equal, return default value.
     */
    fun getSignificantBit(inputs: List<String>, position: Int, defaultValue: Int = 0): Int {
        val totalInputs = inputs.size
        val countOneBits = inputs.filter { it[position] == '1' }.size
        return if (countOneBits * 2 > totalInputs) 1 else if (countOneBits * 2 == totalInputs) defaultValue else 0
    }

    fun getSignificantBits(inputs: List<String>): List<Int> {
        val inputSize = inputs[0].length
        return (0 until inputSize).map { getSignificantBit(inputs, it) }
    }


    fun part1(inputs: List<String>): Int {
        val significantDigits = getSignificantBits(inputs)
        val gamma = significantDigits.joinToString("").toInt(2)
        val epsilon = "1".repeat(significantDigits.size).toInt(2) xor gamma
        return gamma * epsilon
    }

    fun part2(inputs: List<String>): Int {
        val inputSize = inputs[0].length
        var oxygenReadings = inputs
        var co2Readings = inputs

        for (i in 0 until inputSize) {
            val significantBit = getSignificantBit(oxygenReadings, i, 1)
            oxygenReadings = oxygenReadings.filter { it[i] == significantBit.toString()[0] }
            if (oxygenReadings.size == 1) break
        }

        for (i in 0 until inputSize) {
            // find the significant bit and filter the numbers which do not have that bit
            val significantBit = getSignificantBit(co2Readings, i, 1)
            co2Readings = co2Readings.filter { it[i] != significantBit.toString()[0] }
            if (co2Readings.size == 1) break

        }

        val oxygenRating = oxygenReadings[0].toInt(2)
        val co2Rating = co2Readings[0].toInt(2)
        return oxygenRating * co2Rating
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
