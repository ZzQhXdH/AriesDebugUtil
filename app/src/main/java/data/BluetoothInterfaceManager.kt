package data

import service.BluetoothInterface


object BluetoothInterfaceManager
{
    var bleInterface: BluetoothInterface? = null

    fun connect()
    {
        val info = BluetoothDeviceInfoManager.instance.selectorPeripheral()
        bleInterface!!.connect(info.address)
    }

    fun fridgeOpen(sp: Int, timeOut: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x03)
                .append1(0) // 开冰箱门
                .append1(sp) // 速度
                .append2(timeOut) // 超时
                .build()
        bleInterface!!.write(byteArray)
    }

    fun fridgeClose(sp: Int, timeOut: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x03)
                .append1(1) // 关冰箱门
                .append1(sp) // 速度
                .append2(timeOut) // 超时
                .build()
        bleInterface!!.write(byteArray)
    }

    fun pickMotorUp(sp: Int, steps: Int, timeOut: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x04)
                .append1(0x00)
                .append1(sp)
                .append2(steps)
                .append2(timeOut)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun pickMotorDown(sp: Int, steps: Int, timeOut: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x04)
                .append1(0x01)
                .append1(sp)
                .append2(steps)
                .append2(timeOut)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun robotArmControl(p1: Int, p2: Int, timeOut: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x02)
                .append2(p1)
                .append2(p2)
                .append2(timeOut)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun deliver(row: Int, col: Int, timeOut: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x06)
                .append2(timeOut)
                .append1(row)
                .append1(col)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun init(timeOut: Int)
    {
        val byteArray = BaseProtocol().setAction(0x09).append2(timeOut).build()
        bleInterface!!.write(byteArray)
    }

    fun setGoodsType(row: Int, col: Int)
    {
        val byteArray = BaseProtocol()
                    .setAction(0x07)
                    .append1(row)
                    .append1(col)
                    .build()
        bleInterface!!.write(byteArray)
    }

    fun robotArmTest(row: Int, col: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x0A)
                .append1(row)
                .append1(col)
                .append2(10 * 1000)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun readGoodsType(row: Int, col: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x0B)
                .append1(row)
                .append1(col)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun settingGoodsType(row: Int, col: Int, p1: Int, p2: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x0C)
                .append1(row)
                .append1(col)
                .append2(p1)
                .append2(p2)
                .build()
        bleInterface!!.write(byteArray)
    }

    fun robotArmCheck(p1: Int, p2: Int)
    {
        val byteArray = BaseProtocol()
                .setAction(0x01)
                .append2(p1)
                .append2(p2)
                .build()
        bleInterface!!.write(byteArray)
    }

}