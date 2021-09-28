package com.example.a
import android.app.Application
import com.wawlabs.aisearch.Search

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Search.instance?.init("https://test.wawlabs.com/avx_wse","13541553","KNZWB-3765418567")
    }
}