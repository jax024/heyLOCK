package com.applock.lock.apps.fingerprint.password.activity


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.databinding.FirstActivityBinding
import com.applock.lock.apps.fingerprint.password.utils.ConnectionDetector
import com.applock.lock.apps.fingerprint.password.utils.LOGIN_TYPE
import com.applock.lock.apps.fingerprint.password.utils.PARAM_VALID_SOUND
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.PIN_PASS
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults1
import com.applock.lock.apps.fingerprint.password.utils.getIntegerFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.getSelectedBackgroundId
import com.applock.lock.apps.fingerprint.password.utils.hideStatusBarAndNavigationBar
import com.applock.lock.apps.fingerprint.password.utils.updateLanguage
import com.applock.lock.apps.fingerprint.password.view.DataBase
import com.applock.lock.apps.fingerprint.password.view.PatternLockActivity
import com.applock.lock.apps.fingerprint.password.view.SavePatternLockActivty
import com.applock.lock.apps.fingerprint.password.wallpaper.GradientBackgroundDataProvider

class FirstActivity : Activity() {

    private lateinit var binding: FirstActivityBinding
    private val TAG = "FirstActivity++++++"
    var passcode = ""
    var msg =
        "Mobile data is disabled. Connect to Wi-fi network instead, or enable mobile data and try again."
    var isInternetPresent = false
    var cd: ConnectionDetector? = null
    private var from_main = false
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBarAndNavigationBar(this)
        updateLanguage(this)
        val dataBase = DataBase()
        dataBase.SetContext(this@FirstActivity)

        //----------------------------Create channel-----------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notiUtils = NotificationUtils(this)
        }
        from_main = intent.getBooleanExtra("from_main", false)
        Log.i("------from_main----", "$from_main")
        Log.i("------getFromUserDefaults(this, PIN)----", ""+getFromUserDefaults(this, PIN))

        if (getFromUserDefaults(this, PIN) != "" && !from_main) {
            Log.i("------from_main----OPENLOCKEDSCREEN", "$from_main")
            OPENLOCKEDSCREEN()
        } else {
            Log.i("------from_main----ElsePart", "$from_main")

            binding = FirstActivityBinding.inflate(layoutInflater)
            setContentView(binding.root)
            initView()
            /*            if (getFromUserDefaults(
                                this@FirstActivity,
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
                            binding.loutMainPasscode.setBackgroundDrawable(background)
                        } else {
                            saveToUserDefaults(applicationContext, PARAM_VALID_IVDONE, "1")
                            binding.loutMainPasscode.setBackgroundResource(R.drawable.applock_0)
                        }*/


            val selectedBackgroundId = getSelectedBackgroundId(applicationContext)

            val gradientBackViewStateList = GradientBackgroundDataProvider.gradientViewStateList
            gradientBackViewStateList.forEach {
                if (it.id == selectedBackgroundId) {
                    it.isChecked = true


                    binding.loutMainPasscode.background = it.getGradiendDrawable(applicationContext)

                }
            }


        }
    }

    private fun initView() {
        cd = ConnectionDetector(this)
        isInternetPresent = cd!!.isConnectingToInternet
        binding.txtEnterPassword.text = "Enter_new_pin_code"
        mViewClickListners()
    }

    private fun mViewClickListners() {

        binding.loutNum0.setOnClickListener { _: View? ->
            onClickLockNumber(0)
        }
        binding.loutNum1.setOnClickListener { _: View? ->
            onClickLockNumber(1)
        }
        binding.loutNum2.setOnClickListener { _: View? ->
            onClickLockNumber(2)
        }
        binding.loutNum3.setOnClickListener { _: View? ->
            onClickLockNumber(3)
        }
        binding.loutNum4.setOnClickListener { _: View? ->
            onClickLockNumber(4)
        }
        binding.loutNum5.setOnClickListener { _: View? ->
            onClickLockNumber(5)
        }
        binding.loutNum6.setOnClickListener { _: View? ->
            onClickLockNumber(6)
        }
        binding.loutNum7.setOnClickListener { _: View? ->
            onClickLockNumber(7)

        }
        binding.loutNum8.setOnClickListener { _: View? ->
            onClickLockNumber(8)

        }
        binding.loutNum9.setOnClickListener { _: View? ->
            onClickLockNumber(9)
        }

        binding.loutNumback.setOnClickListener { _: View? ->
            handleMediaPlayer()
            backButtonClick(passcode)
        }
        binding.loutClear.setOnClickListener { _: View? ->
            handleMediaPlayer()
            clearButtonClick()
        }
        binding.rlSwitchPattern.setOnClickListener { _: View? ->
            val pwdIntent: Intent
            pwdIntent = Intent(applicationContext, SavePatternLockActivty::class.java)
            pwdIntent.putExtra("from_app", true)
            pwdIntent.putExtra("Packagename", packageName)
            startActivity(pwdIntent)
            finish()
        }
    }

    private fun onClickLockNumber(number: Int) {
        handleMediaPlayer()
        if (passcode.length != 4) {
            passcode += number
            FeelDot(passcode)
        }
    }

    private fun handleMediaPlayer() {
        if (mp != null) {
            mp!!.release()
        }
        if (getFromUserDefaults1(this@FirstActivity, PARAM_VALID_SOUND) == 1) {
            mp = MediaPlayer.create(this@FirstActivity, R.raw.click)
            mp!!.start()
        }
    }

    private fun backButtonClick(pin1: String) {
        if (pin1.isNotEmpty()) {
            when (pin1.length) {
                1 -> {
                    passcode = pin1.substring(0, 0)
                    FeelDot(passcode)
                }

                2 -> {
                    passcode = pin1.substring(0, pin1.length - 1)
                    FeelDot(passcode)
                }

                3 -> {
                    passcode = pin1.substring(0, pin1.length - 1)
                    FeelDot(passcode)
                }

                4 -> {
                    passcode = pin1.substring(0, pin1.length - 1)
                    FeelDot(passcode)
                }
            }
        }
    }

    private fun clearButtonClick() {
        passcode = ""
        FeelDot(passcode)
    }

    private fun FeelDot(pass: String) {
        Log.i("String Length", "" + pass.length)
        if (pass.isEmpty()) {
            try {
                inValidatePasswordView()
            } catch (e: Exception) {
                e.printStackTrace()
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 1) {
            try {

                inValidatePasswordView()
                binding.imgDot1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 2) {
            try {
                inValidatePasswordView()

                binding.imgDot1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                binding.imgDot2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 3) {
            try {
                inValidatePasswordView()

                binding.imgDot1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                binding.imgDot2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                binding.imgDot3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
        }
        if (pass.length == 4) {
            try {
                binding.imgDot1.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                binding.imgDot2.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                binding.imgDot3.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
                binding.imgDot4.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.icn_dot_fill
                    )
                )
            } catch (e: Exception) {
                passcode = ""
                FeelDot(passcode)
            }
            Log.d("++++++++++++", "ConfirmActivity------------");



            val intent = Intent(this, ConfirmActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PIN_PASS, pass)
            bundle.putBoolean("from_main", from_main)
            intent.putExtras(bundle)
            startActivityForResult(intent, 1)
        }
    }

    private fun inValidatePasswordView() {
        binding.imgDot1.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icn_dot_empty
            )
        )
        binding.imgDot2.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icn_dot_empty
            )
        )
        binding.imgDot3.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icn_dot_empty
            )
        )
        binding.imgDot4.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.icn_dot_empty
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                finish()
            }
            if (requestCode == 58) {
                Log.e("+++++++++", "true");
                finish()
            }
        }
    }

    fun OPENLOCKEDSCREEN() {
        val pwdIntent: Intent
        if (getIntegerFromUserDefaults(applicationContext, LOGIN_TYPE) == 0) {
            Log.i("------from_main----OPENLOCKEDSCREEN", "-NewActivity1-----")

            pwdIntent = Intent(this@FirstActivity, NewActivity1::class.java)
            pwdIntent.putExtra("from_app", true)
            pwdIntent.putExtra("Packagename", packageName)
            startActivity(pwdIntent)
            finish()
        } else if (getIntegerFromUserDefaults(
                applicationContext, LOGIN_TYPE
            ) == 1
        ) {
            Log.i("------from_main----OPENLOCKEDSCREEN", "-PatternLockActivity-----")

            pwdIntent = Intent(applicationContext, PatternLockActivity::class.java)
            pwdIntent.putExtra("from_app", true)
            pwdIntent.putExtra("Packagename", packageName)
            startActivity(pwdIntent)
            finish()
        } else if (getIntegerFromUserDefaults(
                applicationContext, LOGIN_TYPE
            ) == 3
        ) {
            Log.i("------from_main----OPENLOCKEDSCREEN", "-GestureLockActivity-----")

            pwdIntent = Intent(applicationContext, GestureLockActivity::class.java)
            pwdIntent.putExtra("from_app", true)
            pwdIntent.putExtra("Packagename", packageName)
            startActivity(pwdIntent)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        var name: String? = null
        var email: String? = null
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