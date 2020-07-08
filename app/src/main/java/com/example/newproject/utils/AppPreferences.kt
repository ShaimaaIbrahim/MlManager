package com.example.newproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.R
import java.io.File

@SuppressLint("CommitPrefEdits")
class AppPreferences(context: Context?){


    private  var sharedPreferences: SharedPreferences?= null
    private var editor: Editor? = null
    private var context: Context? = null

    val KeyPrimaryColor = "prefPrimaryColor"
    val KeyFABColor = "prefFABColor"
    val KeyFABShow = "prefFABShow"
    val KeyNavigationBlack = "prefNavigationBlack"
    val KeyCustomFilename = "prefCustomFilename"
    val KeySortMode = "prefSortMode"
    val KeyIsRooted = "prefIsRooted"
    val KeyCustomPath = "prefCustomPath"

    // List
    val KeyFavoriteApps = "prefFavoriteApps"
    val KeyHiddenApps = "prefHiddenApps"


    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        editor = sharedPreferences!!.edit()
        this.context = context
    }

  fun getCustomFileName() : String{
      return sharedPreferences!!.getString(KeyCustomFilename , "1")!!

  }
    fun getCustomPath (): File{

        val folder = File(UtilsApp.getDefaultAppFolder().path)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder
    }

    fun getSortMode(): String{
        return sharedPreferences!!.getString(KeySortMode ,"1" )!!
    }

    fun getPrimaryColor(): Int {
        return sharedPreferences!!.getInt(KeyPrimaryColor , context!!.resources.getColor(R.color.primaryColor))
    }

    fun getFabColor() : Int{
        return sharedPreferences!!.getInt(KeyFABColor , context!!.resources.getColor(R.color.fab))

    }


}