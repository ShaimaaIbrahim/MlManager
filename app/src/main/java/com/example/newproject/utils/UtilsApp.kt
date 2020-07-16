package com.example.newproject.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.newproject.MainActivity
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.R
import com.example.newproject.bindAppApk
import com.google.android.material.button.MaterialButton
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


object UtilsApp {


    private val MY_PERMISSIONS_REQUEST_WRITE_READ: Int = 100



    public  val   appPreferences  : AppPreferences = MlManagerAppliciation.appPreferences
    public  var  appsExtracted: ArrayList<AppInfo> = ArrayList()
    private  var extractedSet: HashSet<String> = appPreferences.getExtractedApps() as HashSet<String>

    fun getDefaultAppFolder() : File{
        val folder = File(Environment.getExternalStorageDirectory().toString() + File.separator + "/ML Manager")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder
    }

    fun deleteAllFiles(): Boolean{
        var bol : Boolean = false
        var f : File = MlManagerAppliciation.appPreferences.getCustomPath()
        if (f.exists() && f.isDirectory) {
            val files = f.listFiles()
            if (files!=null && !files.isEmpty()){
                for (file in files) {
                    file.delete() }
            }
            if (f.listFiles().isEmpty()) {
                bol = true } }
        return bol
    }

    fun getAllFiles(){
        var f : File = MlManagerAppliciation.appPreferences.getCustomPath()
        if (f.exists() && f.isDirectory) {
            val files = f.listFiles()
    }}

    fun copyFiles(appInfo: AppInfo) : Boolean?{

        var res : Boolean= false
        val initialFile = File(appInfo.source)
        val finalFile = getOutputFilename(appInfo)

        try {
            FileUtils.copyFile(initialFile , finalFile!!)
            res = true

            if (!extractedSet.contains(appInfo.apk)){
                 extractedSet.add(appInfo.apk)
                 appPreferences.setExtractedApps(extractedSet)
                Log.e("nano1" , "added")
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }

        return res
    }

    fun deleteExtractedApp(appInfo: AppInfo){
        if (extractedSet.contains(appInfo.apk))
        extractedSet.remove(appInfo.apk)
        appPreferences.setExtractedApps(extractedSet)
    }


    fun getOutputFilename(appInfo: AppInfo?): File? {

        return File(
            UtilsApp.getDefaultAppFolder().getPath() + "/" + appInfo?.let {
                UtilsApp.getAPKFilename(it)
            } + ".apk"
        )
    }

    fun getAPKFilename(appInfo: AppInfo): String? {
        val appPreferences: AppPreferences = MlManagerAppliciation.appPreferences
        val res: String
        when (appPreferences.getCustomFileName()) {
            "1" -> res = appInfo.apk.toString() + "_" + appInfo.version
            "2" -> res = appInfo.name.toString() + "_" + appInfo.version
            "4" -> res = appInfo.name
            else -> res = appInfo.apk
        }
        return res
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun checkPermissions(activity: Activity , context: Context): Boolean? {
        var res = false
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), UtilsApp.MY_PERMISSIONS_REQUEST_WRITE_READ
                )
            }
        } else {
            res = true
        }
        return res
    }

    fun getShareIntent(outputFilename: File? , context: Context): Intent {
        val intent = Intent()
        var uri : Uri? =
            FileProvider.getUriForFile( context , "com.example.newproject.provider" , outputFilename!!)

        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "application/vnd.android.package-archive"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        return intent
    }

    fun isAppFavorite(appInfo: AppInfo, appFavorites: Set<String?>): Boolean? {
        var res = false
        if (appFavorites.contains(appInfo.apk)) {
            res = true
        }
        return res
    }

    /**
     * Save the app as favorite
     * @param context Context
     * @param menuItem Item of the ActionBar
     * @param isFavorite true if the app is favorite, false otherwise
     */
    fun setAppFavorite(context: Context?, imageView: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.ic_baseline_favorite_24))

        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.ic_baseline_favorite_border_24))

        }
    }

    /**
     * Retrieve if an app is hidden
     * @param appInfo App to check
     * @param appHidden Set with apps
     * @return true if the app is hidden, false otherwise
     */
    fun isAppHidden(appInfo: AppInfo, appHidden: Set<String?>): Boolean? {
        var res = false
        if (appHidden.contains(appInfo.apk)) {
            res = true
        }
        return res
    }

    /**
     * Save the app as hidden
     * @param context Context
     * @param fabHide FAB button to change
     * @param isHidden true if the app is hidden, false otherwise
     */
    fun setAppHidden(context: Context, fabHide: ImageView , isHidden: Boolean) {
        if (isHidden) {
           // fabHide.setTitle(context.resources.getString(R.string.action_unhide))
            fabHide.setImageResource(R.drawable.ic_baseline_visibility_24)
        } else {
          //  fabHide.setTitle(context.resources.getString(R.string.action_hide))
            fabHide.setImageResource(R.drawable.ic_baseline_visibility_off_24)
        }
    }

    /**
     * Save an app icon to cache folder
     * @param context Context
     * @param appInfo App to save icon
     * @return true if the icon has been saved, false otherwise
     */
    fun saveIconToCache(context: Context, appInfo: AppInfo): Boolean? {
        var res = false
        try {
            val applicationInfo = context.packageManager.getApplicationInfo(appInfo.apk, 0)
            val fileUri = File(context.cacheDir, appInfo.apk)
            val out = FileOutputStream(fileUri)
            val icon = context.packageManager.getApplicationIcon(applicationInfo)
            val iconBitmap = icon as BitmapDrawable
            iconBitmap.bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            res = true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * Delelete an app icon from cache folder
     * @param context Context
     * @param appInfo App to remove icon
     * @return true if the icon has been removed, false otherwise
     */
    fun removeIconFromCache(context: Context, appInfo: AppInfo): Boolean? {
        val file = File(context.cacheDir, appInfo.apk)
        return file.delete()
    }

    /**
     * Get an app icon from cache folder
     * @param context Context
     * @param appInfo App to get icon
     * @return Drawable with the app icon
     */

    fun getIconFromCache(context: Context, appInfo: AppInfo): Drawable? {
        val res: Drawable?
        res = try {
            val fileUri = File(context.cacheDir, appInfo.apk)
            val bitmap = BitmapFactory.decodeFile(fileUri.path)
            BitmapDrawable(context.resources, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            context.resources.getDrawable(R.drawable.ic_android_24dp)
        }
        return res
    }
}