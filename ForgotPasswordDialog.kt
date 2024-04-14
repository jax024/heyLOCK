package com.applock.lock.apps.fingerprint.password.view


import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast

import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.AppInterface.OnForgetPasswordDialogListener
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_ANSWER
import com.applock.lock.apps.fingerprint.password.utils.SECURITY_QUESTION
import com.applock.lock.apps.fingerprint.password.utils.getFromUserDefaults

class ForgotPasswordDialog(private val activity: Activity) {
    private var dialog: Dialog? = null
    private var strType: String? = null
    private var strPin: String? = null

    private var onForgetPasswordDialogListener: OnForgetPasswordDialogListener? = null
    fun showDialog(onForgetPasswordDialogListener: OnForgetPasswordDialogListener?) {
        strType = strType
        strPin = strPin
        this.onForgetPasswordDialogListener = onForgetPasswordDialogListener
        Init()
    }

    private fun Init() {
        dialog = Dialog(activity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.lout_forgot_passsword_dialog)
        dialog!!.setCancelable(true)
        dialog!!.window!!
            .setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.setGravity(Gravity.BOTTOM)
        dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val txtDone = dialog!!.findViewById<TextView>(R.id.txtDone)
        val txtCancel = dialog!!.findViewById<TextView>(R.id.txtCancel)
        val editAnswer = dialog!!.findViewById<EditText>(R.id.editAnswer)
        val tvSecurityQuestion = dialog!!.findViewById<TextView>(R.id.tvSecurityQuestion)
        tvSecurityQuestion.text = getFromUserDefaults(
            activity, SECURITY_QUESTION
        )
        txtDone.setOnClickListener { v: View? ->
            try {
                // get user input and set it to result
                // edit text
                //Log.d("TAG", "INIT >>> "+editAnswer.getText().toString() + " SECURITY_ANSWER >>> "+ Utils.getFromUserDefaults(activity, Constant.SECURITY_ANSWER));
                if (editAnswer.text.toString().trim { it <= ' ' } == getFromUserDefaults(
                        activity,SECURITY_ANSWER
                    )) {
                    onForgetPasswordDialogListener!!.onForgetPasswordDone()
                    dialog!!.dismiss()
                } else {
                    Toast.makeText(
                        activity,
                        activity.resources.getString(R.string.msg_answer_miss_matched),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        txtCancel.setOnClickListener { v: View? ->
            try {
                dialog!!.dismiss()
                onForgetPasswordDialogListener!!.onForgetPasswordCancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        dialog!!.show()
    }
}