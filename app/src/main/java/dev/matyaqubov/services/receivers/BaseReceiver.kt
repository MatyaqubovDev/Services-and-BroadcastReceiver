package dev.matyaqubov.services.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


abstract class BaseReceiver : BroadcastReceiver() {
    private val TAG: String = BaseReceiver::class.java.getSimpleName()
    val pdu_type = "pdus"
    override fun onReceive(context: Context?, intent: Intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        when(intent.action){
            "android.intent.action.NEW_OUTGOING_CALL" ->{
                savedNumber = intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")
            }
            "android.intent.action.PHONE_STATE" ->{
                val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
                val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                var state = 0
                if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                    state = TelephonyManager.CALL_STATE_IDLE
                } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK
                } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                    state = TelephonyManager.CALL_STATE_RINGING
                }
                onCallStateChanged(context, state, number)
            }
            "android.provider.Telephony.SMS_RECEIVED" ->{
                // Get the SMS message.
                // Get the SMS message.
                val bundle: Bundle = intent?.getExtras()!!
                val msgs: Array<SmsMessage?>
                var strMessage = ""
                val format = bundle.getString("format")
                // Retrieve the SMS message received.
                // Retrieve the SMS message received.
                val pdus = bundle[pdu_type] as Array<Any>?
                if (pdus != null) {
                    // Check the Android version.
                    val isVersionM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    // Fill the msgs array.
                    msgs = arrayOfNulls(pdus.size)
                    for (i in msgs.indices) {
                        // Check Android version and use appropriate createFromPdu.
                        if (isVersionM) {
                            // If Android version M or newer:
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                            }
                        } else {
                            // If Android version L or older:
                            msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        }
                        // Build the message to show.
                        strMessage += "SMS from " + msgs[i]?.getOriginatingAddress()
                        strMessage += """ :${msgs[i]?.getMessageBody()}
"""
                        // Log and display the SMS message.
                        Log.d(TAG, "onReceive: $strMessage")
                        //Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show()
                        onSMSReceived(context,strMessage)
                    }
                }
            }
            Intent.ACTION_BOOT_COMPLETED->{
                onRebooted(context,"Bu dastur o'chib yondiku a ?")
            }
        }


    }

    //Derived classes should override these to respond to specific events of interest
    protected open fun onIncomingCallStarted(ctx: Context?, number: String?, start: Date?) {}
    protected open fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {}
    protected open fun onSMSReceived(ctx: Context?, sms:String?) {}
    protected open fun onRebooted(ctx: Context?, sms:String?) {}
    protected open fun onIncomingCallEnded(
        ctx: Context?,
        number: String?,
        start: Date?,
        end: Date?
    ) {
    }

    protected open fun onOutgoingCallEnded(
        ctx: Context?,
        number: String?,
        start: Date?,
        end: Date?
    ) {
    }

    protected open fun onMissedCall(ctx: Context?, number: String?, start: Date?) {}

    //Deals with actual events
    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    fun onCallStateChanged(context: Context?, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                savedNumber = number
                onIncomingCallStarted(context, number, callStartTime)
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->                 //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    onOutgoingCallStarted(context, savedNumber, callStartTime)
                }
            TelephonyManager.CALL_STATE_IDLE ->                 //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime)
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, Date())
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, Date())
                }
        }
        lastState = state
    }

    companion object {
        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date? = null
        private var isIncoming = false
        private var savedNumber: String? =
            null ////because the passed incoming is only valid in ringing
    }
}