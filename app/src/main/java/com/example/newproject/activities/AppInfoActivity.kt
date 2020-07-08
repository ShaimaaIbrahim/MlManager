package com.example.newproject.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.newproject.MainActivity
import com.example.newproject.R
import com.example.newproject.databinding.ActivityAppInfoBinding
import com.example.newproject.fragments.AppsFragment
import com.example.newproject.utils.AppInfo
import com.example.newproject.utils.UtilsDialoges
import com.example.newproject.utils.UtilsExtractInBackground
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.scroll_layout.view.*


class AppInfoActivity : AppCompatActivity() {

    private val UNINSTALL_REQUEST_CODE: Int = 1
    private lateinit var binding : ActivityAppInfoBinding
    private lateinit var menu : Menu
    public lateinit var name : String
    private lateinit var version : String
    private lateinit var icon: String
    private lateinit var apk : String

    override fun onStart() {
        super.onStart()
        binding.toolbar.setTitle(" ")
    }

    override fun onPause() {
        super.onPause()
        binding.toolbar.setTitle(" ")
    }

    override fun onStop() {
        super.onStop()
        binding.toolbar.setTitle(" ")
    }

    override fun onRestart() {
        super.onRestart()
        binding.toolbar.setTitle(" ")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      binding = DataBindingUtil.setContentView(this , R.layout.activity_app_info)

        setSupportActionBar(binding.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);

        binding.toolbar.setTitle(" ")

        val extras = intent.extras
        if (extras != null) {
             name = extras.getString(AppsFragment.name)!!
             version = extras.getString(AppsFragment.version)!!
             icon = extras.getString(AppsFragment.icon)!!
             apk = extras.getString(AppsFragment.apk)!!
        }
        binding.name.text = name
     //   binding.profileImage.setImageDrawable(convertStringToDrawable(icon))
        binding.emailId.text = version

        binding.scrollLayout.open.setOnClickListener {
            try {
                val intent = packageManager.getLaunchIntentForPackage(apk)
                startActivity(intent)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                UtilsDialoges.showSnackbar( this , this , String.format(
                    resources.getString(R.string.dialog_cannot_open),
                        name ), null, null, 2)
            } }


        binding.scrollLayout.delete.setOnClickListener {
            Uninstall()
        }

        binding.scrollLayout.extract.setOnClickListener {
            var dialog: ProgressDialog = UtilsDialoges.showDialogWithTitleAndProgress(
                this,
                java.lang.String.format(
                    resources.getString(R.string.dialog_saving),
                    name
                ), resources.getString(R.string.dialog_saving_description)
            )
          /*  var appInfo: AppInfo = AppInfo(name , apk , version , "" , "data" , Drawable R.drawable.ic_add_24dp)
            UtilsExtractInBackground.extractInBackground(this, this, dialog, appInfo)*/

        } }

    private fun Uninstall() {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        intent.data = Uri.parse("package:" + apk)
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        startActivityForResult(intent, UNINSTALL_REQUEST_CODE)
    }

    private fun convertStringToDrawable(icon : String):Drawable {
        val id = resources.getIdentifier(icon, "drawable", packageName)
        val drawable = resources.getDrawable(id)
        return drawable
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu , menu)
        this.menu = menu!!
        showMenuShare()
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.share) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showMenuShare(){
       binding.appBar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val menuItem: MenuItem = menu.findItem(R.id.share)
            if (Math.abs(verticalOffset) == appBarLayout.totalScrollRange) {
                menuItem.isVisible = true
                binding.toolbar.setTitle("hello")
            } else if (verticalOffset == 0) {
                menuItem.isVisible = false
                binding.toolbar.setTitle("")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("App", "OK")
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                finish()
                startActivity(intent)
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("App", "CANCEL")
            }
        }
    }
}