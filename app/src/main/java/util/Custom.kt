package util

import android.widget.Toast
import app.App


private var mToast: Toast? = null

fun showToast(msg: String)
{
    if (mToast == null) {
        mToast = Toast.makeText(App.AppContext, msg, Toast.LENGTH_SHORT)
    } else {
        mToast!!.setText(msg)
        mToast!!.duration = Toast.LENGTH_SHORT
    }
    mToast!!.show()
}