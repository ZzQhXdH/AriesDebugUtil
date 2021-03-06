package event

import data.GoodsTypeInfo
import data.GoodsTypeInfoManager
import util.arg

class GoodsTypeChangeEvent(byteArray: ByteArray)
{
    val row = byteArray.arg(1)
    val col = byteArray.arg(2)
    val p1 = (byteArray.arg(3) shl 7 ) + byteArray.arg(4)
    val p2 = (byteArray.arg(5) shl 7) + byteArray.arg(6)

    init {
        val info = GoodsTypeInfoManager.instance
                .getInfoForGoodsType("$row-$col")
        if (info != null) {
            info.p1 = p1
            info.p2 = p2
            GoodsTypeInfoManager.instance.save(info)
        } else {
            val goodsType = GoodsTypeInfo(row, col, p1, p2)
            GoodsTypeInfoManager.instance.addInfo(goodsType)
            GoodsTypeInfoManager.instance.save(goodsType)
        }
    }
}