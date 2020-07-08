package com.example.newproject.utils

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import com.example.newproject.activities.AppInfoActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make as make1

class UtilsDialoges {

    companion object{

          fun showDialogWithTitleAndProgress(context: Context , title : String , content : String):ProgressDialog {
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage(content); // Setting Message
            progressDialog.setTitle(title); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show()
                return progressDialog
    }

        fun showSnackbar(context: Context , appInfoActivity: AppInfoActivity, format: String, nothing: Nothing?, nothing1: Nothing?, i: Int) {
        Toast.makeText(context , format , Toast.LENGTH_LONG ).show()
        }


    }
}