data class Packet(val value: Long, val length: Int, val version: Int, val typeId: Int, val subPackets: List<Packet>)

fun main() {

    fun inputToBinaryStr(input: String): String {
        val map = input.map { it.toString().toInt(16).toString(2).padStart(4, '0') }
        return map.joinToString("")
    }


    fun getLiteralStringValue(input: String): Pair<Long, Int> {
        val chunks = input.drop(6).chunked(5)
        val groups = chunks.takeWhile { it.first() == '1' }.map { it.drop(1) } + chunks.dropWhile { it.first() == '1' }
            .take(1).map { it.drop(1) }
        return groups.joinToString("").toLong(2) to (6 + groups.size * 5)
    }

    fun getLiteralPacket(input: String): Packet {
        val pktVersion = input.take(3).toInt(2)
        val pktTypeId = input.drop(3).take(3).toInt(2)
        val (value, len) = getLiteralStringValue(input)
        return Packet(value, len, pktVersion, pktTypeId, emptyList())
    }


    fun parsePacket(input: String): Packet {
        val pktVersion = input.take(3).toInt(2)
        val pktTypeId = input.drop(3).take(3).toInt(2)
        if (pktTypeId == 4) {
            return getLiteralPacket(input)
        }
        val lenTypeId = if (input[6] == '0') 15 else 11

        if (lenTypeId == 15) {

            val subPacketsLength = input.drop(7).take(15).toInt(2)
            var subPacketRemainingString = input.drop(7 + 15).take(subPacketsLength)
            val subPackets = mutableListOf<Packet>()
            while (subPacketRemainingString.isNotEmpty()) {
                val subPkt = parsePacket(subPacketRemainingString)
                subPackets.add(subPkt)
                subPacketRemainingString = subPacketRemainingString.drop(subPkt.length)
            }
            return Packet(0, 7 + 15 + subPacketsLength, pktVersion, pktTypeId, subPackets)

        } else {

            var remainingNumPackets = input.drop(7).take(11).toInt(2)
            var subPacketRemainingString = input.drop(7 + 11)
            val subPackets = mutableListOf<Packet>()
            while (remainingNumPackets > 0) {
                val subPkt = parsePacket(subPacketRemainingString)
                subPacketRemainingString = subPacketRemainingString.drop(subPkt.length)
                remainingNumPackets -= 1
                subPackets.add(subPkt)
            }
            return Packet(0, 7 + 11 + subPackets.sumOf { it.length }, pktVersion, pktTypeId, subPackets)
        }
    }

    fun packetVersionSum(packet: Packet): Int {
        return packet.version + packet.subPackets.sumOf { packetVersionSum(it) }
    }

    fun part1(input: String): Int {
        val binaryStr = inputToBinaryStr(input)
        return packetVersionSum(parsePacket(binaryStr))


    }

    fun solveBits(packet: Packet): Long {
        return when (packet.typeId) {
            4 -> packet.value
            0 -> packet.subPackets.map { solveBits(it) }.sum()
            1 -> packet.subPackets.map { solveBits(it) }.reduce { acc, l -> acc * l }
            2 -> packet.subPackets.map { solveBits(it) }.minOrNull() ?: 0
            3 -> packet.subPackets.map { solveBits(it) }.maxOrNull() ?: 0
            5 -> {
                val (p1, p2) = packet.subPackets.map { solveBits(it) }
                if (p1 > p2) 1 else 0
            }
            6 -> {
                val (p1, p2) = packet.subPackets.map { solveBits(it) }
                if (p1 < p2) 1 else 0
            }
            7 -> {
                val (p1, p2) = packet.subPackets.map { solveBits(it) }
                if (p1 == p2) 1 else 0
            }
            else -> throw IllegalArgumentException("Unknown typeId: ${packet.typeId}")
        }
    }


    fun part2(input: String): Long {
        val binaryStr = inputToBinaryStr(input)

        return solveBits(parsePacket(binaryStr))
    }

    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)


    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)

    val input = readInputStr("Day16")
    println(part1(input))
    println(part2(input))
}
