package com.example.newproject.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.example.newproject.MainActivity
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.MlManagerAppliciation.Companion.appPreferences
import com.example.newproject.R

import com.example.newproject.utils.AppPreferences
import com.example.newproject.utils.UtilsApp
import com.example.newproject.utils.UtilsUi
import net.margaritov.preference.colorpicker.ColorPickerPreference

class SettingActivity() : PreferenceActivity() , SharedPreferences.OnSharedPreferenceChangeListener  {

    private lateinit var toolbar : Toolbar
    private lateinit var prefVersion : Preference
    private lateinit var prefLicense : Preference
    private lateinit var prefPrimaryColor : ColorPickerPreference
    private lateinit var prefCustomFilename : ListPreference
    private lateinit var prefSortMode : ListPreference
    private lateinit var prefFABColor : ColorPickerPreference
    private lateinit var prefDeleteAll :Preference
    private lateinit var prefDefaultValues :Preference
    private lateinit var prefNavigationBlack : Preference
    private lateinit var prefCustomPath :Preference
    private lateinit var appPreferences : AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        addPreferencesFromResource(R.xml.setting)

        appPreferences = MlManagerAppliciation.appPreferences


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(this)

  //      setTheme(R.style.AppTheme_Toolbar);

        prefVersion = findPreference("prefVersion")
        prefLicense = findPreference("prefLicense")
        prefPrimaryColor = findPreference("prefPrimaryColor") as ColorPickerPreference
        prefFABColor = findPreference("prefFABColor") as ColorPickerPreference
        prefDeleteAll = findPreference("prefDeleteAll")
        prefDefaultValues = findPreference("prefDefaultValues")
        prefNavigationBlack = findPreference("prefNavigationBlack")
        prefCustomFilename = findPreference("prefCustomFilename") as ListPreference
        prefSortMode = findPreference("prefSortMode") as ListPreference
        prefCustomPath = findPreference("prefCustomPath")



        setCustomPathSummary()
        setSortModeSummary()
        setCustomFilenameSummary()
      //  setInitialConfiguration()

        //prefDeleteAll
        prefDeleteAll.setOnPreferenceClickListener { it ->
            prefDeleteAll.setSummary(R.string.deleting)
            prefDeleteAll.isEnabled = false
            val deleteAll: Boolean = UtilsApp.deleteAllFiles()
            if (deleteAll) {
                prefDeleteAll.setSummary(R.string.deleting_done)
                MainActivity.appsExtracted.clear()
            } else {
                prefDeleteAll.setSummary(R.string.deleting_error)
            }
            prefDeleteAll.isEnabled = true
            true
        }

        Toast.makeText(this , appPreferences.getPrimaryColor().toString() , Toast.LENGTH_LONG).show()

     /*   prefPrimaryColor.setOnPreferenceClickListener {
            getApplication().setTheme(R.style.NewProject1);
            true
        }*/

    }


    private fun setInitialConfiguration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.title = "Setting"
        }
    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val pref: Preference = findPreference(key)
        if (pref === prefCustomFilename) {
            setCustomFilenameSummary()
        } else if (pref === prefSortMode) {
            setSortModeSummary()
        } else if (pref === prefCustomPath) {
           setCustomPathSummary()
        }

        if (appPreferences.getNightMode()){
            MainActivity.setDarkMode()
        }else{
            MainActivity.disableDarkMode()
        }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MainActivity.window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                MainActivity.window1.statusBarColor = UtilsUi.darker(MlManagerAppliciation.appPreferences.getPrimaryColor(), 0.8)
                MainActivity.toolbar.setBackgroundColor(MlManagerAppliciation.appPreferences.getPrimaryColor())
              //  MainActivity.bottomAppBar.setBackgroundColor(appPreferences.getPrimaryColor())

                MainActivity.bottomAppBar.setBackgroundColor(UtilsUi.light(appPreferences.getPrimaryColor() , 0.8))
                MainActivity.fab.setBackgroundColor(appPreferences.getPrimaryColor())
            }

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (AppInfoActivity.toolbar != null && AppInfoActivity.linearLayout != null) {
               // AppInfoActivity.window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
              //  AppInfoActivity.window1.statusBarColor = UtilsUi.darker(MlManagerAppliciation.appPreferences.getPrimaryColor(), 0.8)
                AppInfoActivity.toolbar.setBackgroundColor(MlManagerAppliciation.appPreferences.getPrimaryColor())
                AppInfoActivity.linearLayout.setBackgroundColor(MlManagerAppliciation.appPreferences.getPrimaryColor())
            }
        }*/
        }

  private fun setCustomFilenameSummary() {
        val filenameValue: Int = MlManagerAppliciation.appPreferences.getCustomFileName().toInt()-1
        prefCustomFilename.summary =
            resources.getStringArray(R.array.filenameEntries)[filenameValue]
    }

    private fun setSortModeSummary() {
        val sortValue: Int = appPreferences.getSortMode().toInt() - 1
        prefSortMode.summary = resources.getStringArray(R.array.sortEntries)[sortValue]
    }

   private fun setCustomPathSummary() {
        val path: String = appPreferences.getCustomPath().toString()
        if (path == UtilsApp.getDefaultAppFolder().path) {
            prefCustomPath.summary =
                resources.getString(R.string.button_default) + ": " + UtilsApp.getDefaultAppFolder().path
        } else {
            prefCustomPath.summary = path
        }
    }

}