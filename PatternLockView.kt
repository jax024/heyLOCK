package com.applock.lock.apps.fingerprint.password.activity


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Base64
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.lock.Lock9View1
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.CameraFuncation
import com.applock.lock.apps.fingerprint.password.utils.ConnectionDetector
import com.applock.lock.apps.fingerprint.password.utils.PATTERN_BG
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_ANSWER
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_QUESTION
import com.applock.lock.apps.fingerprint.password.utils.ServicePreference
import com.applock.lock.apps.fingerprint.password.utils.THEME_TYPE
import com.applock.lock.apps.fingerprint.password.utils.getBitmap
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getThemeType
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.visible
import com.applock.lock.apps.fingerprint.password.view.AlertMessages
import com.applock.lock.apps.fingerprint.password.view.SavePatternLockActivty


class PatternLockView(
    context: Context?,
    var datas: String?,
    attrs: AttributeSet?,
    var listener: LockCloseListener?
) : RelativeLayout(context, attrs) {
    private val TAG = PatternLockView::class.java.simpleName
    var applicationContext: Context? = null
    var view: View? = null
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    var message: AlertMessages? = null
    var mRegisterTask: AsyncTask<Void, Void, Void>? = null
    var icon: Drawable? = null
    var packagename: String? = null
    var rootview: LinearLayout? = null

    //ImageView ivpopup;
    var img_dot: ImageView? = null
    var from_app = false
    var count = 0
    var passwordTry = 0
    var isCorrectPasswordEntered = false
    var lock9View: Lock9View1? = null
  lateinit  var lout_toast: CardView
    lateinit  var txt_toast_msg: TextView
    lateinit   var lout_forgot_dialog: RelativeLayout
    var Onclick = OnClickListener { v -> // TODO Auto-generated method stub
        when (v.id) {
            else -> {}
        }
    }
    private var cameraFuncation: CameraFuncation? = null
    private var surfaceView: SurfaceView? = null
    private var CorrectPattern: String? = null

    interface LockCloseListener {
        fun onCloseLock()
        fun onBackCloseLock()
    }

    init {
        intView(context)
    }

    fun intView(c: Context?) {
        applicationContext = c
        view = LayoutInflater.from(applicationContext)
            .inflate(R.layout.patternlock_activity, this, true)
        cd = ConnectionDetector(applicationContext!!)
        isInternetPresent = cd!!.isConnectingToInternet

        //adView = (AdView) view.findViewById(R.id.adView_lock_pattern);

        //------------------Initialize------------
        Init()
    }

    val packageName: String
        get() = applicationContext!!.packageName

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

    fun Init() {
        //Log.d("TAG", "INIT >>> PATTERN_LOCK_VIEW");
        surfaceView = view!!.findViewById<View>(R.id.picSurfaceView) as SurfaceView
        cameraFuncation = CameraFuncation(applicationContext!!, surfaceView!!)
        lock9View = view!!.findViewById<View>(R.id.lock_9_view) as Lock9View1
        rootview = view!!.findViewById<View>(R.id.rootview) as LinearLayout
        lout_toast = view!!.findViewById(R.id.lout_toast)
        lout_forgot_dialog = view!!.findViewById(R.id.lout_forgot_dialog)
        txt_toast_msg = view!!.findViewById(R.id.txt_toast_msg)
        lout_toast.setVisibility(GONE)
        lout_forgot_dialog.setVisibility(GONE)


        //-----------------------set background blure image-------------------------------
        if (getThemeType(applicationContext, THEME_TYPE) != packageName) {
            rootview!!.setBackgroundDrawable(
                getBitmap(
                    applicationContext, PATTERN_BG
                )
            )
        } else {
            if (getBitmap(applicationContext, PATTERN_BG) == null) {
                rootview!!.setBackgroundResource(R.drawable.applock_0)
            } else {
                rootview!!.setBackgroundDrawable(
                    getBitmap(
                        applicationContext, PATTERN_BG
                    )
                )
            }
        }

        //---------set apps name ,icon and forget password------------------------------
        //ivpopup = (ImageView) view.findViewById(R.id.ivpopup);
        img_dot = view!!.findViewById<View>(R.id.img_dot) as ImageView

        val tvForgotPass = view!!.findViewById<TextView>(R.id.tvForgotPass)

        tvForgotPass.setOnClickListener { view: View? -> displaysecurityDialogue() }

        try {
            if (datas == null || datas.equals("", ignoreCase = true)) {
                datas = applicationContext!!.packageName
            }
            try {
                icon = if (datas != null) {
                    applicationContext!!.packageManager.getApplicationIcon(datas!!)
                } else {
                    applicationContext!!.packageManager.getApplicationIcon(packageName)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            img_dot!!.setImageDrawable(icon)
        } catch (e: Exception) {
        }


        lock9View!!.setCallBack(object : Lock9View1.CallBack {
            override fun onFinish(password: String?) {
                try {
                    CorrectPattern = getFromUserDefaults(
                        applicationContext!!, PIN
                    )
                    if (CorrectPattern != null) {
                        if (password != CorrectPattern) {
                            //---camera start-------
                            passwordTry = passwordTry + 1
                            if (passwordTry >= 1) {
                                passwordTry = 0
                                if (cameraFuncation != null) {
                                    cameraFuncation!!.tackPicture(datas)
                                }
                            }
                            val shake = AnimationUtils.loadAnimation(
                                applicationContext,
                                R.anim.shake
                            )
                            img_dot!!.startAnimation(shake)
                        } else {
                            //--------------correct password--
                            if (from_app) {
                                val i = Intent(applicationContext, MainActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                applicationContext!!.startActivity(i)
                            } else {
                                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    val servicePreference = ServicePreference(
                                        applicationContext
                                    )
                                    servicePreference.SetLockUnlock(true)
                                    //                                    setResult(RESULT_OK);
//                                    finish();
                                    if (listener != null) {
                                        listener!!.onCloseLock()
                                    }
                                } else {
                                    SavePreferences("Lock", "True")
                                    isCorrectPasswordEntered = true
                                    //                                    setResult(RESULT_OK);
//                                    finish();
                                    if (listener != null) {
                                        listener!!.onCloseLock()
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                }
            }
        })

    }

    private fun showFilterPopup(v: View) {
        val wrapper: Context = ContextThemeWrapper(
            applicationContext,
            R.style.YourActionBarWidget
        )
        val popup = PopupMenu(wrapper, v)
        popup.menuInflater.inflate(
            R.menu.menu_popup_pattern,
            popup.menu
        )

        // Setup menu item selection
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_keyword -> {
                    displaysecurityDialogue()
                    true
                }

                else -> false
            }
        })
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show()
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
                val i = Intent(applicationContext, SavePatternLockActivty::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                applicationContext!!.startActivity(i)
                if (listener != null) listener!!.onCloseLock()
            } else {
                showToast(applicationContext!!.resources.getString(R.string.msg_answer_miss_matched))
            } }

    }


    fun showToast(msg: String?) {
        txt_toast_msg!!.text = msg
        lout_toast!!.visibility = VISIBLE
        Handler(Looper.myLooper()!!).postDelayed({ lout_toast!!.visibility = GONE }, 2000)
    }

    companion object {
        var name: String? = null
        var email: String? = null
        var acc_type: String? = null
        var package_name: String? = null
        var track_country: String? = null
        var DeviceId: String? = null

        /*   private MaterialLockView materialLockView;*/ //AdView adView;
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