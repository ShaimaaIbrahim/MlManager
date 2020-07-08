package com.example.newproject

import android.app.Application
import android.content.Context
import com.example.newproject.utils.AppPreferences

class MlManagerAppliciation  : Application() {

    companion object{
        public lateinit var appPreferences:AppPreferences
        public lateinit var context : Context
        private var isPro = false
    }


    override fun onCreate() {
        super.onCreate()

        // Load Shared Preference
        appPreferences = AppPreferences(this)
        context = this

        // Check if there is the Pro version
        isPro = this.packageName == getProPackage()

        // Register custom fonts like this (or also provide a font definition file)
      //  Iconics.registerFont(GoogleMaterial())
    }


    /**
     * Retrieve ML Manager Pro
     * @return true for ML Manager Pro, false otherwise
     */
    fun isPro(): Boolean? {
        return isPro
    }

    fun setPro(res: Boolean) {
        isPro = res
    }

    fun getProPackage(): String {
        return "com.javiersantos.mlmanagerpro"
    }

}