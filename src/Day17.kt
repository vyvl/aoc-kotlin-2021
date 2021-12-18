fun main() {

    fun parseInput(input: String): Pair<IntRange, IntRange> {
        val (x1, x2, y1, y2) = """target area: x=(\S+)\.\.(\S+), y=(\S+)\.\.(\S+)""".toRegex().find(input)?.destructured
            ?: throw IllegalArgumentException("Invalid input")
        return Pair(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt())
    }

    fun canReachArea(curr: Pair<Int, Int>, velocity: Pair<Int, Int>, area: Pair<IntRange, IntRange>): Boolean {
        val (x, y) = curr
        val (vx, vy) = velocity
        return !((vx == 0 && x !in area.first) || (vx > 0 && x >= area.first.last) || (vx < 0 && x <= area.first.first) || (vy < 0 && y <= area.second.first))
    }

    fun isPointInArea(
        point: Pair<Int, Int>, area: Pair<IntRange, IntRange>
    ) = !(point.first in area.first && point.second in area.second)

    // return empty list if trajectory cannot reach area else return all points till area is reached
    fun getTrajectory(
        curr: Pair<Int, Int>, velocity: Pair<Int, Int>, area: Pair<IntRange, IntRange>
    ): List<Pair<Int, Int>> {
        var currPosition = curr
        var currVelocity = velocity
        val positions = mutableListOf<Pair<Int, Int>>()
        while (isPointInArea(currPosition, area)) {
            if (!canReachArea(currPosition, currVelocity, area)) {
                return emptyList()
            }
            val (vx, vy) = currVelocity
            currPosition = currPosition.first + currVelocity.first to currPosition.second + currVelocity.second
            currVelocity = (if (vx == 0) vx else if (vx > 0) vx - 1 else vx + 1) to vy - 1
            positions.add(currPosition)

        }
        return positions
    }


    fun part1(input: String): Int {
        val (xRange, yRange) = parseInput(input)
        val vxRange = 1..xRange.last
        val vyRange = 1..xRange.last
        var ans = 0

        for (vx in vxRange) {
            for (vy in vyRange) {
                val velocity = vx to vy
                val steps = getTrajectory(0 to 0, velocity, xRange to yRange)
                if (steps.isNotEmpty()) {
                    ans = maxOf(ans, steps.maxOf { it.second })
                }
            }
        }
        return ans

    }


    fun part2(input: String): Int {
        val (xRange, yRange) = parseInput(input)
        val vxRange = minOf(1, xRange.first)..xRange.last
        val vyRange = yRange.first..xRange.last
        var ans = 0

        for (vx in vxRange) {
            for (vy in vyRange) {

                val velocity = vx to vy
                val steps = getTrajectory(0 to 0, velocity, xRange to yRange)
                if (steps.isNotEmpty()) {
                    ans++
                }
            }
        }
        return ans
    }

    check(part1("target area: x=20..30, y=-10..-5") == 45)
    check(part2("target area: x=20..30, y=-10..-5") == 112)

    val input = readInputStr("Day17")
    println(part1(input))
    println(part2(input))
}
