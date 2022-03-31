package dev.matyaqubov.services.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast




class NotifyingDailyService :Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(pIntent: Intent?, flags: Int, startId: Int): Int {
        // TODO Auto-generated method stub
        Toast.makeText(this, "NotifyingDailyService", Toast.LENGTH_LONG).show()
        Log.i("bootbroadcastpoc", "NotifyingDailyService")
        return super.onStartCommand(pIntent, flags, startId)
    }
}