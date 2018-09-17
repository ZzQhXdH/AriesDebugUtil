package event

class BluetoothConnectStateChangeEvent(val state: Int)
{
    companion object
    {
        const val STATE_CONNECTED = 0
        const val STATE_DISCONNECTED = 1
    }

    inline fun isConnected() = state == STATE_CONNECTED
}