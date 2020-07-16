package com.example.newproject.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.MainActivity
import com.example.newproject.databinding.ExtractedLayoutBinding
import com.example.newproject.utils.AppInfo
import com.example.newproject.utils.UtilsApp
import kotlinx.android.synthetic.main.extracted_layout.view.*

class ExtractAdapter (private val extractedList : ArrayList<AppInfo> , private var activity: Activity): ListAdapter<AppInfo , ExtractAdapter.extractViewHolder>(DiffCallback){


    class extractViewHolder(private val extractedLayoutBinding:ExtractedLayoutBinding) :
        RecyclerView.ViewHolder(extractedLayoutBinding.root){

        fun bind(appInfo: AppInfo){
             extractedLayoutBinding.extract=appInfo
             extractedLayoutBinding.executePendingBindings()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): extractViewHolder {
        return extractViewHolder(ExtractedLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: extractViewHolder, position: Int) {

        var appInfo = extractedList.get(position)
        holder.bind(appInfo)

      holder.itemView.img_delete.setOnClickListener {
      UtilsApp.deleteExtractedApp(appInfo)

      extractedList.remove(appInfo)
      notifyItemRemoved(position)
      notifyItemRangeRemoved(position , extractedList.size)
      holder.itemView.visibility=View.GONE

    //  MainActivity.refreshOptionsMenu(activity)
 }

    }

    override fun getItemCount(): Int {
        return extractedList.size
    }

}
