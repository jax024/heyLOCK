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
   private fun audio() {
/*-------ada-per---------
       import android.content.Context
               import android.view.LayoutInflater
               import android.view.View
               import android.view.ViewGroup
               import android.widget.AdapterView
               import android.widget.ArrayAdapter
               import android.widget.TextView
               import androidx.fragment.app.FragmentActivity

       class AudioAdapter(
           context: Context,
           resource: Int,
           private val audioList: List<String>
       ) : ArrayAdapter<String>(context, resource, audioList), View.OnClickListener {

           override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
               val itemView = convertView ?: LayoutInflater.from(context)
                   .inflate(R.layout.audio_item, parent, false)

               val audioNameTextView: TextView = itemView.findViewById(R.id.audioNameTextView)

               val audioName = audioList[position]
               audioNameTextView.text = audioName

               // Set OnClickListener for the item
               itemView.setOnClickListener {
                   // Show the AudioPlayerFragment when the item is clicked
                   val activity = context as FragmentActivity
                   val audioPlayerFragment = AudioPlayerFragment()
                   audioPlayerFragment.show(activity.supportFragmentManager, audioPlayerFragment.tag)
               }

               return itemView
           }

           override fun onClick(v: View?) {
               // Handle click event if needed
           }
       }*/


/*---fragment-----


       import android.media.MediaPlayer
               import android.os.Bundle
               import android.os.Handler
               import android.view.LayoutInflater
               import android.view.View
               import android.view.ViewGroup
               import android.widget.SeekBar
               import androidx.fragment.app.DialogFragment
               import kotlinx.android.synthetic.main.fragment_audio_player.*

       class AudioPlayerFragment : DialogFragment() {

           private lateinit var mediaPlayer: MediaPlayer
           private lateinit var audioUrl: String
           private var isPlaying = false
           private var durationHandler = Handler()

           override fun onCreateView(
               inflater: LayoutInflater,
               container: ViewGroup?,
               savedInstanceState: Bundle?
           ): View? {
               return inflater.inflate(R.layout.fragment_audio_player, container, false)
           }

           override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
               super.onViewCreated(view, savedInstanceState)

               audioUrl = "your_audio_url_here"

               // Initialize MediaPlayer
               mediaPlayer = MediaPlayer()

               // Set audio source
               mediaPlayer.setDataSource(audioUrl)
               mediaPlayer.prepare()

               // Set audio name
               audioNameTextView.text = "Audio Name"

               // Set duration text
               durationTextView.text = formatTime(mediaPlayer.duration)

               // Set up seekbar
               seekBar.max = mediaPlayer.duration
               seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                   override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                       if (fromUser) {
                           mediaPlayer.seekTo(progress)
                       }
                   }

                   override fun onStartTrackingTouch(seekBar: SeekBar) {
                       // No implementation needed
                   }

                   override fun onStopTrackingTouch(seekBar: SeekBar) {
                       // No implementation needed
                   }
               })

               // Update seekbar and duration text
               durationHandler.postDelayed(updateSeekBarTime, 100)

               // Set up play/pause button
               playPauseButton.setOnClickListener {
                   if (isPlaying) {
                       mediaPlayer.pause()
                       isPlaying = false
                       playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                   } else {
                       mediaPlayer.start()
                       isPlaying = true
                       playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                   }
               }

               // Set up stop button
               stopButton.setOnClickListener {
                   mediaPlayer.stop()
                   mediaPlayer.prepare()
                   isPlaying = false
                   playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                   seekBar.progress = 0
               }
           }

           private val updateSeekBarTime = object : Runnable {
               override fun run() {
                   seekBar.progress = mediaPlayer.currentPosition
                   durationTextView.text = formatTime(mediaPlayer.currentPosition)
                   durationHandler.postDelayed(this, 100)
               }
           }

           private fun formatTime(ms: Int): String {
               val seconds = ms / 1000
               val minutes = seconds / 60
               val remainingSeconds = seconds % 60
               return String.format("%02d:%02d", minutes, remainingSeconds)
           }

           override fun onDestroy() {
               super.onDestroy()
               mediaPlayer.release()
               durationHandler.removeCallbacks(updateSeekBarTime)
           }
       }*/



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
