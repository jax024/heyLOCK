package com.applock.lock.apps.fingerprint.password.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.cardview.widget.CardView
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.activity.FirstActivity
import com.applock.lock.apps.fingerprint.password.activity.MainActivity
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.CameraFuncation
import com.applock.lock.apps.fingerprint.password.utils.ConnectionDetector
import com.applock.lock.apps.fingerprint.password.utils.DIGIT_BG
import com.applock.lock.apps.fingerprint.password.utils.PARAM_VALID_SOUND
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_ANSWER
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_QUESTION
import com.applock.lock.apps.fingerprint.password.utils.ServicePreference
import com.applock.lock.apps.fingerprint.password.utils.THEME_TYPE
import com.applock.lock.apps.fingerprint.password.utils.getBitmap
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults1
import com.applock.lock.apps.fingerprint.password.utils.getThemeType
import com.applock.lock.apps.fingerprint.password.utils.visible

class LockViewExmple(
    context: Context?,
    packagename: String?,
    attrs: AttributeSet?,
    listener: LockCloseListener?
) : RelativeLayout(context, attrs) {
    //private PublishSubject<Void> backKeyPress = PublishSubject.create();
    var applicationContext: Context? = null
    var view: View? = null
    var cameraFuncation: CameraFuncation? = null
    var listener: LockCloseListener?
    var passcode = ""
    var datas: String?
    lateinit var lout_num1: TextView
    lateinit var lout_num2: TextView
    lateinit var lout_num3: TextView
    lateinit var lout_num4: TextView
    lateinit var lout_num5: TextView
    lateinit var lout_num6: TextView
    lateinit var lout_num7: TextView
    lateinit var lout_num8: TextView
    lateinit var lout_num9: TextView
    lateinit var lout_num0: TextView
    lateinit var lout_clear: ImageView
    lateinit var img_dot: ImageView
    lateinit var img_dot1: ImageView
    lateinit var img_dot2: ImageView
    lateinit var img_dot3: ImageView
    lateinit var img_dot4: ImageView
    lateinit var lout_main_passcode: RelativeLayout
    lateinit var icon: Drawable

    //ImageView ivpopup;
    var url = "http://developers.com/email/index.php/email/send_email"
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var isCorrectPasswordEntered = false
    var passwordTry = 0
    var count = 0


    /*Drawable d;
    Bitmap bitmap;
    Drawable d1;
    Bitmap bitmap1;*/
    lateinit var drawableEmpty: Drawable
    lateinit var drawableFilled: Drawable
    lateinit var packagename: String
    var from_app: Boolean
    lateinit var surfaceView: SurfaceView
    lateinit var lout_toast: CardView
    lateinit var txt_toast_msg: TextView
    lateinit var lout_forgot_dialog: RelativeLayout

    interface LockCloseListener {
        fun onCloseLock()
        fun onBackCloseLock()
    }

    fun showToast(msg: String?) {
        txt_toast_msg.text = msg
        lout_toast.visibility = VISIBLE
        Handler(Looper.myLooper()!!).postDelayed({ lout_toast!!.visibility = GONE }, 2200)
    }

    fun intView(c: Context?) {
        Log.d("++++LOckViewExample+++", "------ LOckViewExample-----")

        applicationContext = c
        view = LayoutInflater.from(applicationContext).inflate(R.layout.activity_new, this, true)
        cd = ConnectionDetector(applicationContext)
        isInternetPresent = cd!!.isConnectingToInternet
        surfaceView = view!!.findViewById(R.id.picSurfaceView)
        cameraFuncation = CameraFuncation(applicationContext, surfaceView)
        lout_toast = view!!.findViewById(R.id.lout_toast)
        lout_forgot_dialog = view!!.findViewById(R.id.lout_forgot_dialog)
        txt_toast_msg = view!!.findViewById(R.id.txt_toast_msg)
        lout_toast.setVisibility(GONE)
        lout_forgot_dialog.setVisibility(GONE)

        //ivpopup = view!!.findViewById(R.id.ivpopup);
        lout_num1 = view!!.findViewById(R.id.lout_num1)
        lout_num2 = view!!.findViewById(R.id.lout_num2)
        lout_num3 = view!!.findViewById(R.id.lout_num3)
        lout_num4 = view!!.findViewById(R.id.lout_num4)
        lout_num5 = view!!.findViewById(R.id.lout_num5)
        lout_num6 = view!!.findViewById(R.id.lout_num6)
        lout_num7 = view!!.findViewById(R.id.lout_num7)
        lout_num8 = view!!.findViewById(R.id.lout_num8)
        lout_num9 = view!!.findViewById(R.id.lout_num9)
        lout_num0 = view!!.findViewById(R.id.lout_num0)
        lout_clear = view!!.findViewById(R.id.lout_clear)
        lout_main_passcode = view!!.findViewById(R.id.lout_main_passcode)
        img_dot = view!!.findViewById(R.id.img_dot)
        img_dot1 = view!!.findViewById(R.id.img_dot1)
        img_dot2 = view!!.findViewById(R.id.img_dot2)
        img_dot3 = view!!.findViewById(R.id.img_dot3)
        img_dot4 = view!!.findViewById(R.id.img_dot4)
        val tvForgotPass = view!!.findViewById<TextView>(R.id.tvForgotPass)
      /*  val tvCancel = view!!.findViewById<TextView>(R.id.tvCancel)
        tvCancel.visibility = GONE*/
        tvForgotPass.setOnClickListener { view: View? -> displaysecurityDialogue() }
        setButtonBackgrund()
        LoadBackgroundImage()
        if (datas == null || datas.equals("", ignoreCase = true)) {
            datas = applicationContext!!.packageName
            Log.d("++++NewActivity1+++DEMO", "------ LOckViewExample-----"+applicationContext!!.packageName)

        }
        try {
            icon = applicationContext!!.packageManager.getApplicationIcon(datas!!)
        } catch (e: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        img_dot.setImageDrawable(icon)
        ButtonClick()
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (listener != null) listener!!.onBackCloseLock()
        } else if (event.keyCode == KeyEvent.KEYCODE_HOME) {
        }
        return true
    }

    val packageName: String
        get() = applicationContext!!.packageName

    fun setButtonBackgrund() {
        //if (Utils.getThemeType(context, Constant1.THEME_TYPE).equals(getPackageName())) {
        drawableEmpty = resources.getDrawable(R.drawable.icn_dot_empty)
        drawableFilled = resources.getDrawable(R.drawable.icn_dot_fill)

    }

    fun LoadBackgroundImage() {
        if (getThemeType(applicationContext, THEME_TYPE) != packageName) {
            lout_main_passcode.background = getBitmap(
                applicationContext, DIGIT_BG
            )
        } else {
            if (getBitmap(applicationContext, DIGIT_BG) == null) {
                lout_main_passcode.setBackgroundResource(R.drawable.applock_0)
            } else {
                lout_main_passcode.setBackgroundDrawable(
                    getBitmap(
                        applicationContext, DIGIT_BG
                    )
                )
            }
        }
    }

    var mp: MediaPlayer? = null

    init {
        this.listener = listener
        datas = packagename
        from_app = false
        intView(context)
    }

    private fun ButtonClick() {
        /*ivpopup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showFilterPopup(v);
            }
        });*/
        lout_num1!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num2!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num3!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num4!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num5!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num6!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num7!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num8!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num9!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_num0!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                // TODO Auto-generated method stub
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
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
        })
        lout_clear!!.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (mp != null) {
                    mp!!.release()
                }
                if (getFromUserDefaults1(
                        applicationContext!!,
                        PARAM_VALID_SOUND
                    ) == 1
                ) {
                    mp = MediaPlayer.create(applicationContext, R.raw.click)
                    mp!!.start()
                }
                // TODO Auto-generated method stub
                backButtonClick(passcode)
            }
        })
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
        if (pass.length == 4) {
            Log.d("++++NewActivity1+++DEMO", "------ LOckViewExample-----pass.length == 4"+applicationContext!!.packageName)

            try {
                img_dot1!!.setImageDrawable(drawableFilled)
                img_dot2!!.setImageDrawable(drawableFilled)
                img_dot3!!.setImageDrawable(drawableFilled)
                img_dot4!!.setImageDrawable(drawableFilled)
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
            if (passcode.equals(
                    getFromUserDefaults(
                        applicationContext!!, PIN
                    ), ignoreCase = true
                )
            ) {
                Log.d("++++NewActivity1+++DEMO", "------ LOckViewExample-----passcode.equals-----")
                Log.d("++++NewActivity1+++DEMO", "------ LOckViewExample-----From App-----")


                if (from_app) {
                    Log.d("++++NewActivity1+++DEMO", "------ LOckViewExample-----From App---MainActivity-----")

                    val i = Intent(applicationContext, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext!!.startActivity(i)
                } else {
                    Log.d("++++NewActivity1+++DEMO", "------ LOckViewExample-----Not From App---HideLockFrom Service-----")

                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                        val servicePreference = ServicePreference(applicationContext)
                        servicePreference.SetLockUnlock(true)
                        if (listener != null) {
                            listener!!.onCloseLock()
                        }
                        //                        openApp(datas);
                    } else {
                        SavePreferences("Lock", "True")
                        isCorrectPasswordEntered = true
                        if (listener != null) {
                            listener!!.onCloseLock()
                        }
                        //                        openApp(datas);
                    }
                }
                // unregisterReceiver(mBatInfoReceiver);
            } else {
                try {
                    val shake = AnimationUtils.loadAnimation(
                        applicationContext,
                        R.anim.shake
                    )
                    img_dot!!.startAnimation(shake)
                    findViewById<View>(R.id.shakelayout).startAnimation(shake)
                    if (getpreferences("Vibrate").equals("Yes", ignoreCase = true)) {
                        val v = applicationContext!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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
        val manager = applicationContext!!.packageManager
        try {
            val i = manager.getLaunchIntentForPackage(packageName!!)
            if (i == null) {
                showToast("Can't open app")
            }
            i!!.addCategory(Intent.CATEGORY_LAUNCHER)
            applicationContext!!.startActivity(i)
        } catch (e: Exception) {
            showToast("Can't open app")
            //Toast.makeText(getApplicationContext(), "Can't open app", Toast.LENGTH_SHORT).show();
        }
    }

    private fun SavePreferences(key: String, value: String) {
        val sharedPreferences = applicationContext!!.getSharedPreferences("pref", 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    private fun getpreferences(key: String): String? {
        val sharedPreferences = applicationContext!!.getSharedPreferences("pref", 0)
        return sharedPreferences.getString(key, "0")
    }


    fun displaysecurityDialogue() {
        lout_forgot_dialog.visible()
        (view!!.findViewById<View>(R.id.question) as TextView).setText(
            getFromUserDefaults(
                applicationContext!!,
                SECURITY_QUESTION
            )
        )
        (view!!.findViewById<View>(R.id.txtCancel) as TextView).setOnClickListener{ lout_forgot_dialog!!.visibility = GONE }

        (view!!.findViewById<View>(R.id.txtDone) as TextView).setOnClickListener {
            if ((  (view!!.findViewById<View>(R.id.editAnswer) as EditText)!!.text.toString().trim { it <= ' ' } == getFromUserDefaults(
                    applicationContext!!, SECURITY_ANSWER
                ))) {
                val imm =
                    applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isAcceptingText) {
                    //writeToLog("Software Keyboard was shown");
                    //  hideSoftKeyboard(binding.edtSearch);
                    val manager =
                        applicationContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    manager.hideSoftInputFromWindow(view!!.windowToken, 0)
                } else {
                    // writeToLog("Software Keyboard was not shown");
                }
                lout_forgot_dialog!!.visibility = GONE
                val i = Intent(applicationContext, FirstActivity::class.java)
                i.putExtra("from_main", true)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext!!.startActivity(i)
                if (listener != null) listener!!.onCloseLock()
            } else {
                showToast(applicationContext!!.resources.getString(R.string.msg_answer_miss_matched))
            } }

    }

    companion object {
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