package com.example.newproject.adapters


import androidx.recyclerview.widget.DiffUtil
import com.example.newproject.utils.AppInfo

class FilterDiffUtilsCallback(private val oldList: List<AppInfo>, private val newList: List<AppInfo>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].apk == newList[newItemPosition].apk

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = true
}