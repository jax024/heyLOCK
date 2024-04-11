package com.applock.lock.apps.fingerprint.password.inroduler

import android.app.Activity
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
import com.applock.lock.apps.fingerprint.password.utils.AppInterface
import com.applock.lock.apps.fingerprint.password.utils.PreferenceManager
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.inVisible
import com.applock.lock.apps.fingerprint.password.utils.visible
import com.applock.lock.apps.fingerprint.password.view.ForgotPasswordDialog
import com.applock.lock.apps.fingerprint.password.view.Hack
import com.applock.lock.apps.fingerprint.password.view.MediaDB
import com.applock.lock.apps.fingerprint.password.view.SavePatternLockActivty

class IntrodureActivity : AppCompatActivity(), IntrodureHackListAdapter.OnItemClickListener {
    private var binding: ActivityIntrodureBinding? = null
    var adapter: IntrodureHackListAdapter? = null
    private val TAG = "IntrodureActivity++++++"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntrodureBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        initView()
    }

    private fun initView() {
        if (PreferenceManager.getInstance(this@IntrodureActivity).isIntruderSelfieEnable()) {
            getHackListData()
        } else {
            setDisableEyeView()
        }
        setViewClicks()
    }

    private fun getHackListData() {
        setEnableEyeView()
        val dbh = MediaDB(this@IntrodureActivity)
        dbh.open()
        val list = dbh.hacks
        dbh.close()
        if (list.isNotEmpty()) {
            manageEnableDataViewsVisibilities()
            setFolderListRecyclerView(list)
        } else {
            manageDisableDataViewsVisibilities()
        }
    }

    private fun setViewClicks() {
        binding!!.ivDisableEye.setOnClickListener {
            PreferenceManager.getInstance(this@IntrodureActivity).setIntruderSelfieEnable(true)
            getHackListData()

        }
        binding!!.ivEnableEye.setOnClickListener {
            PreferenceManager.getInstance(this@IntrodureActivity).setIntruderSelfieEnable(false)
            setDisableEyeView()
            manageDisableDataViewsVisibilities()
            binding!!.ivIntroNoData.gone()
            binding!!.tvNoData.gone()
        }
        binding!!.ivBack.setOnClickListener { onBackPressed() }
        binding!!.ivDelete.setOnClickListener { displayDeleteDialogue() }
        binding!!.ivSetting.setOnClickListener { }
    }

    private fun setFolderListRecyclerView(list: List<Hack>) {
        adapter = IntrodureHackListAdapter(this@IntrodureActivity, list)
        binding!!.rvhacklist.adapter = adapter
        adapter!!.setOnItemClickListener(this)
    }

    override fun onIntrodureHackItemClick(
        position: Int,
        app: String,
        path: String,
        timestamp: String
    ) {
        val intent = Intent(this@IntrodureActivity, IntrodurePreviewActivity::class.java)
        intent.putExtra("appName",app)
        intent.putExtra("path",path)
        intent.putExtra("timestamp",timestamp)
        startActivityForResult(intent, 2)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.d("onactivity result", "true");
        setResult(Activity.RESULT_OK)
        initView()

       // finish()
    }



    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finish()
    }

    private fun setDisableEyeView() {
        binding!!.ivDisableEye.visible()
        binding!!.ivEnableEye.gone()
    }

    private fun setEnableEyeView() {
        binding!!.ivDisableEye.gone()
        binding!!.ivEnableEye.visible()

    }

    private fun manageEnableDataViewsVisibilities() {
        binding!!.ivTempImage.gone()
        binding!!.ivIntroNoData.gone()
        binding!!.tvNoData.gone()

        binding!!.rvhacklist.visible()
        binding!!.ivSetting.visible()
        binding!!.ivDelete.visible()
    }

    private fun manageDisableDataViewsVisibilities() {
        binding!!.ivSetting.gone()
        binding!!.rvhacklist.gone()
        binding!!.ivDelete.gone()
        binding!!.ivTempImage.gone()

        binding!!.ivIntroNoData.visible()
        binding!!.tvNoData.visible()
    }

    private fun displayDeleteDialogue() {
        val dialog = Dialog(this@IntrodureActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_introdure_delete)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.BOTTOM)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val tvDeleteNow = dialog.findViewById<TextView>(R.id.tvDeleteNow)
        tvDeleteNow.setOnClickListener {
            // Delete all records
            val dbh = MediaDB(this@IntrodureActivity)
            dbh.deleteHack()
            manageDisableDataViewsVisibilities()
            dialog.dismiss()
        }
        tvCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

}
