package dev.matyaqubov.services.receivers

import android.content.Context
import android.widget.Toast

class SMSBroadcastReceiver : BaseReceiver() {

    override fun onSMSReceived(context: Context?, sms: String?) {
        Toast.makeText(context, sms, Toast.LENGTH_SHORT).show()
    }

}