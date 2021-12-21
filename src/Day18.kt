import java.util.*

class SnailNumber(
    var children: Pair<SnailNumber, SnailNumber>?,
    var value: Int?,
    var parent: SnailNumber?
) {

    constructor(children: Pair<SnailNumber, SnailNumber>) : this(children, null, null)
    constructor(value: Int) : this(null, value, null)
    constructor(value: Int, parent: SnailNumber) : this(null, value, parent)

    fun getMagnitude(): Int {
        return value ?: (children?.let { (l, r) -> 3 * l.getMagnitude() + 2 * r.getMagnitude() } ?: 0)
    }

}


fun main() {

    fun convertToSnailNumber(input: String): SnailNumber {
        val snStk = Stack<SnailNumber>()

        var digitsStr = ""
        for (c in input) {
            if (c.isDigit()) {
                digitsStr += c
            } else {
                if (digitsStr.isNotEmpty()) {
                    snStk.push(SnailNumber(digitsStr.toInt()))
                    digitsStr = ""
                }
                if (c == ']') {
                    val right = snStk.pop()
                    val left = snStk.pop()
                    val snailNumber = SnailNumber(left to right)
                    left.parent = snailNumber
                    right.parent = snailNumber
                    snStk.push(snailNumber)
                }
            }
        }

        return snStk.pop()
    }

    fun addValueToRegularNumber(sn: SnailNumber, value: Int): SnailNumber {
        val snailNumber = SnailNumber(sn.value!! + value)
        snailNumber.parent = sn.parent
        return snailNumber
    }

    fun addRight(sn: SnailNumber, value: Int): SnailNumber {
        if (sn.value != null) {
            return addValueToRegularNumber(sn, value)
        }
        sn.children = sn.children!!.first to addRight(sn.children!!.second, value)
        return sn
    }

    fun addLeft(sn: SnailNumber, value: Int): SnailNumber {
        if (sn.value != null) {
            return addValueToRegularNumber(sn, value)
        }
        sn.children = addLeft(sn.children!!.first, value) to sn.children!!.second
        return sn
    }


    fun prevAdd(sn: SnailNumber, prev: SnailNumber, value: Int) {
        val (left, right) = sn.children!!
        if (left == prev && sn.parent != null) {
            prevAdd(sn.parent!!, sn, value)
        } else if (left != prev) {
            sn.children = addRight(left, value) to right
        }
    }


    fun nxtAdd(sn: SnailNumber, prev: SnailNumber, value: Int) {
        val (left, right) = sn.children!!
        if (right == prev && sn.parent != null) {
            nxtAdd(sn.parent!!, sn, value)
        } else if (right != prev) {
            sn.children = left to addLeft(right, value)
        }
    }

    fun explode(num: SnailNumber): Boolean {

        val stk = Stack<Pair<SnailNumber, Int>>()
        stk.push(Pair(num, 0))
        while (stk.isNotEmpty()) {
            val pair = stk.pop()
            val sn = pair.first
            val depth = pair.second
            if (sn.value == null && depth == 4) {
                val (left, right) = sn.children!!
                val ln = left.value!!
                val rn = right.value!!
                sn.parent?.let {
                    prevAdd(it, sn, ln)
                    nxtAdd(it, sn, rn)
                }
                sn.children = null
                sn.value = 0
                return true
            }
            sn.children?.let {
                stk.push(it.second to depth + 1)
                stk.push(it.first to depth + 1)
            }
        }

        return false

    }

    fun findNumToSplit(sn: SnailNumber): SnailNumber? {
        if (sn.value != null) return if (sn.value!! >= 10) sn else null

        val (left, right) = sn.children!!
        return findNumToSplit(left) ?: findNumToSplit(right)
    }

    fun split(sn: SnailNumber): Boolean {

        val splitNum = findNumToSplit(sn)

        if (splitNum != null) {
            val snValue = splitNum.value!!
            val leftRegularNum = snValue / 2
            val rightRegularNum = (snValue + 1) / 2
            val left = SnailNumber(leftRegularNum, splitNum)
            val right = SnailNumber(rightRegularNum, splitNum)
            splitNum.children = left to right
            splitNum.value = null
            return true
        }

        return false
    }

    fun reduce(sn: SnailNumber): SnailNumber {

        while (true) {
            if (!explode(sn) && !split(sn)) {
                return sn
            }
        }

    }

    fun add(left: SnailNumber, right: SnailNumber): SnailNumber {
        val snailNumber = SnailNumber(left to right)
        left.parent = snailNumber
        right.parent = snailNumber
        return reduce(snailNumber)
    }

    fun part1(inputs: List<String>): Int {
        val snailNumbers = inputs.map { convertToSnailNumber(it) }
        return snailNumbers.reduce { acc, snailNumber -> add(acc, snailNumber) }.getMagnitude()
    }


    fun part2(inputs: List<String>): Int {

        return inputs.indices.flatMap { i -> inputs.indices.map { j -> i to j } }
            .filter { (i, j) -> i != j }
            .maxOf { (i, j) -> add(convertToSnailNumber(inputs[i]), convertToSnailNumber(inputs[j])).getMagnitude() }

    }


    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
