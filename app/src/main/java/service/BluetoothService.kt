package service

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import data.BluetoothReceiverManager
import event.BluetoothConnectStateChangeEvent
import org.greenrobot.eventbus.EventBus
import task.BluetoothReceiverTask
import task.ThreadManager
import util.log
import util.toHexString
import java.util.*

class BluetoothService : Service(), BluetoothInterface
{
    companion object
    {
        private const val SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb"
        private const val CHARA_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb"
        private const val DES_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    }

    private lateinit var mBluetoothManager: BluetoothManager
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private var mGatt: BluetoothGatt? = null
    private var mChara: BluetoothGattCharacteristic? = null

    override fun onCreate()
    {
        super.onCreate()
        mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager.adapter
    }

    override fun onDestroy()
    {
        disconnect()
        super.onDestroy()
    }

    override fun connect(address: String)
    {
        if (mGatt != null) {
            disconnect()
        }
        val device = mBluetoothAdapter.getRemoteDevice(address)
        mGatt = device.connectGatt(this, false, mGattCallback)
    }

    override fun isConnected(): Boolean
    {
        return mGatt != null
    }

    override fun disconnect()
    {
        mGatt?.disconnect()
        mGatt?.close()
        mGatt = null
    }

    override fun write(byteArray: ByteArray)
    {
        ThreadManager.AsyncHandler.post {
            mChara!!.value = byteArray
            mGatt!!.writeCharacteristic(mChara)
        }
    }

    private val mGattCallback = object: BluetoothGattCallback()
    {
        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int)
        {
            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                return
            }

            val service = mGatt!!.getService(UUID.fromString(SERVICE_UUID))
            mChara = service.getCharacteristic(UUID.fromString(CHARA_UUID))
            val des = mChara!!.getDescriptor(UUID.fromString(DES_UUID))

            des.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            mGatt!!.writeDescriptor(des)
            mGatt!!.setCharacteristicNotification(mChara, true)
            EventBus.getDefault().post(BluetoothConnectStateChangeEvent(BluetoothConnectStateChangeEvent.STATE_CONNECTED))
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic)
        {
            val bytes = characteristic.value
            BluetoothReceiverManager.put(bytes)
            //ThreadManager.AsyncHandler.post(BluetoothReceiverTask(bytes))
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int)
        {
            if (status != BluetoothGatt.GATT_SUCCESS)
            {
                EventBus.getDefault().post(BluetoothConnectStateChangeEvent(BluetoothConnectStateChangeEvent.STATE_DISCONNECTED))
                return
            }

            when (newState)
            {
                BluetoothProfile.STATE_CONNECTED ->
                {
                    mGatt!!.discoverServices()
                }

                BluetoothProfile.STATE_DISCONNECTED ->
                {
                    EventBus.getDefault().post(BluetoothConnectStateChangeEvent(BluetoothConnectStateChangeEvent.STATE_DISCONNECTED))
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder
    {
        return BluetoothBinder()
    }

    inner class BluetoothBinder : Binder()
    {
        fun getInterface(): BluetoothInterface
        {
            return this@BluetoothService
        }
    }
}
