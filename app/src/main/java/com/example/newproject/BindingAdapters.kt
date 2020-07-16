package com.example.newproject

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.adapters.AppAdapter
import com.example.newproject.utils.AppInfo
import androidx.databinding.BindingAdapter
import com.example.newproject.adapters.ExtractAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<AppInfo>?){
    val adapter = recyclerView.adapter as AppAdapter

    adapter.submitList(data)
}

@BindingAdapter("extractData")
fun bindRecyclerViewExtract(recyclerView: RecyclerView, data: List<AppInfo>?){
    val adapter = recyclerView.adapter as ExtractAdapter

    adapter.submitList(data)
}
@BindingAdapter("imageIcon")
fun bindAppName(imageView : ImageView, icon : Drawable){
   imageView.setImageDrawable(icon)
}

@BindingAdapter("appName")
fun bindAppName(textView : TextView, name :String){
    if(!name.isEmpty()){

        if (name.length > 10){
           textView.text = name.substring(0 , 10)
        }else{
            textView.text = name }
    }
}
@BindingAdapter("appVersion")
fun bindVersionName(textView : TextView, version :String){
    textView.text = version
}
@BindingAdapter("appApk")
fun bindAppApk(textView : TextView, apk :String){
    if(!apk.isEmpty()){
        if (apk.length > 10){
            textView.text = apk.substring(0 , 10)
        }else{
            textView.text = apk }
    }
    }