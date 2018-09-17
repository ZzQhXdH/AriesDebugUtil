package data

class BluetoothDeviceInfo(val name: String, val address: String, val rssi: Int)
{
    override fun equals(other: Any?): Boolean
    {
        return (other as BluetoothDeviceInfo).address == address
    }

    override fun hashCode(): Int
    {
        return address.hashCode()
    }
}