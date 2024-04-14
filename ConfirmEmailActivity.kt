package com.applock.lock.apps.fingerprint.password.activity


import android.R.attr.button
import android.app.Activity
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.utils.FILE_NAME
import com.applock.lock.apps.fingerprint.password.utils.FILE_NAME_TEMP
import com.applock.lock.apps.fingerprint.password.utils.LOGIN_TYPE
import com.applock.lock.apps.fingerprint.password.utils.PIN
import com.applock.lock.apps.fingerprint.password.utils.PIN_PASS
import com.applock.lock.apps.fingerprint.password.utils.PreferenceManager
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_ANSWER
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_QUESTION
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.saveIntegerToUserDefaults
import com.applock.lock.apps.fingerprint.password.utils.saveToUserDefaults
import com.applock.lock.apps.fingerprint.password.view.AlertMessages


class ConfirmEmailActivity : Activity() {
    var loutNextBtn: LinearLayout? = null
    var etAnswer: EditText? = null
    var message: AlertMessages? = null
    var spinner_question: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   AdsUtils.IS_SPLASH = false
        setContentView(R.layout.activity_confirmemail)
        message = AlertMessages(this)
        loutNextBtn = findViewById(R.id.loutNextBtn)
        spinner_question = findViewById(R.id.spinner_question)
        etAnswer = findViewById(R.id.etAnswer)

        etAnswer!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Not needed for this implementation
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Check if EditText has text
                if (s.toString().isEmpty()) {
                    // If EditText has no text, set button alpha to 0.5
                    loutNextBtn!!.setAlpha(0.5f)
                } else {
                    // If EditText has text, set button alpha to 1
                    loutNextBtn!!.setAlpha(1f)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Not needed for this implementation
            }
        })


        val stringQuestionList = resources.getStringArray(R.array.array_question)
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stringQuestionList)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner_question?.adapter = dataAdapter
        spinner_question?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        loutNextBtn!!.setOnClickListener(View.OnClickListener {
            if (etAnswer!!.text.toString().trim { it <= ' ' } == "") {
               /* Toast.makeText(
                    applicationContext,
                    "Please enter security question answer..",
                    Toast.LENGTH_SHORT
                ).show()*/
            }
            else
            {
                Log.d("++++++++++++", "-------ConfirmEmailActivity-----------");

                saveToUserDefaults(
                    applicationContext,
                    SECURITY_ANSWER,
                    etAnswer!!.text.toString().trim { it <= ' ' })
                     saveToUserDefaults(
                    applicationContext,
                    SECURITY_QUESTION,
                    spinner_question!!.selectedItem.toString()
                )
                if ("xiaomi" == Build.MANUFACTURER.lowercase() && !PreferenceManager.getInstance(this@ConfirmEmailActivity).isAutoStartDialogShow())
                {
                    Log.d("++++++++++++", "-------AutoStartManagementActivity-----------");

                    val intent1 = Intent()
                    intent1.component = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                    startActivity(intent1)
                    PreferenceManager.getInstance(this@ConfirmEmailActivity).setAutoStartDialogShow(true)
                }
                else
                {
                    Log.d("++++++++++++", "-------AutoStartManagementActivity----ELSE-------");

                    try{
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm.isAcceptingText)
                    {
                        val view1 = currentFocus
                        if (view1 != null)
                        {
                            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            manager.hideSoftInputFromWindow(view1.windowToken, 0)
                        }
                    }

                    saveToUserDefaults(this@ConfirmEmailActivity, PIN, intent.extras!!.getString(PIN_PASS))
                    saveIntegerToUserDefaults(this@ConfirmEmailActivity, LOGIN_TYPE, intent.extras!!.getInt(LOGIN_TYPE))
                    saveToUserDefaults(this@ConfirmEmailActivity, FILE_NAME, getFromUserDefaults(this@ConfirmEmailActivity, FILE_NAME_TEMP))


                    Log.d("++++++ConfirmEmailActivity++++++", "-------MainActivity-----------");
                    val i = Intent(this@ConfirmEmailActivity, MainActivity::class.java)
                    startActivity(i)
                    }catch (e: Exception)
                    { Log.d("++++++++", " >>> "+e.message) }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setResult(RESULT_OK)
                finish()
            }

        }
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        private const val REQUEST_CODE_ASK_PERMISSIONS = 124
        fun needPermissionForBlocking(context: Context): Boolean {
            val appOps =
                context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow(
                "android:get_usage_stats",
                Process.myUid(),
                context.packageName
            )
            return mode == AppOpsManager.MODE_ALLOWED
        }
    }
}