package com.example.newproject.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*


class UtilsExtractInBackground {

companion object{

    val scope = CoroutineScope(Dispatchers.IO + Job())
    public  fun extractInBackground(context: Context, activity: Activity, progressDialog: ProgressDialog, appInfo: AppInfo){

     val coroutine=   scope.launch {
                extract(progressDialog , activity , context , appInfo)
            }

     Log.e("shaimaa" , (coroutine.isActive ).toString())
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun extract(progressDialog: ProgressDialog, activity: Activity, context: Context, appInfo: AppInfo){
        withContext(Dispatchers.IO){
            if (UtilsApp.checkPermissions(activity , context)!!){
                UtilsApp.copyFiles(appInfo)

            }else{
 //MlManager Pro
            }
        }
        if (progressDialog.isShowing){
            progressDialog.dismiss()
        }

    }
}


}