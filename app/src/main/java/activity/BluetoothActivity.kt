package activity

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.hontech.ariesdebugutil.R
import data.BluetoothDeviceInfoManager
import data.BluetoothInterfaceManager
import event.BluetoothConnectStateChangeEvent
import fragment.DebugFragment
import fragment.SettingFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import popup.WaitWinodw
import service.BluetoothService
import util.showToast

class BluetoothActivity : AppCompatActivity()
{
    private val mBottomNavigationView: BottomNavigationView by lazy { findViewById<BottomNavigationView>(R.id.id_bluetooth_bottom_navigation) }
    private val mViewPager: ViewPager by lazy { findViewById<ViewPager>(R.id.id_bluetooth_view_pager) }
    private var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        initUi()
        EventBus.getDefault().register(this)
        val intent = Intent(this, BluetoothService::class.java)
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE)
    }

    private fun initUi()
    {
        val stateArray = arrayOf( intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked) )
        val colorArray = intArrayOf( 0xFFFF4081.toInt(), Color.BLACK )
        val cl = ColorStateList(stateArray, colorArray)
        mBottomNavigationView.itemIconTintList = cl
        mBottomNavigationView.itemTextColor = cl

        mViewPager.adapter = ViewPagerAdapter(supportFragmentManager)

        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int)
            {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
            {
            }

            override fun onPageSelected(position: Int)
            {
                if (prevMenuItem != null) {
                    prevMenuItem!!.isChecked = false
                } else {
                    mBottomNavigationView.menu.getItem(0).isChecked = false
                }
                prevMenuItem = mBottomNavigationView.menu.getItem(position)
                prevMenuItem!!.isChecked = true
            }
        })

        mBottomNavigationView.setOnNavigationItemSelectedListener {

            when (it.itemId)
            {
                R.id.menu_debug -> mViewPager.currentItem = 0
                R.id.menu_setting -> mViewPager.currentItem = 1
            }
            true
        }
    }

    override fun onDestroy()
    {
        unbindService(mServiceConnection)
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onBluetoothConnectStateChangeEvent(env: BluetoothConnectStateChangeEvent)
    {
        if (env.isConnected()) {
            showToast("蓝牙已经成连接")
            WaitWinodw.instance.dismiss()
            return
        }
        showToast("蓝牙已经断开连接,正在重新连接")
        BluetoothInterfaceManager.connect()
        WaitWinodw.instance.show(mBottomNavigationView)
    }

    private val mServiceConnection = object: ServiceConnection
    {
        override fun onServiceDisconnected(name: ComponentName)
        {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder)
        {
            BluetoothInterfaceManager.bleInterface = (service as BluetoothService.BluetoothBinder).getInterface()
            BluetoothInterfaceManager.connect()
            WaitWinodw.instance.show(mBottomNavigationView)
        }
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager)
    {
        private val mFragments = arrayOf(DebugFragment(), SettingFragment())

        override fun getItem(position: Int): Fragment
        {
            return mFragments[position]
        }

        override fun getCount(): Int
        {
            return mFragments.size
        }
    }
}