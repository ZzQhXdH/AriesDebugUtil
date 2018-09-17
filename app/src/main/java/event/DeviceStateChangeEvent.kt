package event

import util.arg

class DeviceStateChangeEvent(private val byteArray: ByteArray)
{
    val temperature: String
    val position1: Int
    val position2: Int
    val state: Int

    init {
        val t = (byteArray.arg(1) shl 7) + byteArray.arg(2)
        temperature = if ((t and 0x800) != 0x00) {
            val p = 0x1000 - (t and 0xFFF)
            "-${p * 0.0625}"
        } else {
            "${t * 0.0625}"
        }
        position1 = (byteArray.arg(3) shl 7) + byteArray.arg(4)
        position2 = (byteArray.arg(5) shl 7) + byteArray.arg(6)
        state = byteArray.arg(7)
    }

    inline fun isDoorClose() = (state and 0x20) != 0x00

    inline fun isFridgeClose() = (state and 0x04) == 0x00

    inline fun isFridgeOpen() = (state and 0x02) == 0x00

    inline fun isPickMotor() = (state and 0x10) == 0x00

    inline fun isCompressor() = (state and 0x40) == 0x00
}