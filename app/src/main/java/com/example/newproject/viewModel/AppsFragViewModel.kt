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
import com.example.newproject.MainActivity
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.R
import com.example.newproject.activities.AppInfoActivity
import com.example.newproject.utils.AppInfo
import com.example.newproject.utils.AppPreferences
import io.reactivex.Completable
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class AppsFragViewModel( application: Application) : AndroidViewModel(application) {


    private val packageManager: PackageManager = application.packageManager
    private val context = application


    companion object{

        private lateinit var appPreferences: AppPreferences

        private  lateinit  var systemApps: ArrayList<AppInfo>
        private lateinit  var allApps : ArrayList<AppInfo>
        private lateinit var favoriteList : ArrayList<AppInfo>
        private lateinit var extractedList : ArrayList<AppInfo>
        private lateinit var installedApps : ArrayList<AppInfo>
        private lateinit var hiddenList : ArrayList<AppInfo>
    }

    val viewKodelJob = Job()
    val scopeCoroutines = CoroutineScope(Dispatchers.IO + viewKodelJob)


    // for filter LIst
    val filteredApps: MutableList<AppInfo> = mutableListOf()
    val oldFilteredApps: MutableList<AppInfo> = mutableListOf()

    //liveDAta
    val _appList = MutableLiveData<List<AppInfo>>()
    val appList : LiveData<List<AppInfo>>
    get()= _appList

    private val _appSystemList = MutableLiveData<List<AppInfo>>()
    val appSystemList : LiveData<List<AppInfo>>
        get()= _appSystemList

    private val _appHiddenList = MutableLiveData<List<AppInfo>>()
    val appHiddenList : LiveData<List<AppInfo>>
        get()= _appHiddenList


    private val _appFavoriteList = MutableLiveData<List<AppInfo>>()
    val appFavoriteList : LiveData<List<AppInfo>>
        get()= _appFavoriteList

    public val _appExtractList = MutableLiveData<List<AppInfo>>()
    val appExtractList : LiveData<List<AppInfo>>
        get()= _appExtractList


init {

     getInstalledApps()
     appPreferences = MlManagerAppliciation.appPreferences
    installedApps= ArrayList()
     systemApps = ArrayList()
     allApps = ArrayList()
     favoriteList = ArrayList()
     extractedList = ArrayList()
    hiddenList = ArrayList()
     oldFilteredApps.addAll(installedApps)

    _appList.value = oldFilteredApps

}

   public fun getInstalledApps(){
      val ss = scopeCoroutines.async{
        getInstalledAndSortedApps()
      }
Log.e("nomi" , ss.toString())
    }

    @SuppressLint("QueryPermissionsNeeded")
    private suspend fun getInstalledAndSortedApps() {
      installedApps.clear()
      systemApps.clear()
      allApps.clear()
      extractedList.clear()
      favoriteList.clear()

        var packages: List<PackageInfo> = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

      withContext(Dispatchers.IO) {

          when (appPreferences.getSortMode()) {

              "2" -> Collections.sort(packages, object : Comparator<PackageInfo?> {
                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      val size1 = File(p1!!.applicationInfo.sourceDir).length()
                      val size2 = File(p2!!.applicationInfo.sourceDir).length()
                      return size2.compareTo(size1)
                  }
              })
              "3" -> Collections.sort(packages, object : Comparator<PackageInfo?> {
                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      return java.lang.Long.toString(p2!!.firstInstallTime)
                              .compareTo(java.lang.Long.toString(p1!!.firstInstallTime))
                  }
              })
              "4" -> Collections.sort(packages, object : Comparator<PackageInfo> {
                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      return java.lang.Long.toString(p2!!.lastUpdateTime)
                              .compareTo(java.lang.Long.toString(p1!!.lastUpdateTime))
                  }

              })
              else -> Collections.sort(packages, object : Comparator<PackageInfo?> {

                  override fun compare(p1: PackageInfo?, p2: PackageInfo?): Int {
                      return packageManager.getApplicationLabel(p1!!.applicationInfo).toString()
                              .toLowerCase().compareTo(
                                      packageManager.getApplicationLabel(p2!!.applicationInfo).toString()
                                              .toLowerCase())
                  } }) } }
        withContext(Dispatchers.Default) {
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
                            allApps.add(tempApp)
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
                            allApps.add(tempApp)
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
                            allApps.add(tempApp)
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
                            allApps.add(tempApp)
                            Log.e("shaimaa" ,tempApp.apk )
                            //    _appSystemList.value = systemApps

                        } catch (e : Exception ) {
                            e.printStackTrace()
                        } } } }

            appPreferences.getFavoriteApps()!!.forEach {
                for (x in 0 until allApps.size) {
                    if (it.equals(allApps.get(x).apk) && allApps.get(x).apk != null) {
                        favoriteList.add(allApps.get(x))
                    }
                }
            }
            appPreferences.getExtractedApps().forEach {
                for (x in 0 until allApps.size){
                    if (it.equals(allApps.get(x).apk)){
                        extractedList.add(allApps.get(x))
                    } } }
            appPreferences.getHiddenApps().forEach {
                for (x in 0 until allApps.size){
                    if (it.equals(allApps.get(x).apk)){
                        hiddenList.add(allApps.get(x))
                    } } }
        }
        withContext(Dispatchers.Main){
            _appList.value = installedApps.toList()
            _appSystemList.value =  systemApps.toList()
            _appFavoriteList.value = favoriteList.toList()
            _appExtractList.value = extractedList.distinct().toList()
            _appHiddenList.value = hiddenList.toList()
          //  _isLoaded.postValue(true)

        }

    }
    //for searching
    fun search(query: String): Completable = Completable.create {
        val wanted = installedApps.filter {
            it.name.toLowerCase().contains(query) || it.name.toLowerCase().contains(query)
        }.toList()

        filteredApps.clear()
        filteredApps.addAll(wanted)
        it.onComplete()
    }

    override fun onCleared() {
        super.onCleared()
        viewKodelJob.cancel()
    }
}

