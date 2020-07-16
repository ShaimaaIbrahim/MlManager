package com.example.newproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newproject.MainActivity
import com.example.newproject.activities.AppInfoActivity
import com.example.newproject.adapters.AppAdapter
import com.example.newproject.databinding.FragmentAndroidBinding
import com.example.newproject.viewModel.AppsFragViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [AndroidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AndroidFragment : Fragment() {

private lateinit var binding : FragmentAndroidBinding
private lateinit var viewModel : AppsFragViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAndroidBinding.inflate(inflater)

        viewModel = ViewModelProviders.of(this).get(AppsFragViewModel::class.java)

    //    MainActivity.refreshOptionsMenu(activity!!)
        MainActivity.toolbar.setTitle("SystemApps")

        setUpRecyclerView()


    viewModel.appSystemList.observe(this , Observer {
        binding.loading.visibility = View.GONE
    })

        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        binding.recyclerView.adapter = AppAdapter(AppAdapter.OnClickListner {
            val intent = Intent(activity, AppInfoActivity::class.java)
            intent.putExtra(AppsFragment.name, it.name)
            intent.putExtra(AppsFragment.version, it.version)
            intent.putExtra(AppsFragment.icon, it.icon.toString())
            intent.putExtra(AppsFragment.apk, it.apk)
            intent.putExtra(AppsFragment.source , it.source)
            intent.putExtra(AppsFragment.data , it.data)
            intent.putExtra(AppsFragment.isSystem , it.isSystem)

            startActivity(intent)
        }, activity!!, context!!)

    }


}