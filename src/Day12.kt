typealias AdjacencyList = Map<String, List<String>>

fun main() {

    fun generateAdjacencyList(inputs: List<String>): AdjacencyList {
        val adjacencyList = mutableMapOf<String, List<String>>()
        inputs.forEach {
            val (from, to) = it.split("-")
            val fromNode = from.trim()
            val toNode = to.trim()
            adjacencyList[fromNode] = adjacencyList.getOrDefault(fromNode, mutableListOf()) + toNode
            adjacencyList[toNode] = adjacencyList.getOrDefault(toNode, mutableListOf()) + fromNode
        }
        return adjacencyList
    }


    fun dfsPaths(
        adjacencyList: AdjacencyList,
        currCave: String,
        visitedCaves: Map<String, Int>,
        canVisit: (Map<String, Int>, String) -> Boolean
    ): List<List<String>> {


        if (currCave == "end") return listOf(listOf(currCave))

        val newVisitedCaves = if (currCave.all { it.isUpperCase() }) {
            visitedCaves
        } else {
            visitedCaves + (currCave to (visitedCaves[currCave] ?: 0) + 1)
        }

        return adjacencyList[currCave]!!.filter { canVisit(newVisitedCaves, it) }.flatMap { nextCave ->
            dfsPaths(
                adjacencyList, nextCave, newVisitedCaves, canVisit
            )
        }.map { listOf(currCave) + it }

    }

    fun part1(inputs: List<String>): Int {
        val canVisit = { visitedCaves: Map<String, Int>, cave: String -> !visitedCaves.containsKey(cave) }
        val adjacencyList = generateAdjacencyList(inputs)
        val paths = dfsPaths(adjacencyList, "start", mapOf(), canVisit)
        return paths.size
    }


    fun part2(inputs: List<String>): Int {
        val canVisit = { visitedCaves: Map<String, Int>, cave: String ->
            !visitedCaves.containsKey(cave) || (cave != "start" && visitedCaves[cave] == 1 && visitedCaves.values.none { it == 2 })
        }

        val adjacencyList = generateAdjacencyList(inputs)
        val paths = dfsPaths(adjacencyList, "start", mapOf(), canVisit)
        return paths.size
    }


    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
