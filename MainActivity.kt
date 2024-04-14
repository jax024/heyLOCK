package com.applock.lock.apps.fingerprint.password.activity

import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.databinding.ActivityMainBinding
import com.applock.lock.apps.fingerprint.password.navigation.home.HomeFragment
import com.applock.lock.apps.fingerprint.password.receivers.AppInstallNotifyReceiver
import com.applock.lock.apps.fingerprint.password.receivers.AppOnOffNotifyReceiver
import com.applock.lock.apps.fingerprint.password.receivers.DeviceAdmin
import com.applock.lock.apps.fingerprint.password.services.AppService1
import com.applock.lock.apps.fingerprint.password.services.ScreenOnOffService
import com.applock.lock.apps.fingerprint.password.services.lollipopservice
import com.applock.lock.apps.fingerprint.password.utils.APP_NAME
import com.applock.lock.apps.fingerprint.password.utils.ConnectionDetector
import com.applock.lock.apps.fingerprint.password.utils.REQUEST_HIDE_PHOTOS
import com.applock.lock.apps.fingerprint.password.utils.REQUEST_HIDE_VIDEOS
import com.applock.lock.apps.fingerprint.password.utils.isAllPermissionsGranted
import com.applock.lock.apps.fingerprint.password.utils.putImageHideUri
import com.applock.lock.apps.fingerprint.password.utils.putVideoHideUri
import com.applock.lock.apps.fingerprint.password.utils.saveToUserDefaults
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import java.io.File
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity++++++"

    private val REQUEST_CODE_PERMISSIONS = 100




    var toolbar: MaterialToolbar? = null
    var pm: PackageManager? = null
    var PARAM_VALID_LIST = 0
    var count = 0
    var file: File? = null
    var drawerState = false
    var newContent: Fragment? = null
    var cd: ConnectionDetector? = null
    var isInternetPresent = false
    var gson: Gson = Gson()
    private var drawerLayout: DrawerLayout? = null
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var leftDrawerList: RecyclerView? = null
    private val pendingIntent: PendingIntent? = null
    private val readyToPurchase = false
    var isOnCurrent = true
    var rating_count = 0f
    var fragmentManager: FragmentManager? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()



        Handler().postDelayed({
            saveToUserDefaults(
                applicationContext,
                APP_NAME,
                applicationContext.packageName
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val model = Build.MANUFACTURER + " " + Build.MODEL
                val Sony = "Sony"
                //Log.d("model no", "" + model);
                if (model.lowercase(Locale.getDefault())
                        .contains(Sony.lowercase(Locale.getDefault()))
                ) {
                    if (needPermissionForBlocking(applicationContext)) {
                        hasPermissionToUsageAccess()
                    }
                } else {
                    hasPermissionToUsageAccess()
                }
            }
        }, 2000)
        if ("xiaomi" == Build.MANUFACTURER.lowercase())
        {
            Handler(Looper.myLooper()!!).postDelayed({
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this@MainActivity, AppService1::class.java))
                } else {
                    startService(Intent(this@MainActivity, AppService1::class.java))
                }
            }, 100)
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                startService(
                    Intent(
                        applicationContext,
                        lollipopservice::class.java
                    ).putExtra("action", "START")
                )
            }
            registerReceiver()
            startService(Intent(this@MainActivity, ScreenOnOffService::class.java))
            /*if (Utils.getIntegerFromUserDefaults(getApplicationContext(), Constant1.IS_SERVICE_FIRSTTIME) == 0) {
                Utils.saveIntegerToUserDefaults(MainActivity.this, Constant1.IS_SERVICE_FIRSTTIME, 1);
                registerReceiver();
                startService(new Intent(MainActivity.this, ScreenOnOffService.class));
            }*/
        } else {
            callAllServices()
        }
    }


    // This method checks if the device admin receiver is active
    fun isDeviceAdminActive(context: Context): Boolean {
        val devicePolicyManager =
            context.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (devicePolicyManager != null) {
            val componentName = ComponentName(context, DeviceAdmin::class.java)
            Log.d(TAG, "isDeviceAdminActive?----->"+devicePolicyManager.isAdminActive(componentName))

            return devicePolicyManager.isAdminActive(componentName)
        }
        Log.d(TAG, "isDeviceAdminActive?----->False")

        return false
    }


    // Method to request device admin activation
    fun requestDeviceAdminActivation(context: Context) {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        val componentName = ComponentName(context, DeviceAdmin::class.java)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Click on Activate button to not allow others to uninstall your application and set app as device admin.")
        context.startActivity(intent)
    }

    // Method to set uninstall block
    fun setUninstallBlock(context: Context) {
        val devicePolicyManager = context.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (devicePolicyManager != null) {
            val componentName = ComponentName(context, DeviceAdmin::class.java)
            if (!isDeviceAdminActive(context)) {
                // Request device admin activation
                requestDeviceAdminActivation(context)
            } else {
            //    requestPermissions(arrayOf(Manifest.permission.BIND_DEVICE_ADMIN), REQUEST_CODE_PERMISSIONS);
                // Set uninstall block if the device admin is active
                devicePolicyManager.setUninstallBlocked(componentName, context.packageName, true)
            }
        }
    }



    val isPermissionGranted: Boolean
        get() {
            val permission = true
            return if (Build.VERSION.SDK_INT >= 23) {
                isAllPermissionsGranted(this)
            } else { //permission is automatically granted on sdk<23 upon installation
                true
            }
        }

    private fun initView()
    {
        val b = intent.extras
        if (intent != null)
        {
            var screenOpenType = intent.getStringExtra("screen_open_type")
            if (screenOpenType == null) screenOpenType = "appLock"
        }

     //   setUninstallBlock(this@MainActivity)---Prevent Uninstall


        displayview()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS && grantResults.size == 2) {

        }
    }

    private fun hasPermissionToUsageAccess(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true
        }
        appOps.startWatchingMode(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            applicationContext.packageName,
            object : AppOpsManager.OnOpChangedListener {
                override fun onOpChanged(op: String, packageName: String) {
                    val mode = appOps.checkOpNoThrow(
                        AppOpsManager.OPSTR_GET_USAGE_STATS,
                        Process.myUid(), getPackageName()
                    )
                    if (mode != AppOpsManager.MODE_ALLOWED) {
                        return
                    }
                    appOps.stopWatchingMode(this)
                    if ("xiaomi" == Build.MANUFACTURER.lowercase()) {
                        return
                    }
                    val intent = intent
                    overridePendingTransition(0, 0)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                 /*   val i = Intent(this@MainActivity, SettingActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(i)*/
                }
            })
        requestAccessPermission()
        return false
    }
    private fun requestAccessPermission() {
        try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
            Handler(Looper.myLooper()!!).postDelayed({
                val intent11 = Intent(this@MainActivity, SettingPermissionActivity::class.java)
                intent11.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent11)
            }, 1200)
        } catch (e: Exception) {
        }
    }


    override fun onStart() {
        super.onStart()

    }

    fun displayview() {
        newContent = null
        newContent = HomeFragment()
        if (newContent != null)
        {
            fragmentManager = supportFragmentManager
            fragmentManager!!.beginTransaction()
              //  .setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up)
                .replace(R.id.content_frame, newContent!!).commitAllowingStateLoss()
        }
    }


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        return try {
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun callAllServices() {
        Handler(Looper.myLooper()!!).postDelayed({
            if (!isMyServiceRunning(
                    AppService1::class.java
                )
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this@MainActivity, AppService1::class.java))
                } else {
                    startService(Intent(this@MainActivity, AppService1::class.java))
                }
            }
        }, 100)
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            startService(
                Intent(applicationContext, lollipopservice::class.java).putExtra(
                    "action",
                    "START"
                )
            )
        }
        registerReceiver()
        if (!isMyServiceRunning(ScreenOnOffService::class.java)) {
            startService(Intent(this@MainActivity, ScreenOnOffService::class.java))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == 1) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
            if (requestCode == REQUEST_HIDE_PHOTOS && resultCode == Activity.RESULT_OK) {
                if (data!!.data != null)
                {
                    val treeUri = data.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                         /* contentResolver.takePersistableUriPermission(
                            treeUri!!,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )*/
                        putImageHideUri(treeUri.toString(), this)

                    }
                }
            }
            if (requestCode == REQUEST_HIDE_VIDEOS && resultCode == Activity.RESULT_OK)
            {
                if (data!!.data != null)
                {
                    val treeUri = data.data
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                         /* contentResolver.takePersistableUriPermission(
                            treeUri!!,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )*/
                        putVideoHideUri(treeUri.toString(), this)

                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    fun registerReceiver() {
        val appNotifyReceiver = AppInstallNotifyReceiver()
        val filter = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        filter.addAction(Intent.ACTION_PACKAGE_RESTARTED)
        filter.addAction(Intent.ACTION_PACKAGE_VERIFIED)
        filter.addAction(Intent.ACTION_PACKAGE_INSTALL)
        filter.addAction(Intent.ACTION_PACKAGE_FIRST_LAUNCH)
        filter.addAction(Intent.ACTION_DELETE)
        filter.addAction(Intent.ACTION_DEFAULT)
        filter.addDataScheme("package")
        filter.priority = 999999
       // registerReceiver(appNotifyReceiver, filter)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(appNotifyReceiver, filter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(appNotifyReceiver, filter)
        }

    }

    fun registerOnOfReceiver() {
        val localIntentFilter = IntentFilter("android.intent.action.SCREEN_ON")
        localIntentFilter.addAction("android.intent.action.SCREEN_OFF")
        val appNotifyReceiver = AppOnOffNotifyReceiver()
        registerReceiver(appNotifyReceiver, localIntentFilter)
    }

    override fun onResume() {
        try {
            StopAlarammanger(applicationContext)
        } catch (e: Exception) {
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (Settings.canDrawOverlays(this@MainActivity) && needPermissionForBlocking(
                    applicationContext
                )
            ) {
            }
        }, 2000)
        super.onResume()
    }

    override fun onDestroy() {
        try {
            startAlaramService(applicationContext)
        } catch (e: Exception) {
        }
        super.onDestroy()
    }


/*   //------forChangePassword------
 private fun changePassDialogue() {
        ChooseChangePasswordDialog(this).showDialog(object :
            AppInterface.OnChooseChangePasswordListener {
            override fun onChooseChangePasswordDone(choosePasswordType: AppEnum.ChoosePasswordType?) {
                if (choosePasswordType == AppEnum.ChoosePasswordType.PATTERN) {
                    val i = Intent(baseContext, SavePatternLockActivty::class.java)
                    startActivityForResult(i, 2)
                } else if (choosePasswordType == AppEnum.ChoosePasswordType.PIN_CODE)
                {
                    val i = Intent(baseContext, FirstActivity::class.java)
                    i.putExtra("from_main", true)
                    startActivityForResult(i, 1)
                } else if (choosePasswordType == AppEnum.ChoosePasswordType.GESTURE)
                {
                    val i = Intent(baseContext, SaveGesturePatternLock::class.java)
                    startActivityForResult(i, 4)
                }
            }

            override fun onChooseChangePasswordCancel() {}
        })
    }*/

    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 1234
        private const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
        var PENDINGINTENT: PendingIntent? = null
        private const val REQUEST_CODE = 0

        @JvmField
        var mainActivity: MainActivity? = null

        fun startAlaramService(context: Context) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val alarmIntent = Intent(context, AppService1::class.java)
                    PENDINGINTENT = PendingIntent.getService(
                        context,
                        0,
                        alarmIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                    val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val interval = 500
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = System.currentTimeMillis()
                    manager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        interval.toLong(),
                        PENDINGINTENT!!
                    )
                }
            } catch (e: Exception) {
            }
        }

        fun StopAlarammanger(context: Context) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (PENDINGINTENT != null) {
                        val manager =
                            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        manager.cancel(PENDINGINTENT!!)
                        PENDINGINTENT = null
                    }
                }
            } catch (e: Exception) {
            }
        }

        fun needPermissionForBlocking(context: Context): Boolean {
            val appOps = context
                .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow(
                "android:get_usage_stats",
                Process.myUid(), context.packageName
            )
            return mode == AppOpsManager.MODE_ALLOWED
        }

    }





}