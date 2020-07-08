package com.example.newproject.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.newproject.MlManagerAppliciation
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException


object UtilsApp {


    private val MY_PERMISSIONS_REQUEST_WRITE_READ: Int = 100

    public fun getDefaultAppFolder() : File{
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
            for (file in files) {
                file.delete() }
            if (f.listFiles().isEmpty()) {
                bol = true } }
        return bol }


    fun copyFiles(appInfo: AppInfo) : Boolean?{

        var res : Boolean= false
        val initialFile = File(appInfo.source)
        val finalFile = getOutputFilename(appInfo)

        try {
            FileUtils.copyFile(initialFile , finalFile!!)
            res = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return res
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
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), UtilsApp.MY_PERMISSIONS_REQUEST_WRITE_READ
            )
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
}