package com.applock.lock.apps.fingerprint.password.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.applock.lock.apps.fingerprint.password.databinding.ActivityFileListBinding
import com.applock.lock.apps.fingerprint.password.databinding.ActivityVaultFileListBinding
import com.applock.lock.apps.fingerprint.password.navigation.lock.VideoPreviewActivity
import com.applock.lock.apps.fingerprint.password.navigation.vault.FileListAdapter
import com.applock.lock.apps.fingerprint.password.navigation.vault.MediaFile
import com.applock.lock.apps.fingerprint.password.navigation.vault.VaultFileListAdapter
import com.applock.lock.apps.fingerprint.password.navigation.vault.VaultFragment
import com.applock.lock.apps.fingerprint.password.utils.AUDIO_SDCARD_PATH_ABOVE_Q
import com.applock.lock.apps.fingerprint.password.utils.AUDIO_TYPE
import com.applock.lock.apps.fingerprint.password.utils.FILE_SDCARD_PATH_ABOVE_Q
import com.applock.lock.apps.fingerprint.password.utils.FILE_TYPE
import com.applock.lock.apps.fingerprint.password.utils.INTENT_HIDETYPE
import com.applock.lock.apps.fingerprint.password.utils.IS_FROM_VAULT
import com.applock.lock.apps.fingerprint.password.utils.PHOTO_SDCARD_PATH_ABOVE_Q
import com.applock.lock.apps.fingerprint.password.utils.PHOTO_TYPE
import com.applock.lock.apps.fingerprint.password.utils.REQUEST_HIDE_AUDIOS
import com.applock.lock.apps.fingerprint.password.utils.REQUEST_HIDE_FILES
import com.applock.lock.apps.fingerprint.password.utils.REQUEST_HIDE_PHOTOS
import com.applock.lock.apps.fingerprint.password.utils.REQUEST_HIDE_VIDEOS
import com.applock.lock.apps.fingerprint.password.utils.TYPE
import com.applock.lock.apps.fingerprint.password.utils.VIDEO_SDCARD_PATH_ABOVE_Q
import com.applock.lock.apps.fingerprint.password.utils.VIDEO_TYPE
import com.applock.lock.apps.fingerprint.password.utils.gone
import com.applock.lock.apps.fingerprint.password.utils.putAudioHideUri
import com.applock.lock.apps.fingerprint.password.utils.putFileHideUri
import com.applock.lock.apps.fingerprint.password.utils.putImageHideUri
import com.applock.lock.apps.fingerprint.password.utils.putVideoHideUri
import com.applock.lock.apps.fingerprint.password.utils.visible
import com.applock.lock.apps.fingerprint.password.view.DragSelectTouchListener
import com.applock.lock.apps.fingerprint.password.view.DragSelectionProcessor
import com.applock.lock.apps.fingerprint.password.view.DragSelectionProcessor.ISelectionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class VaultFileListActivity : AppCompatActivity(), VaultFileListAdapter.OnItemClickListener {
    private val TAG = "ActivityVaultFileListBinding++++++"

    private lateinit var binding: ActivityVaultFileListBinding

    private val mMode: DragSelectionProcessor.Mode = DragSelectionProcessor.Mode.Simple
    private var mDragSelectTouchListener: DragSelectTouchListener? = null

    private var mDragSelectionProcessor: DragSelectionProcessor? = null
    var adapter: VaultFileListAdapter? = null


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaultFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData(intent)
        clickListners()
    }

    private fun clickListners() {
        binding.ivBack.setOnClickListener {
            if (adapter?.getCountSelected()!! > 0) {
                adapter?.deselectAll()
                binding.btnHideFile.gone()
                binding.tvSelectAll.visible()
                binding.tvDesectAll.gone()

            } else {
                onBackPressed()
            }
        }
        binding.tvSelectAll.setOnClickListener {
            adapter?.selectAll()
            binding.tvDesectAll.visible()
            binding.tvSelectAll.gone()
            binding.btnHideFile.visible()
        }
        binding.tvDesectAll.setOnClickListener {
            adapter?.deselectAll()
            binding.tvSelectAll.visible()
            binding.tvDesectAll.gone()
            binding.btnHideFile.gone()
        }
        binding.btnHideFile.setOnClickListener {
            selectedItemsList = adapter?.getSelectedItems() ?: arrayListOf()
            Log.d(
                TAG,
                "selectedItemsListSize-----${selectedItemsList.size}"
            )
            Log.d(
                TAG,
                "selectedItemsListAt 0-----${selectedItemsList.get(0).filePath}"
            )
            if (isFromVault) {

            openFile(selectedItemsList.get(0).filePath,
                getMimeTypeFromPath(selectedItemsList.get(0).filePath)!!,
                this@VaultFileListActivity)


           /*     val intent = Intent(this@VaultFileListActivity, VideoPreviewActivity::class.java)
                intent.putExtra("path", selectedItemsList.get(0).filePath)
                startActivity(intent)
                onBackPressed()*/

         /*
                val intent = Intent(this@FileListActivity, VaultPreviewActivity::class.java)
                intent.putExtra("path", selectedItemsList.get(0).filePath)
                startActivity(intent)
                onBackPressed()*/
            } else {
                if (type == PHOTO_TYPE) {
                    hidePhoto()
                }
                if (type == VIDEO_TYPE) {
                    hideVideo()
                }
                if (type == AUDIO_TYPE) {
                    hideAudio()
                }
                if (type == FILE_TYPE) {
                    hideFile()
                }
            }

        }
    }

    fun openFile(filePath: String, mimeType: String, context: Context)
    {
        val fileUri = Uri.parse("file://$filePath")
        val contentUri = FileProvider.getUriForFile(
            this@VaultFileListActivity,
            getApplicationContext().getPackageName() + ".provider",
            File(filePath)
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(contentUri, mimeType)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Handle the case where no viewer application is available
            // or inform the user that no suitable application was found.
        }




    }

    fun getMimeTypeFromPath(filePath: String): String? {
        val extension = filePath.substringAfterLast('.')
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
    }



    private fun getIntentData(intent: Intent?) {
        isFromVault = intent?.getBooleanExtra(IS_FROM_VAULT, false)!!
        type = intent.getStringExtra(TYPE)!!

        if (isFromVault) {
            binding.btnHideFile.text = "UnHide"
        } else {
            binding.btnHideFile.text = "Hide"

        }

        receivedFilesData = if (!isFromVault) {
            ArrayList(FolderListActivity.selectedImages!!)

        } else {

            if (type == PHOTO_TYPE) {
                ArrayList(VaultFragment.itemImageFolderClickList!!)

            } else if (type == VIDEO_TYPE) {
                ArrayList(VaultFragment.itemVideoFolderClickList!!)

            } else if (type == AUDIO_TYPE) {
                ArrayList(VaultFragment.itemAudioFolderClickList!!)

            } else
                ArrayList(VaultFragment.itemFileFolderClickList!!)


        }

        setFileListRecyclerView(receivedFilesData)
    }

    private fun setFileListRecyclerView(receivedFilesData: ArrayList<MediaFile>) {
        adapter = VaultFileListAdapter(this@VaultFileListActivity, receivedFilesData, type)
        binding.rvFileList.adapter = adapter
        adapter!!.setOnItemClickListener(this)

        // Add the DragSelectListener
        mDragSelectionProcessor = DragSelectionProcessor(object : ISelectionHandler {
            override val selection: Set<Int>
                get() = adapter!!.getSelection()

            override fun isSelected(index: Int): Boolean {
                return adapter!!.getSelection().contains(index)
            }

            override fun updateSelection(
                start: Int,
                end: Int,
                isSelected: Boolean,
                calledFromOnStart: Boolean
            ) {
                adapter!!.selectRange(start, end, isSelected)
            }
        }).withMode(mMode)
        mDragSelectTouchListener =
            DragSelectTouchListener().withSelectListener(mDragSelectionProcessor)
        updateSelectionListener()
        binding.rvFileList.addOnItemTouchListener(mDragSelectTouchListener!!)

    }

    private fun updateSelectionListener() {
        mDragSelectionProcessor!!.withMode(mMode)
        Log.d(TAG, "updateSelectionListener-------: ${mMode.name}")
    }

    override fun onFileItemClick(position: Int, mediaFile: MediaFile) {
        if (adapter != null) {
            adapter!!.toggleSelection(position)
        }
    }

    override fun onItemLongClick(view: View?, position: Int, mediaFile: MediaFile): Boolean {
        mDragSelectTouchListener!!.startDragSelection(position)
        return true
    }

    override fun onSelectionCountChanged(selectedCount: Int) {

        if (isFromVault) {
            binding.btnHideFile.text = "UnHide ${selectedCount}"
        } else {
            binding.btnHideFile.text = "Hide ${selectedCount}"
        }
        if (selectedCount > 0) {
            binding.btnHideFile.visible()

        } else {
            binding.btnHideFile.gone()
        }
    }

    private fun hidePhoto() {

        createImageDirAboveQ()
        startProgressActivity(PHOTO_TYPE)

    }

    private fun hideVideo() {
        createVideoDirAboveQ()
        startProgressActivity(VIDEO_TYPE)

    }

    private fun hideAudio() {
        createAudioDirAboveQ()
        startProgressActivity(AUDIO_TYPE)

    }

    private fun hideFile() {
        createFileDirAboveQ()
        startProgressActivity(FILE_TYPE)

    }

    private val imagePermissionForQ: Unit
        private get() {

            createImageDirAboveQ()
            startProgressActivity(PHOTO_TYPE)

        }
    private val audioPermissionForQ: Unit
        private get() {

            createAudioDirAboveQ()
            startProgressActivity(AUDIO_TYPE)

        }

    private val filePermissionForQ: Unit
        private get() {

            createFileDirAboveQ()
            startProgressActivity(FILE_TYPE)

        }

    private val videoPermissionForQ: Unit
        private get() {
            createVideoDirAboveQ()
            startProgressActivity(VIDEO_TYPE)

        }

    @Throws(IOException::class)
    private fun copyFile(src: File, dst: File): Boolean {
        if (src.absolutePath.toString()
            == dst.absolutePath.toString()
        ) {
            return true
        } else {
            val `is`: InputStream = FileInputStream(src)
            val os: OutputStream = FileOutputStream(dst)
            val buff = ByteArray(1024)
            var len: Int
            while (`is`.read(buff).also { len = it } > 0) {
                os.write(buff, 0, len)
            }
            `is`.close()
            os.close()
        }
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            Log.d(
                TAG,
                "-----onActivityResult-----"
            )

            if (requestCode == REQUEST_HIDE_PHOTOS) {
                if (data!!.data != null) {
                    val treeUri = data.data
                    putImageHideUri(treeUri.toString(), this)
                    hidePhoto()
                }
            }
            if (requestCode == REQUEST_HIDE_VIDEOS) {
                if (data!!.data != null) {
                    val treeUri = data.data
                    putVideoHideUri(treeUri.toString(), this)
                    hideVideo()
                }
            }

            if (requestCode == REQUEST_HIDE_AUDIOS) {
                if (data!!.data != null) {
                    val treeUri = data.data
                    putAudioHideUri(treeUri.toString(), this)
                    hideAudio()
                }
            }

            if (requestCode == REQUEST_HIDE_FILES) {
                if (data!!.data != null) {
                    val treeUri = data.data
                    putFileHideUri(treeUri.toString(), this)
                    hideFile()
                }
            }


            if (requestCode == REQUEST_CODE_HIDE_PROGRESS) {
                Log.d(TAG, "-----REQUEST_CODE_HIDE_PROGRESS-------")
                Log.d(TAG, "-----HideType-------$type")

                if (type == PHOTO_TYPE) {
                    VaultFragment.fragmentScope.launch {

                        VaultFragment.getImagesList(this@VaultFileListActivity, false)
                        VaultFragment.getHiddenPhotos(this@VaultFileListActivity, false, false)
                        VaultFragment.catPhotoSelected(this@VaultFileListActivity)
                    }
                }
                if (type == VIDEO_TYPE) {

                    VaultFragment.fragmentScope.launch {
                        VaultFragment.getVideosList(this@VaultFileListActivity, false)
                        VaultFragment.getHiddenVideos(this@VaultFileListActivity, false, false)
                        VaultFragment.catVideoSelected(this@VaultFileListActivity)
                    }
                }
                if (type == AUDIO_TYPE) {
                    VaultFragment.fragmentScope.launch(Dispatchers.IO) {
                        VaultFragment.getAudioFiles(this@VaultFileListActivity, false)
                        VaultFragment.getHiddenAudios(this@VaultFileListActivity, false, false)
                        VaultFragment.catAudioSelected(this@VaultFileListActivity)

                    }
                }
                if (type == FILE_TYPE) {
                    VaultFragment.fragmentScope.launch(Dispatchers.IO) {
                        VaultFragment.getDocumentFilesList(this@VaultFileListActivity, false)
                        VaultFragment.getHiddenDocumentFiles(this@VaultFileListActivity, false, false)
                        VaultFragment.catFileSelected(this@VaultFileListActivity)
                    }
                }

                // Remove selected items from the original list
                receivedFilesData.removeAll(selectedItemsList)
                setFileListRecyclerView(receivedFilesData)
                adapter?.notifyDataSetChanged()
                onBackPressed()
            }

        }

    }

    private fun startProgressActivity(hideType: String) {

        Log.d(
            TAG,
            "selectedItemsList---Size()--${selectedItemsList.get(0).fileName}----${
                selectedItemsList.get(
                    0
                ).filePath
            }"
        )


        if (!selectedItemsList.isNullOrEmpty()) {
            val intent = Intent(this, HideFileActivity::class.java)
            intent.putExtra(INTENT_HIDETYPE, hideType)
            startActivityForResult(intent, REQUEST_CODE_HIDE_PROGRESS)
        }
    }

    private fun createVideoDirAboveQ() {
        val dir = File(VIDEO_SDCARD_PATH_ABOVE_Q)
        if (dir.exists() && dir.isDirectory) {
            Log.d(TAG, "VideoDir ABOVE_Q Directory exist")

        } else {
            try {
                if (dir.mkdirs()) {
                    Log.d(TAG, "VideoDir ABOVE_Q Directory Created")

                } else {
                    Log.d(TAG, "VideoDir ABOVE_Q Directory Not Created")


                }
            } catch (e: Exception) {
                Log.d(TAG, "VideoDir Exception---" + e.message)

            }
        }
    }


    fun createImageDirAboveQ() {
        val dir = File(PHOTO_SDCARD_PATH_ABOVE_Q)
        if (dir.exists() && dir.isDirectory) {
            Log.d(TAG, "ImageABOVE_Q Directory exist")
        } else {
            try {
                if (dir.mkdirs()) {
                    Log.d(TAG, "ImageDir ABOVE_Q  Created")
                } else {
                    Log.d(TAG, "ImageDir ABOVE_Q Not Created")
                }
                MediaScannerConnection.scanFile(
                    this@VaultFileListActivity, arrayOf<String>(dir.absolutePath), null
                ) { str: String?, uri: Uri? ->

                }
            } catch (e: Exception) {
                Log.e(TAG, "ImageDir Exception-----" + e.message)
            }
        }
    }


    fun createFileDirAboveQ() {
        val dir = File(FILE_SDCARD_PATH_ABOVE_Q)
        if (dir.exists() && dir.isDirectory) {
            Log.d(TAG, "FileDir ABOVE_Q Directory exist")

        } else {
            try {
                if (dir.mkdirs()) {
                    Log.d(TAG, "FileDir ABOVE_Q  Created")

                } else {
                    Log.d(TAG, "FileDir ABOVE_Q not Created")

                }
            } catch (e: Exception) {
                Log.e(TAG, "FileDir Exception---" + e.message)

            }
        }
    }


    fun createAudioDirAboveQ() {
        val dir = File(AUDIO_SDCARD_PATH_ABOVE_Q)
        if (dir.exists() && dir.isDirectory) {
            Log.d(TAG, "AudioDir ABOVE_Q Directory exist")

        } else {
            try {
                if (dir.mkdirs()) {
                    Log.d(TAG, "AudioDir ABOVE_Q  Created")
                } else {
                    Log.d(TAG, "AudioDir ABOVE_Q  not Created")
                }
            } catch (e: Exception) {
                Log.d(TAG, "AudioDir Exception----" + e.message)
            }
        }
    }


    companion object {
        lateinit var selectedItemsList: ArrayList<MediaFile>
        var count = 0
        private lateinit var receivedFilesData: ArrayList<MediaFile>

        private val REQUEST_CODE_HIDE_PROGRESS = 101
        var loading: ProgressDialog? = null
        var type = PHOTO_TYPE
        var isFromVault = false

    }

}