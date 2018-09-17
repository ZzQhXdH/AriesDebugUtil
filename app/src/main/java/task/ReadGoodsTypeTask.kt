package task

import data.BluetoothInterfaceManager
import event.ReadGoodsTypeFinishEvent
import org.greenrobot.eventbus.EventBus

class ReadGoodsTypeTask(private val mInfoList: Array<String>) : Runnable
{
    private var nIndex = 0

    override fun run()
    {
        if (nIndex >= mInfoList.size) {
            EventBus.getDefault().post(ReadGoodsTypeFinishEvent())
            return
        }
        val infos = mInfoList[nIndex].split("-")
        val row = infos[0].toInt()
        val col = infos[1].toInt()
        BluetoothInterfaceManager.readGoodsType(row, col)
        nIndex ++
        ThreadManager.AsyncHandler.postDelayed(this, 100)
    }
}