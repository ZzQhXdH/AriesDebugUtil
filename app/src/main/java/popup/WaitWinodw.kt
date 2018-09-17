package popup

import android.content.pm.ActivityInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import app.App
import com.hontech.ariesdebugutil.R
import com.wang.avi.AVLoadingIndicatorView

class WaitWinodw
{
    companion object
    {
        val instance: WaitWinodw by lazy { WaitWinodw() }
    }

    private val mView = LayoutInflater.from(App.AppContext).inflate(R.layout.popup_wait_window, null)
    private val mLoading = mView.findViewById<AVLoadingIndicatorView>(R.id.id_popup_wait_loading)
    private val mPopupWindow = PopupWindow(mView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)


    init {
        mPopupWindow.isOutsideTouchable = false
    }

    fun dismiss()
    {
        mPopupWindow.dismiss()
    }

    fun show(view: View)
    {
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        mLoading.show()
    }
}