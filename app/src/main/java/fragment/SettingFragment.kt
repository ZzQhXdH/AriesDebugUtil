package fragment

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu

import com.hontech.ariesdebugutil.R
import data.BluetoothInterfaceManager
import data.GoodsTypeInfoManager
import event.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import popup.AddGoodsTypeWindow
import popup.WaitHintWindow
import util.showToast

class SettingFragment : Fragment()
{
    private lateinit var mEditTextP1: TextInputEditText
    private lateinit var mEditTextP2: TextInputEditText
    private lateinit var mButtonSetting: Button
    private lateinit var mButtonRead: Button
    private lateinit var mButtonAdd: Button
    private lateinit var mButtonCheck: Button
    private lateinit var mRecyclerView: RecyclerView
    private val mAdapter = Adapter()

    private fun initUi(view: View)
    {
        mEditTextP1 = view.findViewById(R.id.id_setting_edit_text_p1)
        mEditTextP2 = view.findViewById(R.id.id_setting_edit_text_p2)
        mButtonSetting = view.findViewById(R.id.id_setting_button_setting)
        mButtonRead = view.findViewById(R.id.id_setting_button_read)
        mRecyclerView = view.findViewById(R.id.id_setting_recycler_view)
        mButtonAdd = view.findViewById(R.id.id_setting_button_add)
        mButtonCheck = view.findViewById(R.id.id_setting_button_check)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(Decoration())
        mRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mButtonAdd.setOnClickListener(::onAddGoodsTypeClick)
        mButtonRead.setOnClickListener(::onReadGoodsTypeClick)
        mButtonSetting.setOnClickListener(::onSettingGoodsTypeClick)
        mButtonCheck.setOnClickListener(::onCheckedClick)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_setting, null)
        initUi(view)
        EventBus.getDefault().register(this)
        return view
    }

    override fun onDestroyView()
    {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }


    private fun onCheckedClick(view: View) // 校准货道
    {
        var tmp = mEditTextP1.text
        if (tmp.isEmpty()) {
            showToast("请输入位置1数据")
            return
        }
        val p1 = tmp.toString().toInt()
        tmp = mEditTextP2.text
        if (tmp.isEmpty()) {
            showToast("请输入位置2数据")
            return
        }
        val p2 = tmp.toString().toInt()
        BluetoothInterfaceManager.robotArmCheck(p1, p2)
    }

    private fun onReadGoodsTypeClick(view: View) // 读取货道
    {
        WaitHintWindow.instance.show(mRecyclerView, "开始读取货道")
        GoodsTypeInfoManager.instance.startReadGoodsType()
    }

    private fun onAddGoodsTypeClick(view: View) // 增加货道
    {
        AddGoodsTypeWindow.instance.show(view)
    }

    private fun onSettingGoodsTypeClick(view: View)
    {
        WaitHintWindow.instance.show(mRecyclerView, "开始设置货道")
        GoodsTypeInfoManager.instance.startSettingGoodsType()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onCheckedFinish(env: CheckedRobotArmFinishEvent) // 校准机械手完成
    {
        showToast("机械手校准完毕")
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED) // 货道设置完成
    fun onGoodsTypeSettingFinish(env: GoodsTypeSettingFinishEvent)
    {
        val msg = "${env.goodsTypeName()}已经设置成功"
        WaitHintWindow.instance.setHint(msg)
        if (env.goodsTypeName() == GoodsTypeInfoManager.instance.endInfo().goodsTypeName()) {
            WaitHintWindow.instance.dismiss()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED) // 货道读取结束
    fun onReadGoodsTypeFinishEvent(env: ReadGoodsTypeFinishEvent)
    {
        WaitHintWindow.instance.dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED) // 货道读取完成
    fun onGoodsTypeReadEvent(env: GoodsTypeReadEvent)
    {
        mAdapter.notifyDataSetChanged()
        val msg = "${env.row}-${env.col}读取完成"
        WaitHintWindow.instance.setHint(msg)
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED) // 货道增加或者修改完成
    fun onGoodsTypeChangeEvent(env: GoodsTypeChangeEvent)
    {
        mAdapter.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onGoodsTypeCountChangeEvent(env: GoodsTypeCountChangeEvent) // 货道数量发生改变
    {
        mAdapter.notifyDataSetChanged()
    }

    private class Item(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val mLinearLayout = itemView.findViewById<LinearLayout>(R.id.id_item_setting_linear_layout)
        private val mEditTextGoodsType = itemView.findViewById<TextInputEditText>(R.id.id_setting_item_edit_text_goods_type)
        private val mEditTextPosition = itemView.findViewById<TextInputEditText>(R.id.id_setting_item_edit_text_position)
        private val mButtonSetting = itemView.findViewById<Button>(R.id.id_setting_item_button_setting)
        private val mButtonTest = itemView.findViewById<Button>(R.id.id_setting_item_button_test)
        private val mPopupMenu = PopupMenu(itemView.context, itemView)

        init {
            val infalter = mPopupMenu.menuInflater
            infalter.inflate(R.menu.menu_item_delete, mPopupMenu.menu)
        }

        fun setUi(position: Int)
        {
            val info = GoodsTypeInfoManager.instance.getInfo(position)
            mEditTextGoodsType.setText(info.goodsTypeName())
            mEditTextPosition.setText(info.position())
            mButtonSetting.setOnClickListener {
                BluetoothInterfaceManager.setGoodsType(info.row, info.col)
                showToast("开始校准:${info.goodsTypeName()}")
            }
            mButtonTest.setOnClickListener {
                BluetoothInterfaceManager.robotArmTest( info.row, info.col)
                showToast("开始测试:${info.goodsTypeName()}")
            }
            mPopupMenu.setOnMenuItemClickListener {

                if (it.itemId == R.id.menu_delete) {
                    GoodsTypeInfoManager.instance.remove(info)
                    EventBus.getDefault().post(GoodsTypeCountChangeEvent())
                }
                return@setOnMenuItemClickListener true
            }
            mLinearLayout.setOnLongClickListener {
                mPopupMenu.menu.findItem(R.id.menu_delete).title = "删除:${info.goodsTypeName()}"
                mPopupMenu.show()
                return@setOnLongClickListener true
            }
        }
    }

    private class Decoration : RecyclerView.ItemDecoration()
    {
        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView?)
        {
            super.getItemOffsets(outRect, itemPosition, parent)
            outRect.top = 20
        }
    }

    private class Adapter : RecyclerView.Adapter<Item>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting_view, parent, false)
            return Item(view)
        }

        override fun onBindViewHolder(holder: Item, position: Int)
        {
            holder.setUi(position)
        }

        override fun getItemCount(): Int
        {
            return GoodsTypeInfoManager.instance.getInfoCount()
        }
    }

}