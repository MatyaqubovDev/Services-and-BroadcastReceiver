package dev.matyaqubov.services.receivers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import dev.matyaqubov.services.service.NotifyingDailyService


class BootCompletedIntentReceiver : BaseReceiver() {
    override fun onRebooted(context: Context?, sms: String?) {
        Log.w("boot_broadcast_poc", "starting service...");
        context!!.startService(Intent(context, NotifyingDailyService::class.java))
        Toast.makeText(context, sms, Toast.LENGTH_SHORT).show()
    }
}