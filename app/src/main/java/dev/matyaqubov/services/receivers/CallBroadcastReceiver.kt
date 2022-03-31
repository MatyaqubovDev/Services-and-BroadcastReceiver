package dev.matyaqubov.services.receivers


import android.content.Context

import android.widget.Toast
import java.util.*

class CallBroadcastReceiver :BaseReceiver() {

    override fun onIncomingCallStarted(context: Context?, number: String?, start: Date?) {
        Toast.makeText(context, "onIncomingCallStarted", Toast.LENGTH_SHORT).show()
    }

    override fun onOutgoingCallStarted(context: Context?, number: String?, start: Date?) {
        Toast.makeText(context, "onOutgoingCallStarted", Toast.LENGTH_SHORT).show()
    }

    override fun onIncomingCallEnded(context: Context?, number: String?, start: Date?, end: Date?) {
        Toast.makeText(context, "onIncomingCallEnded", Toast.LENGTH_SHORT).show()
    }

    override fun onOutgoingCallEnded(context: Context?, number: String?, start: Date?, end: Date?) {
        Toast.makeText(context, "onOutgoingCallEnded", Toast.LENGTH_SHORT).show()
    }

    override fun onMissedCall(context: Context?, number: String?, start: Date?) {
        Toast.makeText(context, "onMissedCall", Toast.LENGTH_SHORT).show()
    }

}