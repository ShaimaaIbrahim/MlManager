package com.example.newproject.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.newproject.MainActivity
import com.example.newproject.MlManagerAppliciation
import com.example.newproject.MlManagerAppliciation.Companion.appPreferences
import com.example.newproject.MlManagerAppliciation.Companion.context
import com.example.newproject.R
import com.example.newproject.databinding.ActivityAppInfoBinding
import com.example.newproject.fragments.AppsFragment
import com.example.newproject.utils.*
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.scroll_layout.view.*
import java.io.File


class AppInfoActivity : AppCompatActivity() {

    private val REQUEST_PROVISION_MANAGED_PROFILE: Int = 99
    private val UNINSTALL_REQUEST_CODE: Int = 1

    private lateinit var binding: ActivityAppInfoBinding
    private lateinit var menu: Menu
    public lateinit var name: String
    private lateinit var version: String
    private lateinit var icon: Drawable
    private lateinit var apk: String
    private lateinit var data: String
    public lateinit var source: String
    public lateinit var appInfo: AppInfo
    public var isSystem: Boolean = false


    val ENABLE_ADMIN_REQUEST_CODE = 100


    companion object {

        public lateinit var toolbar: Toolbar
        public lateinit var linearLayout: LinearLayout
        public lateinit var window1: Window



    }

    private lateinit var favoriteSet: HashSet<String>
    private lateinit var appsHidden: HashSet<String>
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_info)


        setSupportActionBar(binding.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);

            window1 = window as Window
            toolbar = binding.toolbar as Toolbar
            linearLayout = binding.containerLin



        val extras = intent.extras
        if (extras != null) {
            apk = extras.getString(AppsFragment.apk)!!
            name = extras.getString(AppsFragment.name)!!
            version = extras.getString(AppsFragment.version)!!
            icon = this.packageManager.getApplicationIcon(apk)
             source = extras.getString(AppsFragment.source)!!
             data = extras.getString(AppsFragment.data)!!
            isSystem = extras.getBoolean(AppsFragment.isSystem)



            appInfo = AppInfo(name, apk, version, "jj", "k", icon, isSystem)
            Log.e("halo1", icon.toString())


        }
        //setInitial()
        setInitialConfigurations()

        binding.profileImage.setImageDrawable(icon)
        binding.scrollLayout.open.setOnClickListener {
            try {
                val intent = packageManager.getLaunchIntentForPackage(apk)
                startActivity(intent)
            } catch (e: NullPointerException) {
                e.printStackTrace()
                UtilsDialoges.showSnackbar(binding.container , resources.getString(R.string.dialog_cannot_open))
            }
        }

       binding.scrollLayout.size_bytes.text = "it is ${getFolderSizeInMB(UtilsApp.getOutputFilename(appInfo).toString())} MB "
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
        }

        /**
         * set favorite apps
         */
        favorite()

         binding.scrollLayout.google_play.setOnClickListener{

            if(!isSystem){

                 val clipData: ClipData
                 val clipboardManager =
                  context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                 clipData = ClipData.newPlainText("text", appInfo.apk)
                 clipboardManager.primaryClip = clipData
                 UtilsDialoges.showSnackbar(binding.container,
                     context.resources.getString(R.string.copied_clipboard))
                goToGooglePlay(this , apk)
            }

         }

        binding.scrollLayout.hide.setOnClickListener(View.OnClickListener {

            if(UtilsRoot.isRooted()){

                if (UtilsApp.isAppHidden(appInfo, appsHidden)!!) {
                    val hidden = UtilsRoot.hideWithRootPermission(appInfo.apk, true)
                    if (hidden) {
                   //     UtilsApp.removeIconFromCache(context, appInfo)
                        appsHidden.remove(appInfo.toString())
                        appPreferences.setHiddenApps(appsHidden)
                    //    UtilsDialog.showSnackbar(activity, resources.getString(R.string.dialog_reboot), resources.getString(R.string.button_reboot), null, 3).show()
                    }
                } else {
                //    UtilsApp.saveIconToCache(context, appInfo)
                    val hidden = UtilsRoot.hideWithRootPermission(appInfo.apk, false)
                    if (hidden) {
                        appsHidden.add(appInfo.toString())
                        appPreferences.setHiddenApps(appsHidden)
                    }
                }
                UtilsApp.setAppHidden(context, binding.scrollLayout.hide, UtilsApp.isAppHidden(appInfo, appsHidden)!!)
            }

        })
    }

    private fun favorite() {
        binding.scrollLayout.favorites.setOnClickListener {

            if (UtilsApp.isAppFavorite(appInfo, favoriteSet)!!) {
                UtilsApp.setAppFavorite(this, binding.scrollLayout.favorites, false)
                favoriteSet.remove(apk)
                appPreferences.setFavoriteApps(favoriteSet)
            } else {
                UtilsApp.setAppFavorite(this, binding.scrollLayout.favorites, true)
                favoriteSet.add(apk)
                appPreferences.setFavoriteApps(favoriteSet)
            }
        }
    }


    private fun setInitialConfigurations() {

        favoriteSet =
                MlManagerAppliciation.appPreferences.getFavoriteApps() as HashSet<String>
        appsHidden = MlManagerAppliciation.appPreferences.getHiddenApps() as HashSet<String>

        binding.name.text = name
        //   binding.profileImage.setImageDrawable(convertStringToDrawable(icon))
        binding.version.text = version

        /**
        * set initial configuration
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppInfoActivity.window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            AppInfoActivity.window1.statusBarColor =
                UtilsUi.darker(MlManagerAppliciation.appPreferences.getPrimaryColor(), 0.8)
            AppInfoActivity.toolbar.setBackgroundColor(MlManagerAppliciation.appPreferences.getPrimaryColor())
            AppInfoActivity.linearLayout.setBackgroundColor(MlManagerAppliciation.appPreferences.getPrimaryColor())
        }

        /**
         * check if apps favorites or not
         */
        if (UtilsApp.isAppFavorite(appInfo, favoriteSet)!!) {
            UtilsApp.setAppFavorite(this, binding.scrollLayout.favorites, true)
        } else {
            UtilsApp.setAppFavorite(this, binding.scrollLayout.favorites, false)
        }
        /**
         * check if apps hidden or not
         */
        if (UtilsApp.isAppHidden(appInfo, appsHidden)!!) {
            UtilsApp.setAppHidden(this, binding.scrollLayout.hide, true)
        } else {
            UtilsApp.setAppHidden(this, binding.scrollLayout.hide, false)
        }


    }


    private fun Uninstall() {
        val intent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        intent.data = Uri.parse("package:" + apk)
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        startActivityForResult(intent, UNINSTALL_REQUEST_CODE)
    }

    private fun convertStringToDrawable(icon: String): Drawable {
        val id = resources.getIdentifier(icon, "drawable", packageName)
        val drawable = resources.getDrawable(id)
        return drawable
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
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

    private fun showMenuShare() {
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
        if (requestCode == ENABLE_ADMIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("shaimaa", "OK")
                Toast.makeText(this, "Provisioning done.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Provisioning failed.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    fun getFolderSizeInMB(directory: String?): Long {
        val f = File(directory)
        var size: Long = 0
        if (f.isDirectory) {
            for (file in f.listFiles()) {
                size += getFolderSizeInMB(file.absolutePath)
            }
        } else {
            size = f.length() / 1024 / 2024
        }
        return size
    }

    fun goToGooglePlay(context: Context, id: String) {
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$id")))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$id")))
        }
    }
}