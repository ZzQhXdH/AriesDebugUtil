package util

inline fun ByteArray.arg(index: Int) = this[index + 2].toInt() and 0x7F

inline fun ByteArray.action() = this[2].toInt() and 0xFF

fun ByteArray.isCorrect(): Boolean
{
    val header = this[0].toInt() and 0xFF
    val end = this[size-1].toInt() and 0xFF
    if ( (size <= 5) || (header != 0xE1) || (end != 0xEF) || (size != (this[1].toInt() and 0xFF))) {
        return false
    }
    var c = 0
    for (i in 3 until (size-2)) { c = c xor this[i].toInt() }
    return c == this[size-2].toInt()
}

fun ByteArray.toHexString(): String
{
    val sb = StringBuilder()
    forEach { sb.append(String.format("%02x ", it)) }
    return sb.toString()
}

