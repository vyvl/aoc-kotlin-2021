import kotlin.streams.toList

fun main() {

    fun readLine(input: String): List<Char> {
        return input.chars().toList().map { it.toChar() }
    }

    fun readImage(inputs: List<String>): List<List<Char>> {
        return inputs.map { readLine(it) }
    }

    fun padImage(input: List<List<Char>>, padLen: Int, padChar: Char): List<List<Char>> {
        val imageWithPaddedRows = input.map { List(padLen) { padChar } + it + List(padLen) { padChar } }
        val rowSize = imageWithPaddedRows.first().size
        val paddingLines = List(rowSize) { padChar }
        return List(padLen) { paddingLines } + imageWithPaddedRows + List(padLen) { paddingLines }
    }

    fun findImageAlgoIndex(point: Pair<Int, Int>, image: List<List<Char>>, defaultLight: Char): Int {
        val (x, y) = point
        var binaryString = ""
        for (i in x - 1..x + 1) {
            for (j in y - 1..y + 1) {
                val isOutlierPoint = i in image.indices && j in image[i].indices
                val lightOn = (isOutlierPoint && image[i][j] == '#') || (!isOutlierPoint && defaultLight == '#')
                binaryString += if(lightOn) "1" else "0"
            }
        }
        return binaryString.toInt(2)
    }

    fun enhanceImage(image: List<List<Char>>, imageAlgo: List<Char>, times: Int): List<List<Char>> {

        var enhancedImage = image
        repeat(times) {
            val defaultLight = if (imageAlgo.first() == '#' && it % 2 != 0) '#' else '.'
            val paddedImage = padImage(enhancedImage, 1, defaultLight)
            enhancedImage = paddedImage.indices.map { x ->
                paddedImage[x].indices.map { y -> imageAlgo[findImageAlgoIndex(Pair(x, y), paddedImage, defaultLight)] }
            }
        }
        return enhancedImage
    }



    fun part1(inputs: List<String>): Int {
        val imageAlgo = readLine(inputs.first())
        val image = readImage(inputs.drop(2))
        val enhancedImage = enhanceImage(image, imageAlgo, 2)
        return enhancedImage.flatten().count { it == '#' }
    }


    fun part2(inputs: List<String>): Int {
        val imageAlgo = readLine(inputs.first())
        val image = readImage(inputs.drop(2))
        val enhancedImage = enhanceImage(image, imageAlgo, 50)
        return enhancedImage.flatten().count { it == '#' }
    }


    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
