package com.example.newproject.adapters

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.MainActivity
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.R
import com.example.newproject.activities.AppInfoActivity
import com.example.newproject.databinding.AppLayoutBinding
import com.example.newproject.utils.AppInfo
import com.example.newproject.utils.UtilsApp
import com.example.newproject.utils.UtilsDialoges
import com.example.newproject.utils.UtilsExtractInBackground
import com.example.newproject.viewModel.AppsFragViewModel
import kotlinx.android.synthetic.main.app_layout.view.*

class AppAdapter(private val onClickListener: OnClickListner, private val activity: Activity?, private val context: Context ): ListAdapter<AppInfo , AppAdapter.AppViewHolder>(DiffCallback) {

    val intent: Intent = Intent(MlManagerAppliciation.context, AppInfoActivity::class.java)
    private lateinit var imageView : ImageView

    val transitionName: String =
            MlManagerAppliciation.context.getString(R.string.transition_app_icon)

    class AppViewHolder(private val apInfoBinding: AppLayoutBinding) : RecyclerView.ViewHolder(apInfoBinding.root) {

        fun bind(appInfo: AppInfo) {
            apInfoBinding.appInfo = appInfo
            apInfoBinding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {

        return AppViewHolder(AppLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        var appInfo = getItem(position)
        holder.bind(appInfo)

        imageView = holder.itemView.imgIcon

        //click on item
        holder.itemView.setOnClickListener { onClickListener.onClick(appInfo) }
        //clicking on extract btn
        clickExtract(holder, appInfo)
        //clicking on share btn
        clickShare(holder, appInfo)

    }

    private fun clickShare(holder: AppViewHolder, appInfo: AppInfo) {
        holder.itemView.btnShare.setOnClickListener {
            UtilsApp.copyFiles(appInfo)
            val shareIntent: Intent =
                    UtilsApp.getShareIntent(UtilsApp.getOutputFilename(appInfo), context)
            context.startActivity(Intent.createChooser(shareIntent, String.format(context.resources.getString(R.string.send_to), appInfo.name)))
        }
    }

    private fun clickExtract(holder: AppViewHolder, appInfo: AppInfo) {
        holder.itemView.btnExtract.setOnClickListener {
            var dialog: ProgressDialog = UtilsDialoges.showDialogWithTitleAndProgress(
                    context,
                    java.lang.String.format(context.resources.getString(R.string.dialog_saving), appInfo.name
                    ), context.resources.getString(R.string.dialog_saving_description))
            UtilsExtractInBackground.extractInBackground(context, activity!!, dialog, appInfo)

         MainActivity.refreshOptionsMenu(activity as  MainActivity)
        }

    }


    companion object DiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.apk == newItem.apk
        }
    }

    class OnClickListner(val clickListner: (appInfo: AppInfo ) -> Unit) {
        fun onClick(appInfo: AppInfo ) = clickListner(appInfo )
    }


    }




