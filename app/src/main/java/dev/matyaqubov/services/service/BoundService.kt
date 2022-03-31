package dev.matyaqubov.services.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.Toast

class BoundService:Service() {
    private val myBinder:IBinder=MyBinder()
    private var mChronometer:Chronometer?=null

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Bound Service Created", Toast.LENGTH_SHORT).show()
        mChronometer= Chronometer(this)
        mChronometer!!.base=SystemClock.elapsedRealtime()
        mChronometer!!.onChronometerTickListener= Chronometer.OnChronometerTickListener { chronometer ->
            Toast.makeText(application, chronometer.base.toString(), Toast.LENGTH_SHORT).show()
        }
        mChronometer!!.start()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Toast.makeText(this, "Bound Service onBind", Toast.LENGTH_SHORT).show()
        return myBinder
    }

    override fun onRebind(intent: Intent?) {
        Toast.makeText(this, "Bound Service onReBind", Toast.LENGTH_SHORT).show()
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this, "Bound Service onUnBind", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Bound Service Stopped", Toast.LENGTH_SHORT).show()
        mChronometer!!.stop()
    }




    val timestamp:String
    get() {
        val elapsedMillis=SystemClock.elapsedRealtime()-mChronometer!!.base
        val hours=(elapsedMillis/3600000).toInt()
        val minutes=(elapsedMillis-hours*3600000).toInt()/60000
        val seconds=(elapsedMillis-hours*3600000-minutes*60000).toInt()/1000
        val millis=(elapsedMillis-hours*3600000-minutes*60000-seconds*1000).toInt()
        return "$hours:$minutes:$seconds:$millis"
    }

    inner class MyBinder:Binder(){
        fun getService():BoundService=this@BoundService
    }
}