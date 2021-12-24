class DeterministicDice(private val sides: Int) {

    var totalRolls = 0
    fun roll(times: Int): Int {
        var sum = 0
        repeat(times) {
            totalRolls++
            sum += ((totalRolls - 1) % sides) + 1
        }
        return sum
    }
}

data class Player(val position: Int, val totalScore: Int = 0) {
    fun move(score: Int): Player {
        val tmpPos = (position + score - 1) % 10 + 1
        val tmpScore = totalScore + tmpPos
        return Player(tmpPos, tmpScore)
    }
}


fun main() {

    fun part1(p1: Int, p2: Int): Int {
        var player1 = Player(p1)
        var player2 = Player(p2)
        val dice = DeterministicDice(100)
        while (player1.totalScore < 1000 && player2.totalScore < 1000) {
            player1 = player1.move(dice.roll(3))
            if (player1.totalScore < 1000) player2 = player2.move(dice.roll(3))
        }
        return minOf(player1.totalScore, player2.totalScore) * dice.totalRolls
    }


    fun part2(p1: Int, p2: Int): Long {
        val player1 = Player(p1)
        val player2 = Player(p2)
        var unfinished = mapOf(Pair(player1, player2) to 1L)
        var finished = mapOf<Pair<Player, Player>, Long>()
        var rotate = 0
        while (unfinished.isNotEmpty()) {

            val playersEntries = unfinished.flatMap { (p, c) ->
                val nums =
                    (1..3).flatMap { a -> (1..3).map { b -> a + b } }.flatMap { a -> (1..3).map { b -> a + b } }
                nums.map {
                    (if (rotate % 2 == 0) (p.first.move(it) to p.second.copy()) else (p.first.copy() to p.second.move(it))) to c
                }

            }
            unfinished = playersEntries.filter { it.first.first.totalScore < 21 && it.first.second.totalScore < 21 }.groupBy { it.first }.mapValues { it.value.sumOf { it.second } }
            finished = (finished.toList() + playersEntries.filter { it.first.first.totalScore >= 21 || it.first.second.totalScore >= 21 }).groupBy { it.first }.mapValues { it.value.sumOf { it.second } }
            rotate++
        }
        val player1Wins = finished.filterKeys { it.first.totalScore >= 21 }.values.sum()
        val player2Wins = finished.filterKeys { it.second.totalScore >= 21 }.values.sum()
        return maxOf(player1Wins, player2Wins)
    }

    check(part1(4,8) == 739785)
    check(part2(4,8) == 444356092776315)

}
