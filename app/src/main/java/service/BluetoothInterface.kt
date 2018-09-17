package service

interface BluetoothInterface
{
    fun connect(address: String)

    fun isConnected(): Boolean

    fun disconnect()

    fun write(byteArray: ByteArray)
}