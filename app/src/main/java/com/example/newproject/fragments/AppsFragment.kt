package com.example.newproject.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.newproject.MainActivity
import com.example.newproject.activities.AppInfoActivity
import com.example.newproject.adapters.AppAdapter
import com.example.newproject.databinding.FragmentAppsBinding
import com.example.newproject.viewModel.AppsFragViewModel


class AppsFragment : Fragment() {

    companion object{
   public val name : String = "name"
        public val version : String ="version"
        public val icon : String = "icon"
        public val apk : String = "apk"
    }
    private lateinit var binding: FragmentAppsBinding
    private lateinit var viewModel: AppsFragViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       binding = FragmentAppsBinding.inflate(inflater)

       viewModel = ViewModelProviders.of(this).get(AppsFragViewModel::class.java)

        setupRecyclerView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MainActivity.toolbar.title = "InstalledApps"
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
/*
 <com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
        android:id="@+id/recycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:fastScrollPopupBgColor="?colorAccent"
        app:fastScrollPopupTextColor="@android:color/primary_text_dark"
        app:fastScrollThumbColor="?colorAccent" />
 */
    private fun setupRecyclerView() {

        binding.setLifecycleOwner (this)

        binding.viewModel=viewModel

       binding.recyclerView.adapter = AppAdapter(AppAdapter.OnClickListner{
           it ->
           val intent = Intent(activity, AppInfoActivity::class.java)
           intent.putExtra(name , it.name)
           intent.putExtra(version , it.version)
           intent.putExtra(apk , it.apk)
           intent.putExtra(icon , it.icon.toString())
           startActivity(intent)

       } , activity!! , context!!)
    }


}

