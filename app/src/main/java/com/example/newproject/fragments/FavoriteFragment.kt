package com.example.newproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newproject.MainActivity
import com.example.newproject.activities.AppInfoActivity
import com.example.newproject.adapters.AppAdapter
import com.example.newproject.databinding.FragmentFavoriteBinding
import com.example.newproject.viewModel.AppsFragViewModel


class FavoriteFragment : Fragment() {

    lateinit var viewModel: AppsFragViewModel
    lateinit  var binding: FragmentFavoriteBinding
    lateinit var owner : LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProviders.of(this).get(AppsFragViewModel::class.java)
        binding = FragmentFavoriteBinding.inflate(inflater)
        owner = this

        setupRecyclerView()
         viewModel.appFavoriteList.observe(this , Observer {
            if (it.size>=1){
               binding.loading.visibility= View.GONE
            }
        })
        MainActivity.toolbar.setTitle("FavoriteApps")
       return binding.root
    }

        public fun setupRecyclerView() {

            binding.setLifecycleOwner(owner)

            binding.viewModel=viewModel

            binding.recyclerView.adapter = AppAdapter(AppAdapter.OnClickListner{

                val intent = Intent(activity!!, AppInfoActivity::class.java)
                intent.putExtra(AppsFragment.name , it.name)
                intent.putExtra(AppsFragment.version , it.version)
                intent.putExtra(AppsFragment.apk , it.apk)
                Log.e("halo" , it.icon.toString())
                intent.putExtra(AppsFragment.data , it.data)
                intent.putExtra(AppsFragment.isSystem , it.isSystem)
                intent.putExtra(AppsFragment.source , it.source)

                context!!.startActivity(intent)

            } , activity!! , context!! )
        }
    }

