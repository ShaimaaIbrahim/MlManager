package com.example.newproject


import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import com.example.newproject.MlManagerAppliciation.Companion.context
import com.example.newproject.activities.SettingActivity
import com.example.newproject.adapters.AppAdapter
import com.example.newproject.data.IAdapterFilter
import com.example.newproject.databinding.ActivityMainBinding
import com.example.newproject.fragments.AppsFragment
import com.example.newproject.utils.*
import com.example.newproject.viewModel.AppsFragViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.nikartm.support.ImageBadgeView
import java.security.PrivateKey


/**
 * created by shaimaa salama
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appPreferences: AppPreferences
    public var count : Int =0
    private lateinit var searchView: SearchView
    private lateinit var viewModel : AppsFragViewModel



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        // menuInflater.inflate(R.menu.bottom_app_bar , menu)
        menuInflater.inflate(R.menu.up_app_bar , menu)

        val menuItem = menu!!.findItem(R.id.apks_count) as MenuItem
        val actionView = menuItem.actionView

        actionView.findViewById<ImageBadgeView>(R.id.cart_menu_icon).badgeValue = appPreferences.getExtractedApps().size
        viewModel.getInstalledApps()

            actionView.setOnClickListener {
                Log.e("show" , "clicked")
                onOptionsItemSelected(menuItem)
                actionView.findViewById<ImageBadgeView>(R.id.cart_menu_icon).badgeValue = appPreferences.getExtractedApps().size

            }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo( searchManager.getSearchableInfo(componentName))
        searchView.setMaxWidth(Int.MAX_VALUE)
        //   searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                iAdapterFilter.getFilter(query , context)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                iAdapterFilter.noFilter(newText , context)

                return true
            } })
        return super.onCreateOptionsMenu(menu)
    }


    companion object{

        private const val TAG = "MainActivity"
        public lateinit var toolbar: Toolbar
        public lateinit var bottomAppBar: BottomAppBar
        public lateinit var window1: Window
        public lateinit var fab:FloatingActionButton
        public  var appsExtracted:ArrayList<AppInfo> = UtilsApp.appsExtracted
        private lateinit var iAdapterFilter: IAdapterFilter


        public fun setFilterList(iAdapterFilter: IAdapterFilter){
            this.iAdapterFilter= iAdapterFilter
        }

        fun refreshOptionsMenu(activity: Activity){
            activity.invalidateOptionsMenu()

        }

   fun setDarkMode(){
       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
   }

        fun disableDarkMode(){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        fun setAppExtracted(appsExtracted: ArrayList<AppInfo>) {
         this.appsExtracted = appsExtracted
        }
    }

     @SuppressLint("SetTextI18n")
     fun setInitialConfiguration() {

         setTheme(R.style.Theme_NewProject)


      //   MainActivity.refreshOptionsMenu(this)
         toolbar = binding.toolbar as Toolbar
         binding.toolbar.title = "InstalledApps"
         window1 = window as Window
         bottomAppBar = binding.bottomAppBar
         fab = binding.fab
         setSupportActionBar(toolbar)


         if (appPreferences.getNightMode()){
             MainActivity.setDarkMode()
         }else{
             disableDarkMode()
         }
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 MainActivity.window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                 MainActivity.window1.statusBarColor = UtilsUi.darker(MlManagerAppliciation.appPreferences.getPrimaryColor(), 0.8)
                 MainActivity.toolbar.setBackgroundColor(MlManagerAppliciation.appPreferences.getPrimaryColor())
                 MainActivity.bottomAppBar.setBackgroundColor(appPreferences.getPrimaryColor())

                 MainActivity.fab.setBackgroundColor(appPreferences.getPrimaryColor())
             }
         }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appPreferences = MlManagerAppliciation.appPreferences

        viewModel = ViewModelProviders.of(this).get(AppsFragViewModel::class.java)

        /**
         * set initial configuration
         */


        if (appPreferences.getNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        setInitialConfiguration()

        binding.bottomAppBar.setNavigationOnClickListener {
            navController.navigate(R.id.favoritesFragment)
        }

        binding.bottomAppBar.setOnMenuItemClickListener {  menuItem ->
            menuItem.onNavDestinationSelected(navController)
        }

        binding.fab.setOnClickListener {
           startActivity(Intent(this , SettingActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        val res : Int = item.itemId
       if (res==R.id.app_bar_search) {
           return true
       }
       else if(res==R.id.apks_count){
           viewModel.appExtractList.observe(this , Observer {
               Log.e("show" , "observes")
               UtilsDialoges.showRecycledDialog(this , context ,it.distinct() as ArrayList<AppInfo>)

           })
           return true
       }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
      navController.popBackStack()
      UtilsDialoges.dialog.dismiss()
        Log.e("show" , "dismiss")
        finish()
    }


}
