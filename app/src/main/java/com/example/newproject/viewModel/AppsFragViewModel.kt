package com.example.newproject.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.R
import com.example.newproject.utils.AppInfo
import com.example.newproject.utils.AppPreferences
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AppsFragViewModel( application: Application) : AndroidViewModel(application) {


    private val packageManager: PackageManager = application.packageManager
    private val context = application
    private lateinit var sortedList :  MutableList<PackageInfo>
    private lateinit var appPreferences: AppPreferences

    private var installedApps : ArrayList<AppInfo> = ArrayList()
    private  var systemApps: ArrayList<AppInfo> = ArrayList()


    val viewKodelJob = Job()
    val scopeCoroutines = CoroutineScope(Dispatchers.IO + viewKodelJob)

    private var  totalApps: Int = 0
    private val actualApps: Int? = 0

    private val _appList = MutableLiveData<List<AppInfo>>()
    val appList : LiveData<List<AppInfo>>
    get()= _appList

    private val _appSystemList = MutableLiveData<List<AppInfo>>()
    val appSystemList : LiveData<List<AppInfo>>
        get()= _appSystemList

    private val _appHiddenList = MutableLiveData<List<AppInfo>>()
    val appHiddenList : LiveData<List<AppInfo>>
        get()= _appHiddenList

init {
    getInstalledApps()
    appPreferences = MlManagerAppliciation.appPreferences
}

   private fun getInstalledApps(){
      scopeCoroutines.launch{
        getInstalledAndSortedApps()
      }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private suspend fun getInstalledAndSortedApps() {
      installedApps.clear()
      systemApps.clear()

      withContext(Dispatchers.Main){
          var packages:List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
          //  Set<String> hiddenApps = appPreferences.getHiddenApps();
          // totalApps = packages.size() + hiddenApps.size();
          // Get Sort Mode
          when(appPreferences.getSortMode()){

              "2"->  Collections.sort(packages , object : Comparator<PackageInfo?>{
                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      val size1 = File(p1!!.applicationInfo.sourceDir).length()
                      val size2 = File(p2!!.applicationInfo.sourceDir).length()
                      return size2.compareTo(size1)
                  } })
              "3" -> Collections.sort(packages , object : Comparator<PackageInfo?>{
                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      return java.lang.Long.toString(p2!!.firstInstallTime)
                          .compareTo(java.lang.Long.toString(p1!!.firstInstallTime))
                  } })
              "4" -> Collections.sort(packages , object : Comparator<PackageInfo>{
                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      return java.lang.Long.toString(p2!!.lastUpdateTime)
                          .compareTo(java.lang.Long.toString(p1!!.lastUpdateTime))
                  }

              })
              else ->   Collections.sort(packages, object : Comparator<PackageInfo?> {

                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      return packageManager.getApplicationLabel(p1!!.applicationInfo).toString()
                          .toLowerCase().compareTo(
                              packageManager.getApplicationLabel(p2!!.applicationInfo).toString()
                                  .toLowerCase())
                  }
              })
          }
          for (packageInfo in packages){

              if (!packageInfo.applicationInfo.equals("") || !packageInfo.packageName.isEmpty()){
                  //non system apps
                  if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                      try {
                          // Non System Apps
                          val tempApp = AppInfo(
                              packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                              packageInfo.packageName,
                              packageInfo.versionName,
                              packageInfo.applicationInfo.sourceDir,
                              packageInfo.applicationInfo.dataDir,
                              packageManager.getApplicationIcon(packageInfo.applicationInfo),
                              false
                          )
                          installedApps.add(tempApp)
                          Log.e("shaimaa" ,tempApp.apk )
                        //  _appList.value = installedApps

                      } catch (e: OutOfMemoryError) {

                          val tempApp = AppInfo(
                              packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                              packageInfo.packageName,
                              packageInfo.versionName,
                              packageInfo.applicationInfo.sourceDir,
                              packageInfo.applicationInfo.dataDir,
                              context.resources.getDrawable(R.drawable.ic_android_24dp),
                              false
                          )
                          installedApps.add(tempApp)
                          Log.e("shaimaa" ,tempApp.apk )
                        //  _appList.value= installedApps

                      } catch (e: Exception) {
                          e.printStackTrace()
                      }
                  }else{
                      try {
                          // System Apps
                          val tempApp = AppInfo(
                              packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                              packageInfo.packageName,
                              packageInfo.versionName,
                              packageInfo.applicationInfo.sourceDir,
                              packageInfo.applicationInfo.dataDir,
                              packageManager.getApplicationIcon(packageInfo.applicationInfo),
                              true
                          )
                          systemApps.add(tempApp)
                          Log.e("shaimaa" ,tempApp.apk )
                       //   _appSystemList.value = systemApps

                      } catch (e : OutOfMemoryError ) {

                          var  tempApp  = AppInfo(
                              packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                              packageInfo.packageName,
                              packageInfo.versionName,
                              packageInfo.applicationInfo.sourceDir,
                              packageInfo.applicationInfo.dataDir,
                              context.resources.getDrawable(R.drawable.ic_android_24dp),
                              false)

                          systemApps.add(tempApp)
                          Log.e("shaimaa" ,tempApp.apk )
                      //    _appSystemList.value = systemApps

                      } catch (e : Exception ) {
                          e.printStackTrace()
                      }
                  }
              }
          }
          _appList.value = installedApps.toList()
          _appSystemList.value =  systemApps.toList()
      }
    }


    override fun onCleared() {
        super.onCleared()
        viewKodelJob.cancel()
    }
}

