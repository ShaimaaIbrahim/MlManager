package com.example.newproject.fragments


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import com.example.newproject.MainActivity
import com.example.newproject.R
import com.example.newproject.activities.AppInfoActivity
import com.example.newproject.adapters.AppAdapter
import com.example.newproject.adapters.FilterDiffUtilsCallback
import com.example.newproject.data.IAdapterFilter
import com.example.newproject.databinding.FragmentAppsBinding
import com.example.newproject.viewModel.AppsFragViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers


class AppsFragment : Fragment() ,IAdapterFilter {

    private val disposable = CompositeDisposable()

    companion object{

        public val name : String = "name"
        public val version : String ="version"
        public val icon : String = "icon"
        public val apk : String = "apk"
        public val data : String = "data"
        public val isSystem : String = "isSystem"
        public var source : String ="source"
        public lateinit var binding: FragmentAppsBinding
        public lateinit var viewModel: AppsFragViewModel
        public lateinit var adapter: AppAdapter
        public lateinit var iAdapterFilter: IAdapterFilter


    }


     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
       //   MainActivity.refreshOptionsMenu(activity!!)
     }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAppsBinding.inflate(inflater)

        viewModel = ViewModelProviders.of(this).get(AppsFragViewModel::class.java)


        MainActivity.setFilterList(this)
        //MainActivity.refreshOptionsMenu(activity!!)


        setupRecyclerView()

        viewModel.appList.observe(this , Observer {
            if (it.size>=1){
                binding.loading.visibility= View.GONE
            }
        })


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
        val intent = Intent(activity, AppInfoActivity::class.java)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        binding.recyclerView.adapter = AppAdapter(AppAdapter.OnClickListner {
            val intent = Intent(activity, AppInfoActivity::class.java)
            intent.putExtra(AppsFragment.name, it.name)
            intent.putExtra(AppsFragment.version, it.version)
            intent.putExtra(AppsFragment.icon, it.icon.toString())
            intent.putExtra(AppsFragment.apk , it.apk)
            intent.putExtra(AppsFragment.data, it.data)
            intent.putExtra(AppsFragment.source, it.source)
            intent.putExtra(AppsFragment.isSystem, it.isSystem)

            startActivity(intent)
      }, activity!! , context!! )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val transitionName = context!!.resources.getString(R.string.transition_app_icon)
        //    val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, appIcon, transitionName)
        //    context!!.startActivity(intent, transitionActivityOptions.toBundle())
        } else {
            context!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back)
        }
    }

     @SuppressLint("CheckResult")
     override fun getFilter(newText: String, context: Context) {

         Toast.makeText(context , newText , Toast.LENGTH_LONG).show()

             }


     override fun noFilter(query: String, context: Context) {
         Toast.makeText(context , query , Toast.LENGTH_LONG).show()
         viewModel
             .search(query.toString())
             .subscribeOn(Schedulers.computation())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe {
                 val diffResult = DiffUtil.calculateDiff(FilterDiffUtilsCallback(viewModel.oldFilteredApps, viewModel.filteredApps))
                 viewModel.oldFilteredApps.clear()
                 viewModel.oldFilteredApps.addAll(viewModel.filteredApps)
                 viewModel._appList.value = viewModel.oldFilteredApps
                 diffResult.dispatchUpdatesTo(binding.recyclerView.adapter!!)
             }.addTo(disposable)
       //  setupRecyclerView()
     }


 }

