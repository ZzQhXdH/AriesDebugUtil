package activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.hontech.ariesdebugutil.R
import data.BluetoothDeviceInfoManager
import event.BluetoothScanEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import popup.WaitPopupWindow
import task.ThreadManager
import util.log

class MainActivity : AppCompatActivity()
{
    companion object
    {
        private const val REQUEST_CODE = 0
    }

    private val mRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.id_main_recycler_view) }
    private val mScanButton: Button by lazy { findViewById<Button>(R.id.id_main_scan_button) }
    private val mAdapter = Adapter(::onItemClick)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ThreadManager.UiHangler
        requestPermission()
    }

    private fun requestPermission()
    {
        val ret = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (ret == PackageManager.PERMISSION_GRANTED) {
            onInit()
            return
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == REQUEST_CODE) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            onInit()
        }
    }

    private fun onInit()
    {
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.addItemDecoration(Decoration())
        BluetoothDeviceInfoManager.instance.enable()
        mScanButton.setOnClickListener(::onScanClick)
        EventBus.getDefault().register(this)
    }

    private fun onItemClick(position: Int)
    {
        BluetoothDeviceInfoManager.instance.SelectIndex = position
        val intent = Intent(this, BluetoothActivity::class.java)
        startActivity(intent)
    }

    private fun onScanClick(view: View)
    {
        WaitPopupWindow.instance.show(mRecyclerView) { BluetoothDeviceInfoManager.instance.stopScan() }
        BluetoothDeviceInfoManager.instance.startScan()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onBluetoothPeriphalScanned(env: BluetoothScanEvent)
    {
        mAdapter.notifyDataSetChanged()
    }

    override fun onDestroy()
    {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private class Item(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val mTextViewName = itemView.findViewById<TextView>(R.id.id_item_text_view_name)
        private val mTextViewAddress = itemView.findViewById<TextView>(R.id.id_item_text_view_address)
        private val mTextViewRssi = itemView.findViewById<TextView>(R.id.id_item_text_view_rssi)
        private val mLinearLayout = itemView.findViewById<LinearLayout>(R.id.id_item_linear_layout)

        fun setUi(position: Int, onItemClick: (Int) -> Unit)
        {
            val info = BluetoothDeviceInfoManager.instance.get(position)
            mTextViewAddress.text = info.address
            mTextViewName.text = info.name
            mTextViewRssi.text = "${info.rssi}dB"
            mLinearLayout.setOnClickListener { onItemClick(position) }
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

    private class Adapter(val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<Item>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
            return Item(view)
        }

        override fun onBindViewHolder(holder: Item, position: Int)
        {
            holder.setUi(position, onItemClick)
        }

        override fun getItemCount(): Int
        {
            return BluetoothDeviceInfoManager.instance.peripheralCount()
        }
    }

}