package com.applock.lock.apps.fingerprint.password.inroduler

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.databinding.ActivityIntrodureBinding
import com.applock.lock.apps.fingerprint.password.databinding.ActivityIntrodurePreviewBinding
import com.applock.lock.apps.fingerprint.password.utils.PreferenceManager
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.inVisible
import com.applock.lock.apps.fingerprint.password.utils.visible
import com.applock.lock.apps.fingerprint.password.view.Hack
import com.applock.lock.apps.fingerprint.password.view.MediaDB
import com.bumptech.glide.Glide

class IntrodurePreviewActivity : AppCompatActivity(){
    private var binding: ActivityIntrodurePreviewBinding? = null
    var adapter: IntrodureHackListAdapter? = null
    private val TAG = "IntrodureActivity++++++"
    private var appName="null"
    private var path = "null"
    private var timestamp = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntrodurePreviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        getIntentData()
        initView()
    }

    private fun getIntentData() {
        appName=  intent!!.getStringExtra("appName")!!
       path= intent!!.getStringExtra("path")!!
        timestamp=intent!!.getStringExtra("timestamp")!!

    }

    private fun initView() {
        if (timestamp.isNotEmpty()) {
            //tvTimeStamp.text=hackList[position].timestamp
        }
        if (path.isNotEmpty()) {
            Glide.with(this@IntrodurePreviewActivity).load(path).into(binding!!.ivTempImage)
        }

        setViewClicks()
    }


    private fun setViewClicks() {

        binding!!.ivBack.setOnClickListener { onBackPressed() }
        binding!!.ivDelete.setOnClickListener { displayDeleteDialogue() }

    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }


    private fun displayDeleteDialogue() {
        val dialog = Dialog(this@IntrodurePreviewActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_introdure_delete)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val tvDeleteNow = dialog.findViewById<TextView>(R.id.tvDeleteNow)
        tvDeleteNow.setOnClickListener {
            // Delete all records
            val dbh = MediaDB(this@IntrodurePreviewActivity)
            dbh.deleteHackFile(path)
            dialog.dismiss()
            setResult(RESULT_OK)
            finish()
        }
        tvCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

}
