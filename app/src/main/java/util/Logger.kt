package util

import android.util.Log

inline fun log(msg: String, tag: String = "调试")
{
    Log.d(tag, msg)
}