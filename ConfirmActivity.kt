package com.applock.lock.apps.fingerprint.password.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.LOGIN_TYPE
import com.applock.lock.apps.fingerprint.password.utils.PARAM_VALID_BACKGROUND
import com.applock.lock.apps.fingerprint.password.utils.PARAM_VALID_SOUND
import com.applock.lock.apps.fingerprint.password.utils.PIN_PASS
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults1
import com.applock.lock.apps.fingerprint.password.utils.getSelectedBackgroundId
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.hideStatusBarAndNavigationBar
import com.applock.lock.apps.fingerprint.password.view.DonePasswordChangedDialog
import com.applock.lock.apps.fingerprint.password.wallpaper.GradientBackgroundDataProvider

class ConfirmActivity : Activity() {
    var passcode = ""
    var lout_main_passcode: RelativeLayout? = null
    var rlSwitchPattern: RelativeLayout? = null
    var img_dot1: ImageView? = null
    var img_dot2: ImageView? = null
    var img_dot3: ImageView? = null
    var img_dot4: ImageView? = null
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
    var lout_numback: RelativeLayout? = null
    var lout_clear: RelativeLayout? = null
    var setpin: String? = null
    private var from_main = false
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        hideStatusBarAndNavigationBar(this)
        setContentView(R.layout.first_activity)
        from_main = intent.getBooleanExtra("from_main", false)

        val b = intent.extras
        setpin = b!!.getString(PIN_PASS)
        initView()
/*        if (getFromUserDefaults(
                this@ConfirmActivity,
                PARAM_VALID_BACKGROUND
            ) != ""
        ) {
            val background = BitmapDrawable(
                StringToBitMap(
                    getFromUserDefaults(
                        applicationContext,
                        PARAM_VALID_BACKGROUND
                    )
                )
            )
            lout_main_passcode!!.setBackgroundDrawable(background)
        } else {
            lout_main_passcode!!.setBackgroundResource(R.drawable.applock_0)
        }*/
        val selectedBackgroundId = getSelectedBackgroundId(applicationContext)

        val gradientBackViewStateList = GradientBackgroundDataProvider.gradientViewStateList
        gradientBackViewStateList.forEach {
            if (it.id == selectedBackgroundId) {
                it.isChecked = true
                lout_main_passcode!!.background = it.getGradiendDrawable(applicationContext)

            }
        }

    }

    fun initView() {
        rlSwitchPattern = findViewById(R.id.rlSwitchPattern)
        rlSwitchPattern!!.gone()
        if (from_main)rlSwitchPattern!!.gone()
        lout_main_passcode = findViewById(R.id.lout_main_passcode)
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
        lout_numback = findViewById(R.id.lout_numback)
        lout_clear = findViewById(R.id.lout_clear)
        img_dot1 = findViewById(R.id.img_dot1)
        img_dot2 = findViewById(R.id.img_dot2)
        img_dot3 = findViewById(R.id.img_dot3)
        img_dot4 = findViewById(R.id.img_dot4)
        val txt_enter_password = findViewById<TextView>(R.id.txt_enter_password)
        txt_enter_password.text = "confirm_pin"
        ButtonClick()
    }

    var mp: MediaPlayer? = null
    private fun ButtonClick() {
        lout_num1!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "1"
                FeelDot(passcode)
            }
        }
        lout_num2!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "2"
                FeelDot(passcode)
            }
        }
        lout_num3!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "3"
                FeelDot(passcode)
            }
        }
        lout_num4!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "4"
                FeelDot(passcode)
            }
        }
        lout_num5!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "5"
                FeelDot(passcode)
            }
        }
        lout_num6!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "6"
                FeelDot(passcode)
            }
        }
        lout_num7!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "7"
                FeelDot(passcode)
            }
        }
        lout_num8!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "8"
                FeelDot(passcode)
            }
        }
        lout_num9!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "9"
                FeelDot(passcode)
            }
        }
        lout_num0!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            if (passcode.length != 4) {
                passcode = passcode + "0"
                FeelDot(passcode)
            }
        }
        lout_numback!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                    PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            backButtonClick(passcode)
        }
        lout_clear!!.setOnClickListener { v: View? ->
            if (mp != null) {
                mp!!.release()
            }
            if (getFromUserDefaults1(
                    this@ConfirmActivity,
                   PARAM_VALID_SOUND
                ) == 1
            ) {
                mp = MediaPlayer.create(this@ConfirmActivity, R.raw.click)
                mp!!.start()
            }
            clearButtonClick()
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
                img_dot1!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
                img_dot2!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
                img_dot3!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
                img_dot4!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 1) {
            try {
                img_dot1!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot2!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
                img_dot3!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
                img_dot4!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 2) {
            try {
                img_dot1!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot2!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot3!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
                img_dot4!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 3) {
            try {
                img_dot1!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot2!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot3!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot4!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_empty
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 4) {
            try {
                img_dot1!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot2!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot3!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                img_dot4!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
            if (setpin == pass)
            {
                if (from_main)
                {
                    Log.d("++++++++++++", "FromMail----DonePassCode Dialog------------");

                    /*Intent i = new Intent(ConfirmActivity.this, ChangePasswordDoneActivity.class);
                    i.putExtra("pin", pass);
                    i.putExtra("type", "0");
                    startActivityForResult(i, 1);*/

                DonePasswordChangedDialog(this@ConfirmActivity).showDialog(
                        "0",
                        pass,
                        object : AppInterface.OnPasswordChangedDoneListener {
                            override fun onPasswordChangedDone() {
                                this@ConfirmActivity.setResult(RESULT_OK)
                                onBackPressed()
                              //  finish()
                            }
                        })
                     }
                else
                {
                    Log.d("++++++++++++", "ConfirmEmailActivity- not from Main-----------");

                    val i = Intent(this@ConfirmActivity, ConfirmEmailActivity::class.java)
                    val b = Bundle()
                    b.putString(PIN_PASS, pass)
                    b.putInt(LOGIN_TYPE,0)
                    i.putExtras(b)
                    startActivityForResult(i, 1)
                }
            } else {
                Log.d("++++++++++++", "WrongPassCode------------");

                try {
                    val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
                    findViewById<View>(R.id.shakelayout).startAnimation(shake)
                    if (getpreferences("Vibrate").equals("Yes", ignoreCase = true)) {
                        val v =
                            this.applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
                        v.vibrate(200)
                    }
                    passcode = ""
                    FeelDot(passcode)
                } catch (e: Exception) {
                    passcode = ""
                    FeelDot(passcode)
                }
            }
        }
    }

    private fun getpreferences(key: String): String? {
        val sharedPreferences = getSharedPreferences("pref", 0)
        return sharedPreferences.getString(key, "0")
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       // if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            setResult(RESULT_OK)
            finish()
       // }
    }


    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
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