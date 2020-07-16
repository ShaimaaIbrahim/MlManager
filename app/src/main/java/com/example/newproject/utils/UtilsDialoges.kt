package com.example.newproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.MainActivity
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.MlManagerAppliciation.Companion.context
import com.example.newproject.R
import com.example.newproject.adapters.ExtractAdapter
import com.example.newproject.viewModel.AppsFragViewModel
import com.google.android.material.snackbar.Snackbar


class UtilsDialoges {

    companion object{

        public lateinit var dialog: Dialog
        public lateinit var viewModel: AppsFragViewModel
        public lateinit var lifecycleOwner: LifecycleOwner
        public  var distincitList : ArrayList<AppInfo> = ArrayList()
        public var appPreferences: AppPreferences = MlManagerAppliciation.appPreferences





          fun showDialogWithTitleAndProgress(context: Context , title : String , content : String):ProgressDialog {
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage(content); // Setting Message
            progressDialog.setTitle(title); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show()
                return progressDialog
    }

        fun showSnackbar(coordinatorLayout: CoordinatorLayout , message:String) {
            val snackbar = Snackbar
                .make(coordinatorLayout, message , Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        @SuppressLint("InflateParams")
        fun showRecycledDialog(activity: Activity, context: Context, extractedList: ArrayList<AppInfo>) {


            dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)


            val dialogeView: View = LayoutInflater.from(context).inflate(R.layout.dialoge_layout, null)

            dialog.setContentView(dialogeView)
            dialog.setCancelable(true)


              dialogeView.findViewById<RecyclerView>(R.id.recycler_view).layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)

              dialogeView.findViewById<RecyclerView>(R.id.recycler_view).adapter = ExtractAdapter(extractedList.distinct() as ArrayList<AppInfo>, activity)

                  dialog.show()

            Log.e("show" , "showed")

             dialogeView.findViewById<ImageView>(R.id.dismiss).setOnClickListener {
          //    MainActivity.refreshOptionsMenu(activity)
                 if (dialog.isShowing){
                     dialog.dismiss()

                 }
             }
            val window: Window? = dialog.window
            window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        }

   fun getDialoge() : Dialog{
       return dialog
   }



    }
}