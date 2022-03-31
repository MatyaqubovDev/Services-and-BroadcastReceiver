package dev.matyaqubov.services.service

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.widget.Toast

class MyIntentService(): IntentService("my_intent_thread") {
    override fun onHandleIntent(intent: Intent?) {
        synchronized(this){
            try {
                Thread.sleep(2000)
            } catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "MyIntentService started", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "MyIntentService destroyed", Toast.LENGTH_SHORT).show()
    }
}