package dev.matyaqubov.services.receivers

import android.content.Context
import android.widget.Toast
import dev.matyaqubov.services.service.NotifyingDailyService

import android.content.Intent
import android.util.Log


class BootCompletedIntentReceiver :BaseReceiver() {
    override fun onRebooted(context: Context?, sms: String?) {
        Log.w("boot_broadcast_poc", "starting service...");
        context!!.startService(Intent(context, NotifyingDailyService::class.java))
    }
}