package popup

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import app.App
import com.hontech.ariesdebugutil.R
import com.wang.avi.AVLoadingIndicatorView

class WaitHintWindow
{
    companion object
    {
        val instance: WaitHintWindow by lazy { WaitHintWindow() }
    }

    private val mView = LayoutInflater.from(App.AppContext).inflate(R.layout.popup_wait_hint_window, null)
    private val mLoading = mView.findViewById<AVLoadingIndicatorView>(R.id.id_popup_wait_hint_loading)
    private val mTextViewHint = mView.findViewById<TextView>(R.id.id_popup_wait_hint_text_view)
    private val mPopupWindow = PopupWindow(mView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true)

    init {
        mPopupWindow.isOutsideTouchable = false
    }

    fun dismiss() = mPopupWindow.dismiss()

    fun setHint(text: String)
    {
        mTextViewHint.text = text
    }

    fun show(view: View, text: String)
    {
        mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        mTextViewHint.text = text
        mLoading.show()
    }
}