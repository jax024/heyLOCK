package com.applock.lock.apps.fingerprint.password.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.activity.ConfirmEmailActivity
import com.applock.lock.apps.fingerprint.password.lock.Lock9View
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.FILE_NAME
import com.applock.lock.apps.fingerprint.password.utils.FILE_NAME_TEMP
import com.applock.lock.apps.fingerprint.password.utils.LOGIN_TYPE
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.PIN_PASS
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getSelectedBackgroundId
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.hideStatusBarAndNavigationBar
import com.applock.lock.apps.fingerprint.password.utils.saveIntegerToUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.saveToUserDefaults
import com.applock.lock.apps.fingerprint.password.wallpaper.GradientBackgroundDataProvider
import java.io.FileOutputStream

class ConfirmPatternLock : FragmentActivity() {
    var rootviews: RelativeLayout? = null

    var lock9View: Lock9View? = null
    var PattrenDraw: String? = null
    var pl_message_text: TextView? = null
    var loutBottom: LinearLayout? = null
    var rlSwitch: RelativeLayout? = null
    private var from_main = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBarAndNavigationBar(this)
        setContentView(R.layout.activity_save_pattern_lock_activty)
        from_main = intent.getBooleanExtra("from_main", false)
        Initialization()
    }

    fun Initialization() {
        try {
            //-----------------------set background blure image-------------------------------
            var bitmap: Bitmap
            rootviews = findViewById<View>(R.id.rootviews) as RelativeLayout
            rlSwitch = findViewById<View>(R.id.rlSwitchPasscode) as RelativeLayout
            rlSwitch!!.gone()
            if(from_main) rlSwitch!!.gone()

            /*int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk == android.os.Build.VERSION_CODES.KITKAT) {*/
            /*---today----
                        if (getFromUserDefaults(
                                this@ConfirmPatternLock,
                                PARAM_VALID_BACKGROUND
                            ) != ""
                        ) {
                            val background = BitmapDrawable(
                                StringToBitMap(
                                    getFromUserDefaults(
                                        applicationContext, PARAM_VALID_BACKGROUND
                                    )
                                )
                            )
                            rootviews!!.setBackgroundDrawable(background)
                        } else {
                            saveToUserDefaults(applicationContext, PARAM_VALID_IVDONE, "1")
                            rootviews!!.setBackgroundResource(R.drawable.applock_0)
                        }
            */


            val selectedBackgroundId = getSelectedBackgroundId(applicationContext)

            val gradientBackViewStateList = GradientBackgroundDataProvider.gradientViewStateList
            gradientBackViewStateList.forEach {
                if (it.id == selectedBackgroundId) {
                    it.isChecked = true
                    rootviews!!.background = it.getGradiendDrawable(applicationContext)

                }
            }


            loutBottom = findViewById<View>(R.id.loutBottom) as LinearLayout
            pl_message_text = findViewById<View>(R.id.pl_message_text) as TextView
            pl_message_text!!.text = "draw_pattern_to_confirm"

            //---------------------Cancel & Confirm button click event------------
            loutBottom!!.setOnClickListener { finish() }

            //------------------------------Lock activity----------------------
            lock9View = findViewById<View>(R.id.lock_9_view) as Lock9View

            lock9View!!.setCallBack(object : Lock9View.CallBack {
                override fun onFinish(password: String?) {
                    //---------------store password into file---------
                    try {
                        if (password != null) {
                            if (intent.getStringExtra(PIN).equals(password, ignoreCase = true)) {
                                //---------if confirm password is match then set gesture pattern lock default laock-----------------
                                screenshort1(rootviews)
                                //-----------------------------------------------------------------------------------------------
                                val pass = intent.getStringExtra(PIN)

                                if (from_main)
                                {

                                    Log.d("++++++++++++", "ConfirmEmailActivity- not from Main-----------");

                                    val i = Intent(this@ConfirmPatternLock, ConfirmEmailActivity::class.java)
                                    val b = Bundle()
                                    b.putString(PIN_PASS, pass)
                                    b.putInt(LOGIN_TYPE,1)
                                    i.putExtras(b)
                                    startActivityForResult(i, 1)

                                }
                                else
                                {
                                    DonePasswordChangedDialog(this@ConfirmPatternLock).showDialog(
                                        "1",
                                        pass,
                                        object : AppInterface.OnPasswordChangedDoneListener {
                                            override fun onPasswordChangedDone() {
                                                this@ConfirmPatternLock.setResult(Activity.RESULT_OK)
                                                finish()
                                            }
                                        })
                                }


                            } else {
                                Toast.makeText(
                                    this@ConfirmPatternLock,
                                    "Please enter correct pattern!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@ConfirmPatternLock,
                                "Please draw pattern first!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                    }
                }
            })


        } catch (e: Exception) {
        }
    }

    fun GetExistingPatternLock(): String? {
        val preferences = getSharedPreferences("Patternlockpassword", Context.MODE_PRIVATE)
        return preferences.getString("PatternLock", null)
    }

    override fun onBackPressed() {
        // TODO Auto-generated method stub
        setResult(Activity.RESULT_OK)
        finish()
        super.onBackPressed()
    }


    fun screenshort1(layout: RelativeLayout?) {
        val fileName = "MyFile.png"
        var outputStream: FileOutputStream? = null
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            val myBitmap = captureScreen(layout)
            myBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            saveToUserDefaults(applicationContext, FILE_NAME_TEMP, fileName)
            outputStream.flush()
            outputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(Activity.RESULT_OK)
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

        fun captureScreen(v: View?): Bitmap? {
            var screenshot: Bitmap? = null
            try {
                if (v != null) {
                    // screenshot = Bitmap.createBitmap(v.getMeasuredWidth(),
                    // v.getMeasuredHeight(), Config.ARGB_8888);
                    screenshot = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(screenshot)
                    v.draw(canvas)
                }
            } catch (e: Exception) {
                Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.message)
            }
            return screenshot
        }
    }
}