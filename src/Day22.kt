typealias Cuboid = Triple<IntRange, IntRange, IntRange>

class RestartInstruction(input: String) {

    val isOn: Boolean
    val cuboidRange: Cuboid

    init {
        val (cmdStr, rangeStr) = input.split(" ").map { it.trim() }
        this.isOn = cmdStr == "on"
        val (x1, x2, y1, y2, z1, z2) = """x=(\S+)\.\.(\S+),y=(\S+)\.\.(\S+),z=(\S+)\.\.(\S+)""".toRegex()
            .find(rangeStr)?.destructured ?: throw IllegalArgumentException("Invalid input")
        this.cuboidRange = Triple(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt(), z1.toInt()..z2.toInt())
    }
}

fun main() {

    fun getInitializationProcedureRegionState(instruction: RestartInstruction): Map<Triple<Int, Int, Int>, Boolean> {
        val reactor = mutableMapOf<Triple<Int, Int, Int>, Boolean>()

        val (xRange, yRange, zRange) = instruction.cuboidRange
        val isOn = instruction.isOn
        val modXRange = maxOf(-50, xRange.first)..minOf(50, xRange.last)
        val modYRange = maxOf(-50, yRange.first)..minOf(50, yRange.last)
        val modZRange = maxOf(-50, zRange.first)..minOf(50, zRange.last)

        for (x in modXRange) {
            for (y in modYRange) {
                for (z in modZRange) {
                    reactor[Triple(x, y, z)] = isOn
                }
            }
        }
        return reactor
    }

    fun part1(inputs: List<String>): Int {
        val reactor = mutableMapOf<Triple<Int, Int, Int>, Boolean>()

        val instructions = inputs.map { RestartInstruction(it) }

        instructions.forEach {
            reactor += getInitializationProcedureRegionState(it).toMutableMap()
        }

        return reactor.values.count { it }

    }

    fun doesIntersect(r1: IntRange, r2: IntRange): Boolean {
        return !(r1.last < r2.first || r2.last < r1.first)
    }


    fun doesIntersect(r1: Cuboid, r2: Cuboid): Boolean {
        return doesIntersect(r1.first, r2.first) && doesIntersect(r1.second, r2.second) && doesIntersect(
            r1.third, r2.third
        )
    }


    fun intersect(r1: IntRange, r2: IntRange): IntRange {
        return maxOf(r1.first, r2.first)..minOf(r1.last, r2.last)
    }

    fun subtractRange(r1: Cuboid, r2: Cuboid): List<Cuboid> {

        if (!doesIntersect(r1, r2)) {
            return listOf(r1)
        }

        val ranges = mutableListOf<Cuboid>()
        if (r1.first.first < r2.first.first) {
            ranges.add(Triple(r1.first.first until r2.first.first, r1.second, r1.third))
        }
        if (r2.first.last < r1.first.last) {
            ranges.add(Triple((r2.first.last + 1)..r1.first.last, r1.second, r1.third))
        }
        if (r1.second.first < r2.second.first) {
            ranges.add(Triple(intersect(r1.first, r2.first), r1.second.first until r2.second.first, r1.third))
        }
        if (r2.second.last < r1.second.last) {
            ranges.add(Triple(intersect(r1.first, r2.first), (r2.second.last + 1)..r1.second.last, r1.third))
        }
        if (r1.third.first < r2.third.first) {
            ranges.add(
                Triple(
                    intersect(r1.first, r2.first), intersect(r1.second, r2.second), r1.third.first until r2.third.first
                )
            )
        }
        if (r2.third.last < r1.third.last) {
            ranges.add(
                Triple(
                    intersect(r1.first, r2.first), intersect(r1.second, r2.second), (r2.third.last + 1)..r1.third.last
                )
            )
        }
        return ranges
    }

    fun countCubesInCuboid(cuboid: Cuboid): Long {
        val (xRange, yRange, zRange) = cuboid
        return (xRange.last - xRange.first + 1).toLong() * (yRange.last - yRange.first + 1).toLong() * (zRange.last - zRange.first + 1).toLong()
    }

    fun part2(inputs: List<String>): Long {

        val instructions = inputs.map { RestartInstruction(it) }
        var ranges = listOf<Cuboid>()
        for (instruction in instructions) {
            val range = instruction.cuboidRange
            ranges = if (instruction.isOn) {
                ranges.flatMap { subtractRange(it, range) } + listOf(range)
            } else {
                ranges.flatMap { subtractRange(it, range) }
            }
        }

        return ranges.sumOf { countCubesInCuboid(it) }
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 474140)
    check(part2(testInput) == 2758514936282235)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))

}
