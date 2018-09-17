package data

class GoodsTypeInfo(val row: Int, val col: Int, var p1: Int, var p2: Int)
{
    companion object
    {
        fun ofString(key: String, value: String): GoodsTypeInfo
        {
            val keys = key.split("-")
            val values = value.split(",")
            val row = keys[0].toInt()
            val col = keys[1].toInt()
            val p1 = values[0].toInt()
            val p2 = values[1].toInt()
            return GoodsTypeInfo(row, col, p1, p2)
        }
    }

    fun goodsTypeName() = "$row-$col"

    fun position() = "($p1,$p2)"

    override fun toString() = "$p1,$p2"

    override fun equals(other: Any?): Boolean
    {
        return (other as GoodsTypeInfo).goodsTypeName() == goodsTypeName()
    }

    override fun hashCode(): Int
    {
        return goodsTypeName().hashCode()
    }
}