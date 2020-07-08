package com.example.newproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.onNavDestinationSelected
import com.example.newproject.activities.SettingActivity
import com.example.newproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    companion object{
        private const val TAG = "MainActivity"
        public lateinit var toolbar: Toolbar

    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        toolbar = binding.toolbar as Toolbar
        setSupportActionBar(toolbar)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
       // menuInflater.inflate(R.menu.bottom_app_bar , menu)
       menuInflater.inflate(R.menu.search , menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val res : Int = item.itemId
        when(res){
            R.id.app_bar_search -> Toast.makeText(this , "Search " , Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
      navController.popBackStack()
        finish()
    }
}