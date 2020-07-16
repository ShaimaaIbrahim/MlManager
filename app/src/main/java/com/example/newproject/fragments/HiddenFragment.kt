package com.example.newproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newproject.MainActivity
import com.example.newproject.R
import com.example.newproject.databinding.FragmentHiddenBinding
import com.example.newproject.viewModel.AppsFragViewModel


class HiddenFragment : Fragment() {

    private lateinit var binding : FragmentHiddenBinding
    private lateinit var viewModel : AppsFragViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         binding = FragmentHiddenBinding.inflate(inflater)
         viewModel = ViewModelProviders.of(this).get(AppsFragViewModel::class.java)

        viewModel.appHiddenList.observe(this , Observer {
            Log.e("hidden" , it.size.toString())
            if (it.size==0){
                binding.notFound.visibility=View.VISIBLE
                binding.imgNot.visibility =View.VISIBLE
                binding.loading.visibility =View.GONE
            }
        })
        MainActivity.toolbar.setTitle("HiddenApps")
        return binding.root
    }


}