package app

import android.app.Application
import android.content.Context

class App : Application()
{
    companion object
    {
        lateinit var AppContext: Context
    }

    override fun onCreate()
    {
        super.onCreate()
        AppContext = applicationContext
    }
}