package fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.hontech.ariesdebugutil.R
import data.BluetoothInterfaceManager
import event.DeviceStateChangeEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import util.log

class DebugFragment : Fragment()
{
    private lateinit var mCheckBoxDoor: CheckBox
    private lateinit var mCheckBoxFridgeClose: CheckBox
    private lateinit var mCheckBoxFridgeOpen: CheckBox
    private lateinit var mCheckBoxPickMotor: CheckBox
    private lateinit var mCheckBoxCompressor: CheckBox

    private lateinit var mTextViewPosition1: TextView
    private lateinit var mTextViewPosition2: TextView

    private lateinit var mTextViewTemperature: TextView

    private lateinit var mEditTextFridgeSpeed: TextInputEditText
    private lateinit var mEditTextFridgeTimeOut: TextInputEditText
    private lateinit var mButtonFridgeOpen: Button
    private lateinit var mButtonFridgeClose: Button

    private lateinit var mEditTextPickMotorSpeed: TextInputEditText
    private lateinit var mEditTextPickMotorSteps: TextInputEditText
    private lateinit var mEditTextPickMotorTimeOut: TextInputEditText

    private lateinit var mButtonPickMotorUp: Button
    private lateinit var mButtonPickMotorDown: Button

    private lateinit var mEditTextPosition1: TextInputEditText
    private lateinit var mEditTextPosition2: TextInputEditText
    private lateinit var mEditTextRobotArmTimeOut: TextInputEditText

    private lateinit var mButtonSettingPosition: Button

    private lateinit var mEditTextRow: TextInputEditText
    private lateinit var mEditTextCol: TextInputEditText
    private lateinit var mEditTextDeliverTimeOut: TextInputEditText

    private lateinit var mButtonDeliver: Button

    private lateinit var mEditTextInitTimeOut: TextInputEditText
    private lateinit var mButtonInit: Button


    private fun initUi(view: View)
    {
        mCheckBoxDoor = view.findViewById(R.id.id_debug_checkbox_door)
        mCheckBoxFridgeClose = view.findViewById(R.id.id_debug_checkbox_fridge_close)
        mCheckBoxFridgeOpen = view.findViewById(R.id.id_debug_checkbox_fridge_open)
        mCheckBoxPickMotor = view.findViewById(R.id.id_debug_checkbox_pick_motor)
        mCheckBoxCompressor = view.findViewById(R.id.id_debug_checkbox_compressor)

        mTextViewPosition1 = view.findViewById(R.id.id_debug_text_view_position_1)
        mTextViewPosition2 = view.findViewById(R.id.id_debug_text_view_position_2)

        mTextViewTemperature = view.findViewById(R.id.id_debug_text_view_temperature)

        mEditTextFridgeSpeed = view.findViewById(R.id.id_debug_edit_text_fridge_speed)
        mEditTextFridgeTimeOut = view.findViewById(R.id.id_debug_edit_text_fridge_time_out)
        mButtonFridgeClose = view.findViewById(R.id.id_debug_button_fridge_close)
        mButtonFridgeOpen = view.findViewById(R.id.id_debug_button_fridge_open)

        mEditTextPickMotorSpeed = view.findViewById(R.id.id_debug_edit_text_pick_motor_speed)
        mEditTextPickMotorSteps = view.findViewById(R.id.id_debug_edit_text_pick_motor_steps)
        mEditTextPickMotorTimeOut = view.findViewById(R.id.id_debug_edit_text_pick_motor_time_out)

        mButtonPickMotorUp = view.findViewById(R.id.id_debug_button_pick_motor_up)
        mButtonPickMotorDown = view.findViewById(R.id.id_debug_button_pick_motor_down)

        mEditTextPosition1 = view.findViewById(R.id.id_debug_edit_text_position_1)
        mEditTextPosition2 = view.findViewById(R.id.id_debug_edit_text_position_2)
        mEditTextRobotArmTimeOut = view.findViewById(R.id.id_debug_edit_text_position_time_out)

        mButtonSettingPosition = view.findViewById(R.id.id_debug_button_robot_arm_setting)

        mEditTextRow = view.findViewById(R.id.id_debug_edit_text_row)
        mEditTextCol = view.findViewById(R.id.id_debug_edit_text_col)

        mEditTextDeliverTimeOut = view.findViewById(R.id.id_debug_edit_text_deliver_time_out)

        mButtonDeliver = view.findViewById(R.id.id_debug_button_deliver)

        mEditTextInitTimeOut = view.findViewById(R.id.id_debug_edit_text_init_time_out)
        mButtonInit = view.findViewById(R.id.id_debug_button_init)

        mButtonFridgeClose.setOnClickListener(::onFridgeClose)
        mButtonFridgeOpen.setOnClickListener(::onFridgeOpen)
        mButtonPickMotorUp.setOnClickListener(::onPickMotorUp)
        mButtonPickMotorDown.setOnClickListener(::onPickMotorDown)
        mButtonSettingPosition.setOnClickListener(::onRobotArmControl)
        mButtonDeliver.setOnClickListener(::onDeliver)
        mButtonInit.setOnClickListener(::onInit)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_debug, null)
        initUi(view)
        EventBus.getDefault().register(this)
        return view
    }

    override fun onDestroyView()
    {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onStateChangeEvent(env: DeviceStateChangeEvent)
    {
        mCheckBoxDoor.isChecked = env.isDoorClose()
        mCheckBoxFridgeOpen.isChecked = env.isFridgeOpen()
        mCheckBoxFridgeClose.isChecked = env.isFridgeClose()
        mCheckBoxPickMotor.isChecked = env.isPickMotor()
        mCheckBoxCompressor.isChecked = env.isCompressor()
        mTextViewTemperature.text = "冰箱温度:${env.temperature}℃"
        mTextViewPosition1.text = "磁编码器1:${env.position1}"
        mTextViewPosition2.text = "磁编码器2:${env.position2}"
    }

    private fun onFridgeClose(view: View)
    {
        val sp = mEditTextFridgeSpeed.text.toString().toInt()
        val timeOut = mEditTextFridgeTimeOut.text.toString().toInt()
        BluetoothInterfaceManager.fridgeClose(sp, timeOut)
    }

    private fun onFridgeOpen(view: View)
    {
        val sp = mEditTextFridgeSpeed.text.toString().toInt()
        val timeOut = mEditTextFridgeTimeOut.text.toString().toInt()
        BluetoothInterfaceManager.fridgeOpen(sp, timeOut)
    }

    private fun onPickMotorUp(view: View)
    {
        val sp = mEditTextPickMotorSpeed.text.toString().toInt()
        val timeOut = mEditTextPickMotorTimeOut.text.toString().toInt()
        val steps = mEditTextPickMotorSteps.text.toString().toInt()
        BluetoothInterfaceManager.pickMotorUp(sp, steps, timeOut)
    }

    private fun onPickMotorDown(view: View)
    {
        val sp = mEditTextPickMotorSpeed.text.toString().toInt()
        val timeOut = mEditTextPickMotorTimeOut.text.toString().toInt()
        val steps = mEditTextPickMotorSteps.text.toString().toInt()
        BluetoothInterfaceManager.pickMotorDown(sp, steps, timeOut)
    }

    private fun onRobotArmControl(view: View)
    {
        val p1 = mEditTextPosition1.text.toString().toInt()
        val p2 = mEditTextPosition2.text.toString().toInt()
        val timeOut = mEditTextRobotArmTimeOut.text.toString().toInt()
        BluetoothInterfaceManager.robotArmControl(p1, p2, timeOut)
    }

    private fun onDeliver(view: View)
    {
        val row = mEditTextRow.text.toString().toInt()
        val col = mEditTextCol.text.toString().toInt()
        val timeOut = mEditTextDeliverTimeOut.text.toString().toInt()
        BluetoothInterfaceManager.deliver(row, col, timeOut)
    }

    private fun onInit(view: View)
    {
        val timeOut = mEditTextInitTimeOut.text.toString().toInt()
        BluetoothInterfaceManager.init(timeOut)
    }

}