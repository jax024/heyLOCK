package com.applock.lock.apps.fingerprint.password.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Base64
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.utils.Utils
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.CameraFuncation
import com.applock.lock.apps.fingerprint.password.utils.ConnectionDetector
import com.applock.lock.apps.fingerprint.password.utils.DIGIT_BG
import com.applock.lock.apps.fingerprint.password.utils.PARAM_VALID_SOUND
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.ServicePreference
import com.applock.lock.apps.fingerprint.password.utils.THEME_TYPE
import com.applock.lock.apps.fingerprint.password.utils.getBitmap
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults1
import com.applock.lock.apps.fingerprint.password.utils.getThemeType
import com.applock.lock.apps.fingerprint.password.utils.hideStatusBarAndNavigationBar
import com.applock.lock.apps.fingerprint.password.utils.isAllPermissionsGranted
import com.applock.lock.apps.fingerprint.password.utils.visible
import com.applock.lock.apps.fingerprint.password.view.ForgotPasswordDialog

class NewActivity1 : Activity() {
    var passcode = ""
    var datas: String? = null
    var lout_num1: TextView? = null
    var lout_num2: TextView? = null
    var lout_num3: TextView? = null
    var lout_num4: TextView? = null
    var lout_num5: TextView? = null
    var lout_num6: TextView? = null
    var lout_num7: TextView? = null
    var lout_num8: TextView? = null
    var lout_num9: TextView? = null
    var lout_num0: TextView? = null
    var lout_clear: ImageView? = null
    var img_dot: ImageView? = null
    var img_dot1: ImageView? = null
    var img_dot2: ImageView? = null
    var img_dot3: ImageView? = null
    var img_dot4: ImageView? = null
    var lout_main_passcode: RelativeLayout? = null
    var icon: Drawable? = null
    var url = "http://developers.com/email/index.php/email/send_email"
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var isCorrectPasswordEntered = false
    var passwordTry = 0
    var count = 0
    var drawableEmpty: Drawable? = null
    var drawableFilled: Drawable? = null
    var packagename: String? = null
    var from_app = false
    private var cameraFuncation: CameraFuncation? = null
    private var surfaceView: SurfaceView? = null
    var screenOpenType: String? = null
    private fun setRootView(activity: Activity) {
        val parent = activity.findViewById<ViewGroup>(android.R.id.content)
        run {
            var i = 0
            val count = parent.childCount
            while (i < count) {
                val childView = parent.getChildAt(i)
                if (childView is ViewGroup) {
                    childView.setFitsSystemWindows(true)
                    childView.clipToPadding = true
                }
                i++
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBarAndNavigationBar(this)
        setContentView(R.layout.activity_new)
        Log.d("++++NewActivity1+++MY", "onCreate------ from NewActivity1-----")

        //Log.d("Applock while", "Lock open to password screen-->>");
        screenOpenType = "appLock"
        cd = ConnectionDetector(this@NewActivity1)
        isInternetPresent = cd!!.isConnectingToInternet
        surfaceView = findViewById(R.id.picSurfaceView)
        cameraFuncation = CameraFuncation(applicationContext, surfaceView!!)
        lout_num1 = findViewById(R.id.lout_num1)
        lout_num2 = findViewById(R.id.lout_num2)
        lout_num3 = findViewById(R.id.lout_num3)
        lout_num4 = findViewById(R.id.lout_num4)
        lout_num5 = findViewById(R.id.lout_num5)
        lout_num6 = findViewById(R.id.lout_num6)
        lout_num7 = findViewById(R.id.lout_num7)
        lout_num8 = findViewById(R.id.lout_num8)
        lout_num9 = findViewById(R.id.lout_num9)
        lout_num0 = findViewById(R.id.lout_num0)
        lout_clear = findViewById(R.id.lout_clear)
        lout_main_passcode = findViewById(R.id.lout_main_passcode)
        img_dot = findViewById(R.id.img_dot)
        img_dot1 = findViewById(R.id.img_dot1)
        img_dot2 = findViewById(R.id.img_dot2)
        img_dot3 = findViewById(R.id.img_dot3)
        img_dot4 = findViewById(R.id.img_dot4)
        val tvForgotPass = findViewById<TextView>(R.id.tvForgotPass)
      /*  val tvCancel = findViewById<TextView>(R.id.tvCancel)
        tvCancel.setOnClickListener { view: View? -> onBackPressed() }*/
        tvForgotPass.setOnClickListener { view: View? -> displaysecurityDialogue() }
        setButtonBackgrund()
        LoadBackgroundImage()
        if (intent != null) {
            screenOpenType = intent.getStringExtra("screen_open_type")
            if (screenOpenType == null) screenOpenType = "appLock"
        }
        val extras = intent.extras
        if (extras != null) {
            datas = extras.getString("Packagename")
            from_app = intent.getBooleanExtra("from_app", false)
        } else {
            datas = packageName
        }
        try {
            icon = packageManager.getApplicationIcon(datas!!)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        img_dot!!.setImageDrawable(icon)
        ButtonClick()
        tvForgotPass.visibility = View.VISIBLE
    }

    fun setButtonBackgrund() {
        //if (Utils.getThemeType(getApplicationContext(), Constant1.THEME_TYPE).equals(getPackageName())) {
        drawableEmpty = resources.getDrawable(R.drawable.icn_dot_empty)
        drawableFilled = resources.getDrawable(R.drawable.icn_dot_fill)


    }

    fun LoadBackgroundImage() {
        if (getThemeType(applicationContext, THEME_TYPE) != packageName) {
            lout_main_passcode!!.background = getBitmap(
                applicationContext,
                DIGIT_BG
            )
        } else {
            if (getBitmap(applicationContext, DIGIT_BG) == null) {
                lout_main_passcode!!.setBackgroundResource(R.drawable.applock_0)
            } else {
                lout_main_passcode!!.setBackgroundDrawable(
                    getBitmap(
                        applicationContext, DIGIT_BG
                    )
                )
            }
        }
    }

    var mp: MediaPlayer? = null
    private fun ButtonClick() {
        lout_num1!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "1"
                FeelDot(passcode)
            }
        }
        lout_num2!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "2"
                FeelDot(passcode)
            }
        }
        lout_num3!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@NewActivity1,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "3"
                FeelDot(passcode)
            }
        }
        lout_num4!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "4"
                FeelDot(passcode)
            }
        }
        lout_num5!!.setOnClickListener {
            // TODO Auto-generated method stub
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "5"
                FeelDot(passcode)
            }
        }
        lout_num6!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "6"
                FeelDot(passcode)
            }
        }
        lout_num7!!.setOnClickListener {
            // TODO Auto-generated method stub
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "7"
                FeelDot(passcode)
            }
        }
        lout_num8!!.setOnClickListener {
            // TODO Auto-generated method stub
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "8"
                FeelDot(passcode)
            }
        }
        lout_num9!!.setOnClickListener {
            // TODO Auto-generated method stub
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "9"
                FeelDot(passcode)
            }
        }
        lout_num0!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            if (passcode.length > 3) {
                // Toast.makeText(SetPasscode.this, "4 Digit Only",
                // Toast.LENGTH_LONG).show();
            } else {
                passcode = passcode + "0"
                FeelDot(passcode)
            }
        }
        lout_clear!!.setOnClickListener {
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(this@NewActivity1, PARAM_VALID_SOUND) == 1) {
                mp = MediaPlayer.create(this@NewActivity1, R.raw.click)
                mp!!.start()
            }
            backButtonClick(passcode)
        }
    }

    private fun backButtonClick(pin1: String) {
        //Log.d("clear", "" + pin1);
        if (pin1.length == 0) {
        } else if (pin1.length == 1) {
            passcode = pin1.substring(0, pin1.length - 1)
            FeelDot(passcode)
        } else if (pin1.length == 2) {
            passcode = pin1.substring(0, pin1.length - 1)
            FeelDot(passcode)
        } else if (pin1.length == 3) {
            passcode = pin1.substring(0, pin1.length - 1)
            FeelDot(passcode)
        } else if (pin1.length == 4) {
            passcode = pin1.substring(0, pin1.length - 1)
            FeelDot(passcode)
        }
    }

    private fun clearButtonClick() {
        passcode = ""
        FeelDot(passcode)
    }

    private fun FeelDot(pass: String) {
        Log.i("String Lenght", "" + pass.length)
        if (pass.length == 0) {
            try {
                img_dot1!!.setImageDrawable(drawableEmpty)
                img_dot2!!.setImageDrawable(drawableEmpty)
                img_dot3!!.setImageDrawable(drawableEmpty)
                img_dot4!!.setImageDrawable(drawableEmpty)
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 1) {
            try {
                img_dot1!!.setImageDrawable(drawableFilled)
                img_dot2!!.setImageDrawable(drawableEmpty)
                img_dot3!!.setImageDrawable(drawableEmpty)
                img_dot4!!.setImageDrawable(drawableEmpty)
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 2) {
            try {
                img_dot1!!.setImageDrawable(drawableFilled)
                img_dot2!!.setImageDrawable(drawableFilled)
                img_dot3!!.setImageDrawable(drawableEmpty)
                img_dot4!!.setImageDrawable(drawableEmpty)
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 3) {
            try {
                img_dot1!!.setImageDrawable(drawableFilled)
                img_dot2!!.setImageDrawable(drawableFilled)
                img_dot3!!.setImageDrawable(drawableFilled)
                img_dot4!!.setImageDrawable(drawableEmpty)
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 4)
        {
            try
            {
                img_dot1!!.setImageDrawable(drawableFilled)
                img_dot2!!.setImageDrawable(drawableFilled)
                img_dot3!!.setImageDrawable(drawableFilled)
                img_dot4!!.setImageDrawable(drawableFilled)
            } catch (e: Exception)
            {
                passcode = ""
                FeelDot(passcode)
            }
            if (passcode.equals(getFromUserDefaults(this, PIN), ignoreCase = true))
            {
                if (from_app)
                {
                    if (isAllPermissionsGranted(this)) {
                        val i = Intent(this@NewActivity1, MainActivity::class.java)
                        i.putExtra("screen_open_type", screenOpenType)
                        startActivityForResult(i, 58)
                    } else {
                        startActivity(
                            Intent(this@NewActivity1, AppPermissionActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        finishAffinity()
                    }
                }
                else
                {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                        val servicePreference = ServicePreference(
                            applicationContext
                        )
                        servicePreference.SetLockUnlock(true)
                        setResult(RESULT_OK)
                        finish()
                        //openApp(datas);
                    } else {
                        SavePreferences("Lock", "True")
                        isCorrectPasswordEntered = true
                        setResult(RESULT_OK)
                        finish()
                        //openApp(datas);
                    }
                }
                // unregisterReceiver(mBatInfoReceiver);
            }
            else
            {
                try {
                    val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
                    img_dot!!.startAnimation(shake)
                    findViewById<View>(R.id.shakelayout).startAnimation(shake)
                    if (getpreferences("Vibrate").equals("Yes", ignoreCase = true)) {
                        val v = this.applicationContext
                            .getSystemService(VIBRATOR_SERVICE) as Vibrator
                        v.vibrate(200)
                    }
                    passcode = ""
                    FeelDot(passcode)
                    passwordTry = passwordTry + 1
                    if (passwordTry >= 1) {
                        passwordTry = 0
                        if (cameraFuncation != null) {
                            cameraFuncation!!.tackPicture(datas)
                        }
                    }
                } catch (e: Exception) {
                    passcode = ""
                    FeelDot(passcode)
                }
            }
        }
    }

    fun openApp(packageName: String?) {
        val manager = packageManager
        try {
            val i = manager.getLaunchIntentForPackage(packageName!!)
            if (i == null) {
                Toast.makeText(this@NewActivity1, "Can't open app", Toast.LENGTH_SHORT).show()
                //throw new PackageManager.NameNotFoundException();
            }
            i!!.addCategory(Intent.CATEGORY_LAUNCHER)
            startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(this@NewActivity1, "Can't open app", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        count++
        if (count < 2) {
            Toast.makeText(this@NewActivity1, "Press again to exit!", Toast.LENGTH_SHORT).show()
        } else {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
    }

    private fun SavePreferences(key: String, value: String) {
        val sharedPreferences = getSharedPreferences("pref", 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    private fun getpreferences(key: String): String? {
        val sharedPreferences = getSharedPreferences("pref", 0)
        return sharedPreferences.getString(key, "0")
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        //Log.d("onpause", "true");
        super.onPause()
    }

    override fun onStart() {
        // TODO Auto-generated method stub
        super.onStart()
        // to lock app while minimize and reopen
        // SavePreferences("Lock", "True");
        isCorrectPasswordEntered = false
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
        if (!isCorrectPasswordEntered) {
            SavePreferences("Lock", "False")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(RESULT_OK)
        finish()
    }

    fun displaysecurityDialogue() {

        ForgotPasswordDialog(this@NewActivity1).showDialog(object: AppInterface.OnForgetPasswordDialogListener{
            override fun onForgetPasswordDone() {
                val i = Intent(applicationContext, FirstActivity::class.java)
                i.putExtra("from_main", true)
                startActivityForResult(i, 1)
            }

        })
    }

    companion object {
        //---------------------------------------------------------------------------------------------------------------------
        fun StringToBitMap(encodedString: String?): Bitmap? {
            return try {
                val encodeByte =
                    Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(
                    encodeByte, 0,
                    encodeByte.size
                )
            } catch (e: Exception) {
                e.message
                null
            }
        }
    }
}

