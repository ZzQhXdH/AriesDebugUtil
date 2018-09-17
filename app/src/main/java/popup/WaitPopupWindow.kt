package popup

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import app.App
import com.hontech.ariesdebugutil.R
import com.wang.avi.AVLoadingIndicatorView

class WaitPopupWindow
{
    companion object
    {
        val instance: WaitPopupWindow by lazy { WaitPopupWindow() }
    }

    private val mView = LayoutInflater.from(App.AppContext).inflate(R.layout.popup_wait_background, null)
    private val mLoading = mView.findViewById<AVLoadingIndicatorView>(R.id.id_popup_wait_loading)
    private val mButtonCancel = mView.findViewById<Button>(R.id.id_popup_wait_button)
    private val mPopupWindow = PopupWindow(mView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)

    init {
        mPopupWindow.isOutsideTouchable = false
        mButtonCancel.setOnClickListener { mPopupWindow.dismiss() }
    }

    fun show(view: View,  dismiss: () -> Unit)
    {
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        mPopupWindow.setOnDismissListener(dismiss)
        mLoading.show()
    }
}