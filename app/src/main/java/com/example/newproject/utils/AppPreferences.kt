package com.example.newproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.preference.PreferenceManager
import android.util.Log
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
    val KeyEextractedApps ="prefExtractedApps"

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

    fun getFavoriteApps(): Set<String?>? {
        return sharedPreferences!!.getStringSet(KeyFavoriteApps, HashSet())
    }

    fun setFavoriteApps(favoriteApps: Set<String?>?) {
        editor!!.remove(KeyFavoriteApps)
        editor!!.commit()
        editor!!.putStringSet(KeyFavoriteApps, favoriteApps)
        editor!!.commit()
    }

    fun getRootStatus(): Int {
        return sharedPreferences!!.getInt(KeyIsRooted, 0)
    }

    fun setRootStatus(rootStatus: Int) {
        editor!!.putInt(KeyIsRooted, rootStatus)
        editor!!.commit()
    }

    fun getHiddenApps (): Set<String> {
      return sharedPreferences!!.getStringSet(KeyHiddenApps , HashSet())!!
    }

    fun setHiddenApps(hiddenApps: Set<String>) {
        editor!!.remove(KeyHiddenApps)
        editor!!.commit()
        editor!!.putStringSet(KeyHiddenApps , hiddenApps)
        editor!!.commit()
    }

    fun getNightMode() : Boolean {
      return sharedPreferences!!.getBoolean(KeyNavigationBlack , false)
    }

    fun setExtractedApps(appsExtracted: Set<String?>?) {

        if (appsExtracted!!.size >=1 && null != appsExtracted && !appsExtracted.isEmpty()){

            editor!!.remove(KeyEextractedApps)
            editor!!.commit()
            editor!!.putStringSet(KeyEextractedApps , appsExtracted)
            editor!!.commit()
            Log.e("set" , "success")
        }

    }

    fun getExtractedApps() : Set<String>{

        return  sharedPreferences!!.getStringSet(KeyEextractedApps, HashSet())!!

    }
}