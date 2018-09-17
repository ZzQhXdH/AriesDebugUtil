package data

import task.BluetoothReceiverTask
import task.ThreadManager
import util.log
import util.toHexString

object BluetoothReceiverManager
{
    private val mReceiverBuffer = ByteArray(64)
    private var mIndex = 0
    private val mHeader = 0xE1.toByte()
    private val mEnd = 0xEF.toByte()

    fun put(byteArray: ByteArray)
    {
        log(byteArray.toHexString(), "蓝牙接收")
        if ((mIndex + byteArray.size) >= mReceiverBuffer.size) {
            mIndex = 0
        }

        System.arraycopy(byteArray, 0, mReceiverBuffer, mIndex, byteArray.size)
        mIndex += byteArray.size
        parse()
    }

    private fun parse()
    {
        var headerIndex = -1
        var endIndex = -1

        for (i in 0 until mIndex)
        {
            if (mReceiverBuffer[i] == mHeader)
            {
                headerIndex = i
            }

            if (mReceiverBuffer[i] == mEnd)
            {
                endIndex = i
                if (headerIndex == -1)
                {
                    log("数据错误-----")
                    return
                }
                break
            }
        }

        val len = endIndex - headerIndex + 1

        if (len >= 6)
        {
            val buffer = ByteArray(len)
            System.arraycopy(mReceiverBuffer, headerIndex, buffer, 0, len)
            ThreadManager.AsyncHandler.post(BluetoothReceiverTask(buffer))

            if (((endIndex + 1) == mIndex))
            {
                mIndex = 0
                return
            }
            System.arraycopy(mReceiverBuffer, endIndex + 1, mReceiverBuffer, 0, mIndex - endIndex -1)
            mIndex -= (endIndex + 1)
        }

    }

}