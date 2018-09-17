package data

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import app.App
import event.BluetoothScanEvent
import org.greenrobot.eventbus.EventBus
import util.log

class BluetoothDeviceInfoManager : ScanCallback()
{
    companion object
    {
        val instance: BluetoothDeviceInfoManager by lazy { BluetoothDeviceInfoManager() }
    }

    private val mBluetoothManager = App.AppContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val mAdapter = mBluetoothManager.adapter
    private val mScanner = mAdapter.bluetoothLeScanner
    private val mPeripheralDeviceList = ArrayList<BluetoothDeviceInfo>()
    var SelectIndex = -1

    fun selectorPeripheral() = mPeripheralDeviceList[SelectIndex]

    fun peripheralCount() = mPeripheralDeviceList.count()

    fun get(index: Int) = mPeripheralDeviceList[index]

    fun enable()
    {
        if (mAdapter.isEnabled) {
            return
        }
        mAdapter.enable()
    }

    fun startScan()
    {
        mPeripheralDeviceList.clear()
        mScanner.startScan(this)
    }

    fun stopScan()
    {
        mScanner.stopScan(this)
    }

    override fun onScanResult(callbackType: Int, result: ScanResult)
    {
        val device = result.device
        val name = device.name ?: "未知设备"
        val address = device.address
        val rssi = result.rssi
        val info = BluetoothDeviceInfo(name, address, rssi)
        if (mPeripheralDeviceList.contains(info)) {
            return
        }
        mPeripheralDeviceList.add(info)
        EventBus.getDefault().post(BluetoothScanEvent())
    }

}