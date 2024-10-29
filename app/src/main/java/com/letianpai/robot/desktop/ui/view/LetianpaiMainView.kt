package com.letianpai.robot.desktop.ui.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.letianpai.robot.components.network.lexnet.callback.DeviceChannelLogoCallBack
import com.letianpai.robot.components.network.nets.GeeUINetResponseManager
import com.letianpai.robot.components.parser.logo.LogoInfo
import com.letianpai.robot.desktop.MainActivity
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.callback.OpenAppCallback
import com.letianpai.robot.desktop.callback.OpenAppCallback.OpenAppCommandListener
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.ui.activity.AppListActivity
import com.letianpai.robot.desktop.utils.ImageDownloader
import com.letianpai.robot.desktop.utils.KeyConsts
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

class LetianpaiMainView : LinearLayout {
    private var mContext: Context? = null

    //    private ImageView ivAppIcon;
    //    private TextView appTitle;
    private var openAppList: SettingsButton? = null
    private var backButton: BackButton? = null
    private var ivAppIcon: Button? = null
    private var bg_image: ImageView? = null


    private var updateViewHandler: UpdateViewHandler? = null

    constructor(context: Context) : super(context) {
        inits(context)
    }

    private fun inits(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.letianpai_main, this)
        initViews()
        addListeners()
        loadPreDownImage()
        imageUrl
    }

    val imageUrl: Unit
        get() {
            Log.d("ZG", "getImageUrl")
            GeeUINetResponseManager.getInstance(mContext!!)!!.logoInfo

            val listener = object : DeviceChannelLogoCallBack.DeviceChannelLogoUpdateListener {
                override fun onLogoInfoUpdate(logoInfo: LogoInfo?) {
                    updateViewData(logoInfo)
                }
            }
            DeviceChannelLogoCallBack.instance.setDeviceChannelLogoUpdateListener(listener)
        }

    /***
     * Update Background
     * @param logoInfo
     */
    private fun updateViewData(logoInfo: LogoInfo?) {
        Thread {
            if (logoInfo?.data != null && (!TextUtils.isEmpty(logoInfo.data!!.desktop_logo))) {
                updateBackground(logoInfo.data!!.desktop_logo)
                downImage(logoInfo.data!!.desktop_logo!!)
            }
        }.start()
    }

    private fun downImage(imageUrl: String) {
        val file = File(sDCardDataPath, "image.jpg")
        try {
            // Attempt to delete an existing file with the same name (if it exists)
            if (file.exists()) {
                file.delete()
            }
            // Creating a new file
            val created = file.createNewFile()
            if (created) {
                val destinationPath = file.path
                ImageDownloader.downloadImage(imageUrl, destinationPath)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    /***
     * Load last loaded image
     * @return
     */
    private fun loadPreDownImage() {
        val file = File(sDCardDataPath, "image.jpg")
        if (file.exists()) {
            // Image file exists, load image
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
            bg_image!!.setImageBitmap(myBitmap)


            ////
//            ImageView bg_image = findViewById(R.id.bg_image);
//            bg_image.setImageResource(R.drawable.bg17); // 设置占位符图片
        }
    }

    @get:RequiresApi(api = Build.VERSION_CODES.FROYO)
    private val sDCardDataPath: String?
        get() {
            val externalFilesDir = mContext!!.getExternalFilesDir(null)
            return externalFilesDir?.absolutePath
        }

    private fun addListeners() {
        openAppList!!.setOnClickListener { openAppListView() }

        backButton!!.setOnClickListener { responseBack() }

        ivAppIcon!!.setOnClickListener { openGeeUISettings() }
        OpenAppCallback.instance
            .setOpenAppListener { (mContext as MainActivity).finish() }
    }

    private fun responseBack() {
        Log.e("letianpai_test", "============= keyImageButton ===============")
        (mContext as MainActivity).finish()
        if ((mContext as MainActivity).robotMode == 11) {
            Log.e(
                "letianpai_",
                "changeMode_data:(MainActivity) mContext).getRobotMode()======== 11 ========: " + (mContext as MainActivity).robotMode
            )
            changeRobotMode()
        } else if ((mContext as MainActivity).robotMode == 3) {
            changeStaticMode()
        } else if ((mContext as MainActivity).robotMode == 5) {
            changeSleepMode()
        }
    }

    private fun initViews() {
        openAppList = findViewById(R.id.openAppList)
        backButton = findViewById(R.id.back_main)
        ivAppIcon = findViewById(R.id.ivAppIcon)
        bg_image = findViewById(R.id.bg_image)
        updateViewHandler = UpdateViewHandler(
            mContext!!.applicationContext
        )
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inits(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inits(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        inits(context)
    }

    private fun openAppListView() {
        val intent = Intent(mContext, AppListActivity::class.java)
        val packageName = (mContext as MainActivity).prePackageName
        val mode = (mContext as MainActivity).robotMode
        intent.putExtra("package", packageName)
        intent.putExtra("mode", mode)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mContext!!.startActivity(intent)
    }

    private fun openGeeUISettings() {
        val packageName = "com.robot.geeui.setting"
        val activityName = "com.robot.geeui.setting.MainActivity"
        val intent = Intent()
        intent.putExtra(OPEN_FROM, OPEN_FROM_TITLE)
        intent.setComponent(ComponentName(packageName, activityName))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mContext!!.startActivity(intent)
    }

    fun changeRobotMode() {
        changeMode("robot")
    }

    fun changeSleepMode() {
        changeMode("sleep")
    }

    fun changeMode(mode: String) {
        val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
        Log.e("letianpai", "changeMode_data: $data")
        ModeChangeCmdCallback.instance
            .changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data)
    }

    fun changeStaticMode() {
        val packageName = (mContext as MainActivity).prePackageName
        if (TextUtils.isEmpty(packageName)) {
            return
        }
        val mode: String =
            RobotAppListManager.getInstance(mContext!!).getModeNameByPackage(packageName).toString()
        if (TextUtils.isEmpty(mode)) {
            return
        }
        val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
        Log.e("letianpai_", "changeMode_data: $data")
        ModeChangeCmdCallback.instance
            .changeRobotMode(KeyConsts.Companion.CHANGE_SHOW_MODE, data)
    }

    fun updateBackground(url: String?) {
        Log.d("ZG", "updateBackground")
        val message = Message()
        message.what = UPDATE_BACKGROUND
        message.obj = url
        updateViewHandler!!.sendMessage(message)
    }

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context =
            WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_BACKGROUND -> {
                    Log.d("ZG", "updateBackground AAA" + msg.obj as String)

                    //                    Picasso.with(mContext)
//                            .load((String) msg.obj)
//                            .resize(500, 500)
//                            .centerCrop()
//                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .into(bg_image);
                    val file: File = File(sDCardDataPath, "image.jpg")

                    DownloadImageTask().execute(msg.obj as String, file.absolutePath)
                }
            }
        }
    }


    private inner class DownloadImageTask : AsyncTask<String?, Void?, String?>() {

        override fun onPostExecute(result: String?) {
            if (result != null) {
                val imgFile = File(result)
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                    bg_image!!.setImageBitmap(myBitmap)
                }
            }
        }
        override fun doInBackground(vararg params: String?): String? {
            val urlString = params[0] ?: ""
            val destinationPath = params[1] ?: ""
            return ImageDownloader.downloadImage(urlString, destinationPath)
        }
    }


    companion object {
        private const val UPDATE_BACKGROUND = 110

        private const val OPEN_FROM = "from"
        private const val OPEN_FROM_TITLE = "from_title"
    }
}

