package com.applock.lock.apps.fingerprint.password.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.activity.FirstActivity
import com.applock.lock.apps.fingerprint.password.lock.Lock9View
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.getSelectedBackgroundId
import com.applock.lock.apps.fingerprint.password.utils.hideStatusBarAndNavigationBar
import com.applock.lock.apps.fingerprint.password.wallpaper.GradientBackgroundDataProvider


class SavePatternLockActivty : FragmentActivity() {
    var rootviews: RelativeLayout? = null
    var tvswitch: RelativeLayout? = null

    /*MaterialLockView materialLockView;*/
    var PattrenDraw: String? = null
    var lock9View: Lock9View? = null
    var loutBottom: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBarAndNavigationBar(this)
        setContentView(R.layout.activity_save_pattern_lock_activty)
        Initialization()
    }

    /*public static String getFromUserDefaults(Context context, String key) {
            Log.d("Utils", "Get:" + key);
            SharedPreferences preferences = context.getSharedPreferences(
                    Constant1.SHARED_PREFS, Context.MODE_PRIVATE);
            return preferences.getString(key, "");
        }*/
    fun Initialization() {
        try {
            //-----------------------set background blure image-------------------------------
            var bitmap: Bitmap
            rootviews = findViewById<View>(R.id.rootviews) as RelativeLayout
            tvswitch = findViewById<View>(R.id.rlSwitchPasscode) as RelativeLayout
            tvswitch!!.setOnClickListener {
                val intent = Intent(
                    this@SavePatternLockActivty,
                    FirstActivity::class.java
                )
                intent.putExtra("from_main", true)
                startActivityForResult(intent, 1)
                finish()
            }

            val selectedBackgroundId = getSelectedBackgroundId(applicationContext)

            val gradientBackViewStateList = GradientBackgroundDataProvider.gradientViewStateList
            gradientBackViewStateList.forEach {
                if (it.id == selectedBackgroundId) {
                    it.isChecked = true
                    rootviews!!.background = it.getGradiendDrawable(applicationContext)

                }
            }


            /* ---today----
                        *//* int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk == android.os.Build.VERSION_CODES.KITKAT) {*//*
            if (getFromUserDefaults(
                    this@SavePatternLockActivty,
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
                //Log.d("TAG", "PATTERN >>> BG_1 >>> "+Utils.getFromUserDefaults(SavePatternLockActivty.this, Constant.PARAM_VALID_BACKGROUND));
            } else {
                saveToUserDefaults(applicationContext, PARAM_VALID_IVDONE, "1")
                rootviews!!.setBackgroundResource(R.drawable.applock_0)
                //Log.d("TAG", "PATTERN >>> BG_2 >>> "+Utils.getFromUserDefaults(SavePatternLockActivty.this, Constant.PARAM_VALID_BACKGROUND));
            }*/

            /*} else {
                if (!Utils.getFromUserDefaults(SavePatternLockActivty.this, Constant1.PARAM_VALID_BACKGROUND).equals("")) {
                    BitmapDrawable background = new BitmapDrawable(StringToBitMap(Utils.getFromUserDefaults(getApplicationContext(), Constant1.PARAM_VALID_BACKGROUND)));
                    bitmap = (Bitmap) background.getBitmap();
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
                }
                rootviews.setBackgroundDrawable(GestureConstant.createBitmap_ScriptIntrinsicBlur(bitmap, 25.0f, SavePatternLockActivty.this));
            }*/


            //-------------------------------------------------------------------------------
            loutBottom = findViewById<View>(R.id.loutBottom) as LinearLayout

            //---------------------Cancel & continue button click event------------
            loutBottom!!.setOnClickListener { // TODO Auto-generated method stub
                finish()
            }

            //--------------------------------------------------
            //------------------------------Lock activity----------------------
            lock9View = findViewById<View>(R.id.lock_9_view) as Lock9View

            lock9View!!.setCallBack(object : Lock9View.CallBack
            {
                override fun onFinish(password: String?)
                {
                    //---------------store password into file---------
                    val patternIntent = Intent(applicationContext, ConfirmPatternLock::class.java)
                    patternIntent.putExtra(PIN, password)
                    patternIntent.putExtra("from_app", intent.getBooleanExtra("from_app",false))
                    patternIntent.putExtra("Packagename", packageName)
                    startActivityForResult(patternIntent, 1)
                }
            })

        } catch (e: Exception) {
        }
    }

    fun SavePatternToFile(Password: String?) {
        val preferences = getSharedPreferences("Patternlockpassword", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("PatternLock", Password)
        editor.commit()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
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
    }
}