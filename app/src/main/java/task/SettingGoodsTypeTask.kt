package task

import data.BluetoothInterfaceManager
import data.GoodsTypeInfo

class SettingGoodsTypeTask(private val mInfoList: ArrayList<GoodsTypeInfo>) : Runnable
{
    private var nIndex = 0

    override fun run()
    {
        if (nIndex >= mInfoList.size) {
            return
        }
        val info = mInfoList[nIndex]
        BluetoothInterfaceManager.settingGoodsType(info.row, info.col, info.p1, info.p2)
        nIndex ++
        ThreadManager.AsyncHandler.postDelayed(this, 100)
    }
}