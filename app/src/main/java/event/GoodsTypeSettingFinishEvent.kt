package event

import util.arg

class GoodsTypeSettingFinishEvent(byteArray: ByteArray)
{
    val row = byteArray.arg(1)
    val col = byteArray.arg(2)

    inline fun goodsTypeName() = "$row-$col"
}