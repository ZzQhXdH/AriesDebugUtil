package task

import android.os.Handler
import android.os.HandlerThread

object ThreadManager
{
    private val AsyncThread = HandlerThread("async")
    val AsyncHandler: Handler
    val UiHangler = Handler()

    init {
        AsyncThread.start()
        AsyncHandler = Handler(AsyncThread.looper)
    }
}