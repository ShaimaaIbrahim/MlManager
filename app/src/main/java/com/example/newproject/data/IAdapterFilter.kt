package com.example.newproject.data

import android.content.Context

interface IAdapterFilter {

    fun getFilter(newText : String , context : Context)

    fun noFilter(query : String , context : Context)
}