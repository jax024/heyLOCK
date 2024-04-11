package com.applock.lock.apps.fingerprint.password.navigation.lock

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.MediaController
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.applock.lock.apps.fingerprint.password.R
import com.applock.lock.apps.fingerprint.password.databinding.ActivityIntrodureBinding
import com.applock.lock.apps.fingerprint.password.databinding.ActivityIntrodurePreviewBinding
import com.applock.lock.apps.fingerprint.password.databinding.ActivityVideoPreviewBinding
import com.applock.lock.apps.fingerprint.password.utils.PreferenceManager
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.inVisible
import com.applock.lock.apps.fingerprint.password.utils.visible
import com.applock.lock.apps.fingerprint.password.view.Hack
import com.applock.lock.apps.fingerprint.password.view.MediaDB
import com.bumptech.glide.Glide

class VideoPreviewActivity : AppCompatActivity(){
    private var binding: ActivityVideoPreviewBinding? = null
    private val TAG = "IntrodureActivity++++++"
    private var appName="null"
    private var path = "null"
    private var timestamp = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPreviewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        getIntentData()
        initView()
    }

    private fun getIntentData() {
       path= intent!!.getStringExtra("path")!!

    }

    private fun initView() {
        if (timestamp.isNotEmpty()) {
            //tvTimeStamp.text=hackList[position].timestamp
        }
        if (path.isNotEmpty()) {

            // Replace videoUri with the path to your video file
            val videoUri = Uri.parse(path)

            binding!!.videoView.setVideoURI(videoUri)

            // Create MediaController
            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding!!.videoView)

            // Set MediaController for VideoView
            binding!!. videoView.setMediaController(mediaController)

            // Start playing the video
            binding!!.videoView.start()
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
        val dialog = Dialog(this@VideoPreviewActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_introdure_delete)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val tvDeleteNow = dialog.findViewById<TextView>(R.id.tvDeleteNow)
        tvDeleteNow.setOnClickListener {
            // Delete all records
            val dbh = MediaDB(this@VideoPreviewActivity)
            dbh.deleteHackFile(path)
            dialog.dismiss()
            setResult(RESULT_OK)
            finish()
        }
        tvCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

}
