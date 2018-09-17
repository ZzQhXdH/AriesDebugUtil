package data

import android.content.Context
import app.App
import task.ReadGoodsTypeTask
import task.SettingGoodsTypeTask
import task.ThreadManager

class GoodsTypeInfoManager
{
    companion object
    {
        val instance: GoodsTypeInfoManager by lazy { GoodsTypeInfoManager() }
    }

    private val LIST = arrayOf(
            "1-1", "1-2", "1-3", "1-4", "1-5", "1-6", "1-7", "1-8", "1-9", "1-10",
            "2-1", "2-2", "2-3", "2-4", "2-5", "2-6", "2-7", "2-8", "2-9", "2-10",
            "3-1", "3-2", "3-3", "3-4", "3-5", "3-6", "3-7", "3-8", "3-9", "3-10",
            "4-1", "4-2", "4-3", "4-4", "4-5", "4-6", "4-7", "4-8", "4-9", "4-10",
            "5-1", "5-2", "5-3", "5-4", "5-5", "5-6", "5-7", "5-8", "5-9", "5-10",
            "6-1", "6-2", "6-3", "6-4", "6-5", "6-6", "6-7", "6-8", "6-9", "6-10"
    )

    private val mInfoList = ArrayList<GoodsTypeInfo>()

    private val SHARED_NAME = "aries"

    private val mSharedPreferences = App.AppContext.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)

    init {
        LIST.forEach {
            val info = mSharedPreferences.getString(it, "")
            if (!info.isEmpty()) {
                mInfoList.add( GoodsTypeInfo.ofString(it, info) )
            }
        }
    }

    fun remove(info: GoodsTypeInfo)
    {
        mInfoList.remove(info)
        mSharedPreferences.edit().remove(info.goodsTypeName()).apply()
    }

    fun getInfo(index: Int) = mInfoList[index]

    fun addInfo(info: GoodsTypeInfo) = mInfoList.add(info)

    fun endInfo() = mInfoList[mInfoList.size-1]

    fun startReadGoodsType()
    {
        ThreadManager.AsyncHandler.post(ReadGoodsTypeTask(LIST))
    }

    fun startSettingGoodsType()
    {
        ThreadManager.AsyncHandler.post(SettingGoodsTypeTask(mInfoList))
    }

    fun getInfoForGoodsType(info: String): GoodsTypeInfo?
    {
        for (g in mInfoList)
        {
            if (g.goodsTypeName() == info) {
                return g
            }
        }
        return null
    }

    fun getInfoCount() = mInfoList.size

    fun save(info: GoodsTypeInfo) = mSharedPreferences.edit().putString(info.goodsTypeName(), info.toString()).apply()

}













