package popup


import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText

import android.widget.PopupWindow
import app.App
import com.hontech.ariesdebugutil.R
import data.BluetoothInterfaceManager
import util.showToast


class AddGoodsTypeWindow
{
    companion object
    {
        val instance: AddGoodsTypeWindow by lazy { AddGoodsTypeWindow() }
    }

    private val mView = LayoutInflater.from(App.AppContext).inflate(R.layout.popup_add_goods_type_view, null)
    private val mEditTextCol = mView.findViewById<EditText>(R.id.id_popup_add_edit_text_col)
    private val mEditTextRow = mView.findViewById<EditText>(R.id.id_popup_add_edit_text_row)
    private val mButtonCancel = mView.findViewById<Button>(R.id.id_popup_add_button_cancel)
    private val mButtonOk = mView.findViewById<Button>(R.id.id_popup_add_button_ok)
    private val mPopupWindow = PopupWindow(mView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)


    init {
        mPopupWindow.isOutsideTouchable = false
        mButtonCancel.setOnClickListener {
            mPopupWindow.dismiss()
        }
        mButtonOk.setOnClickListener(::onOk)
    }

    private fun onOk(view: View)
    {
        var tmp = mEditTextRow.text
        if (tmp.isEmpty()) {
            showToast("请输入行")
            return
        }
        val row = tmp.toString().toInt()
        if (row > 10 || row <= 0) {
            showToast("行不能大于10或者小于1")
        }

        tmp = mEditTextCol.text
        if (tmp.isEmpty()) {
            showToast("请输入列")
            return
        }
        val col = tmp.toString().toInt()
        if (row > 6 || row <= 0) {
            showToast("列不能大于6或者小于1")
        }

        BluetoothInterfaceManager.setGoodsType(row, col)
        showToast("$row-$col")
    }

    fun show(view: View)
    {
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }
}