package dev.matyaqubov.services


import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.matyaqubov.services.databinding.ActivityMainBinding
import dev.matyaqubov.services.receivers.NetworkBroadcastReceiver
import dev.matyaqubov.services.service.BoundService
import dev.matyaqubov.services.service.MyIntentService
import dev.matyaqubov.services.service.StartedService

class MainActivity : AppCompatActivity() {
    private var phoneStatePermisson = false
    private var outgoingPermission = false
    private var recieveSMSPermission = false
    private var rebootPermission = false
    var receiver = NetworkBroadcastReceiver()
    var boundSerice: BoundService? = null
    var isBound = false
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
        binding.apply {
            bStartedStart.setOnClickListener { startStartedService() }
            bStartedStop.setOnClickListener { stopStartedService() }
            bBoundStart.setOnClickListener { startBoundService() }
            bBoundStop.setOnClickListener { stopBoundService() }
            bPrintTimeStamp.setOnClickListener {
                tvTimestamp.text = boundSerice!!.timestamp
            }
            bIntentStart.setOnClickListener {
                startIntentService()
            }
            bIntentStop.setOnClickListener {
                stopIntentService()
            }
        }
    }

    private fun requestPermission() {
        phoneStatePermisson = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
        outgoingPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.PROCESS_OUTGOING_CALLS
        ) == PackageManager.PERMISSION_GRANTED
        recieveSMSPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECEIVE_SMS
        ) == PackageManager.PERMISSION_GRANTED
        rebootPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
        ) == PackageManager.PERMISSION_GRANTED
        var permissionsToRequest = mutableListOf<String>()
        if (!phoneStatePermisson) permissionsToRequest.add(Manifest.permission.READ_PHONE_STATE)
        if (!outgoingPermission) permissionsToRequest.add(Manifest.permission.PROCESS_OUTGOING_CALLS)
        if (!recieveSMSPermission) permissionsToRequest.add(Manifest.permission.RECEIVE_SMS)
        if (!rebootPermission) permissionsToRequest.add(Manifest.permission.RECEIVE_BOOT_COMPLETED)

        if (permissionsToRequest.isNotEmpty()) permissionLauncher.launch(permissionsToRequest.toTypedArray())

    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            phoneStatePermisson =
                permissions[Manifest.permission.READ_PHONE_STATE] ?: phoneStatePermisson
            outgoingPermission =
                permissions[Manifest.permission.PROCESS_OUTGOING_CALLS] ?: outgoingPermission
            recieveSMSPermission =
                permissions[Manifest.permission.RECEIVE_SMS] ?: recieveSMSPermission
            rebootPermission =
                permissions[Manifest.permission.RECEIVE_BOOT_COMPLETED] ?: rebootPermission

        }

    override fun onStart() {
        super.onStart()

//        val filter=IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        registerReceiver(receiver,filter)
    }

    fun startStartedService() {
        startService(Intent(this, StartedService::class.java))
    }

    fun stopStartedService() {
        stopService(Intent(this, StartedService::class.java))
    }

    fun startBoundService() {
        val intent = Intent(this, BoundService::class.java)
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE)
    }

    fun stopBoundService() {
        if (isBound) {
            unbindService(mServiceConnection)
            isBound = false
        }
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val myBinder = service as BoundService.MyBinder
            boundSerice = myBinder.getService()

            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun startIntentService() {
        val intent = Intent(this, MyIntentService::class.java)
        startService(intent)
    }

    fun stopIntentService() {
        val intent = Intent(this, MyIntentService::class.java)
        stopService(intent)
    }

}