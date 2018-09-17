package task

import event.*
import org.greenrobot.eventbus.EventBus
import util.action
import util.isCorrect
import util.log
import util.toHexString

class BluetoothReceiverTask(private val byteArray: ByteArray) : Runnable
{
    companion object
    {
        const val ACTION_STATE = 0x88
        const val ACTION_GOODS_TYPE_DATA = 0x87
        const val ACTION_READ_GOODS_TYPE_DATA = 0x8B
        const val ACTION_SETTING_GOODS_TYPE_DATA = 0x8C
        const val ACTION_CHECKED_ROBOT_ARM = 0x81
    }

    override fun run()
    {
        log(byteArray.toHexString())

        if (!byteArray.isCorrect()) {
            return
        }

        val action = byteArray.action()
        when (action)
        {
            ACTION_CHECKED_ROBOT_ARM -> {
                EventBus.getDefault().post(CheckedRobotArmFinishEvent())
            }
            ACTION_STATE -> {
                EventBus.getDefault().post(DeviceStateChangeEvent(byteArray))
            }
            ACTION_GOODS_TYPE_DATA -> {
                EventBus.getDefault().post(GoodsTypeChangeEvent(byteArray))
            }
            ACTION_READ_GOODS_TYPE_DATA -> {
                EventBus.getDefault().post(GoodsTypeReadEvent(byteArray))
            }
            ACTION_SETTING_GOODS_TYPE_DATA -> {
                EventBus.getDefault().post(GoodsTypeSettingFinishEvent(byteArray))
            }
        }
    }
}