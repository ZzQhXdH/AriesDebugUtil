package data

class BaseProtocol
{
    private var mAction = 0x00
    private var mIndex = 0x00
    private val mBuffer = ByteArray(20)

    fun setAction(action: Int): BaseProtocol
    {
        mAction = action
        return this
    }

    fun append1(v: Int): BaseProtocol
    {
        mBuffer[mIndex] = (v and 0x7F).toByte()
        mIndex ++
        return this
    }

    fun append2(v: Int): BaseProtocol
    {
        mBuffer[mIndex] = (v shr 7).toByte()
        mIndex ++
        mBuffer[mIndex] = (v and 0x7F).toByte()
        mIndex ++
        return this
    }

    fun build(): ByteArray
    {
        val len = mIndex + 5
        val buffer = ByteArray(len)
        buffer[0] = 0xE1.toByte()
        buffer[1] = (len).toByte()
        buffer[2] = mAction.toByte()
        var c = 0
        for (i in 0 until mIndex) {
            c = c xor mBuffer[i].toInt()
            buffer[i + 3] = mBuffer[i]
        }
        buffer[len-2] = (c and 0xFF).toByte()
        buffer[len-1] = 0xEF.toByte()
        return buffer
    }

}