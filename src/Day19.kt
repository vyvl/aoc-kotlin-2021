import kotlin.math.abs
import kotlin.math.sign

typealias TPoint = Triple<Int, Int, Int>
typealias TPointPair = Pair<TPoint, TPoint>

class Scanner(val beacons: List<TPoint>, val origin: TPoint)


fun main() {

    fun readInputToScanners(inputs: List<String>): List<Scanner> {
        var remainingInputs = inputs
        val scanners = mutableListOf<Scanner>()
        while (remainingInputs.isNotEmpty()) {
            val scannerBeacons =
                remainingInputs.takeWhile { it.trim().isNotEmpty() }.drop(1).map { it.split(",").map { it.toInt() } }
                    .map { Triple(it[0], it[1], it[2]) }
            scanners.add(Scanner(scannerBeacons, TPoint(0, 0, 0)))
            remainingInputs = remainingInputs.drop(scannerBeacons.size + 2)
        }
        return scanners
    }

    // hardcode all rotations
    fun getAxis(): List<Triple<Int, Int, Int>> {
        return listOf(
            Triple(1, 2, 3),
            Triple(3, 2, -1),
            Triple(3, -1, -2),
            Triple(3, -2, 1),
            Triple(-2, -3, 1),
            Triple(-2, 1, 3),
            Triple(2, 3, 1),
            Triple(2, -3, -1),
            Triple(2, -1, 3),
            Triple(-1, 2, -3),
            Triple(1, -3, 2),
            Triple(-3, 2, 1),
            Triple(-1, -2, 3),
            Triple(-1, -3, -2),
            Triple(-2, 3, -1),
            Triple(1, 3, -2),
            Triple(3, 1, 2),
            Triple(-3, -1, 2),
            Triple(-3, -2, -1),
            Triple(-1, 3, 2),
            Triple(-2, -1, -3),
            Triple(1, -2, -3),
            Triple(2, 1, -3),
            Triple(-3, 1, -2)
        )
    }

    fun distance(p1: TPoint, p2: TPoint): Int {
        val dx = abs(p1.first - p2.first)
        val dy = abs(p1.second - p2.second)
        val dz = abs(p1.third - p2.third)
        return dx * dx + dy * dy + dz * dz
    }

    fun manhattanDistance(p1: TPoint, p2: TPoint): Int {
        return abs(p1.first - p2.first) + abs(p1.second - p2.second) + abs(p1.third - p2.third)
    }


    fun rotatePoint(point: TPoint, axis: Triple<Int, Int, Int>): TPoint {
        val coords = listOf(point.first, point.second, point.third)
        val xCoord = coords[abs(axis.first) - 1] * axis.first.sign
        val yCoord = coords[abs(axis.second) - 1] * axis.second.sign
        val zCoord = coords[abs(axis.third) - 1] * axis.third.sign
        return TPoint(xCoord, yCoord, zCoord)
    }

    //point1 is zeroth origin point
    fun findOrigin(point1: TPoint, point2: TPoint): TPoint {
        val dx = point2.first - point1.first
        val dy = point2.second - point1.second
        val dz = point2.third - point1.third
        return TPoint(dx, dy, dz)
    }

    fun transformToZeroOrigin(point: TPoint, axis: Triple<Int, Int, Int>, origin: TPoint): TPoint {
        val rotatedPoint = rotatePoint(point, axis)
        val x = rotatedPoint.first - origin.first
        val y = rotatedPoint.second - origin.second
        val z = rotatedPoint.third - origin.third
        return TPoint(x, y, z)
    }


    fun getMapOfDistanceAndPair(scanner: Scanner): Map<Int, TPointPair> {
        val beacons = scanner.beacons
        return beacons.pairs().associateBy { distance(it.first, it.second) }
    }

    fun getCommonPairs(map1: Map<Int, TPointPair>, map2: Map<Int, TPointPair>): List<Pair<TPointPair, TPointPair>> {

        val commonKeys = map1.keys.intersect(map2.keys)
        return commonKeys.map { map1[it]!! to map2[it]!! }
    }

    // if rotation is correct,the manhattan distance between points should be same
    fun isCorrectRotation(tp1: TPointPair, tp2: TPointPair, axis: Triple<Int, Int, Int>): Boolean {
        val (p1, p2) = tp1
        val (p3, p4) = tp2
        val rotatedP3 = rotatePoint(p3, axis)
        val rotatedP4 = rotatePoint(p4, axis)
        return manhattanDistance(p1, rotatedP3) == manhattanDistance(p2, rotatedP4)
                || manhattanDistance(p1, rotatedP4) == manhattanDistance(p2, rotatedP3)
    }


    fun findPossibleRotations(mapping: List<Pair<TPointPair, TPointPair>>): List<Triple<Int, Int, Int>> {
        return getAxis().filter { rotation -> mapping.all { isCorrectRotation(it.first, it.second, rotation) } }
    }

    fun getCommonPairsForScanner(oldScanner: Scanner, newScanner: Scanner): List<Pair<TPointPair, TPointPair>> {
        val distPair1 = getMapOfDistanceAndPair(oldScanner)
        val distPair2 = getMapOfDistanceAndPair(newScanner)
        return getCommonPairs(distPair1, distPair2)
    }

    fun tryAlignScanner(alignedScanner: Scanner, unalignedScanner: Scanner): Scanner? {
        val commonPairs = getCommonPairsForScanner(alignedScanner, unalignedScanner)
        val rotations = findPossibleRotations(commonPairs)
        for (rotation in rotations) {
            val (p1, p2) = commonPairs[0].first
            val (p3, p4) = commonPairs[0].second
            val shouldSwapPairOrder =
                manhattanDistance(p1, rotatePoint(p3, rotation)) == manhattanDistance(p2, rotatePoint(p4, rotation))
            val origin = if (shouldSwapPairOrder) findOrigin(p1, rotatePoint(p3, rotation))
            else findOrigin(p1, rotatePoint(p4, rotation))

            val newBeaconLocations = unalignedScanner.beacons.map { transformToZeroOrigin(it, rotation, origin) }
            // if atleast 12 beacons are in the same location, then we can align the unaligned scanner with aligned scanner
            if ((newBeaconLocations intersect alignedScanner.beacons.toSet()).size >= 12) return Scanner(
                newBeaconLocations,
                origin
            )
        }
        return null
    }


    fun alignScanners(inputs: List<String>): MutableList<Scanner> {
        val scanners = readInputToScanners(inputs)

        val alignedScanners = mutableListOf(scanners[0])
        val nonAlignedScanners = scanners.drop(1).toMutableList()

        while (nonAlignedScanners.isNotEmpty()) {

            // list of pairs of original scanner and its aligned transformed scanner
            val alignedScannerMapping =
                // find all pairs of aligned and non-aligned scanners
                alignedScanners.flatMap { os -> nonAlignedScanners.map { ns -> os to ns } }
                    // filter pairs which have atleast 12 common distances
                    .filter { (os, ns) -> getCommonPairsForScanner(os, ns).size >= 12 }
                    // align a non-aligned scanner with respective to already aligned scanner and return map of non-aligned scanner and value of it after aligning
                    .mapNotNull { (os, ns) -> tryAlignScanner(os, ns)?.let { ns to it } }

            alignedScannerMapping.forEach { (original, aligned) ->
                alignedScanners.add(aligned)
                nonAlignedScanners.remove(original)
            }

        }

        return alignedScanners
    }

    fun part1(inputs: List<String>): Int {
        val registeredScanners = alignScanners(inputs)
        return registeredScanners.map { it.beacons }.flatten().distinct().size

    }


    fun part2(inputs: List<String>): Int {
        val registeredScanners = alignScanners(inputs)
        val origins = registeredScanners.map { it.origin }
        return origins.pairs().maxOf { (o1, o2) -> manhattanDistance(o1, o2) }
    }


    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
