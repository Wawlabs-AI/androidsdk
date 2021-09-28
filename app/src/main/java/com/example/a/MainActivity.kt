package com.example.a


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wawlabs.aisearch.Search


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.text)
        val text2 = findViewById<TextView>(R.id.text2)
        val map = hashMapOf("q" to "iphone", "d" to "True", "f" to "True")
        Search.instance?.searchIo(this,params =  map) {
            Log.e("Test searchIo response", it)
            text.text = it

        }
      /*

       Search.instance?.searchIo(this,paramsWithUrl =  "https://test.wawlabs.com/awx_vse?q=iphone&d=True&f=True") {
            Log.e("Test response", it)
            text.text = it

        }

        */
        Search.instance?.sendClick(this,"123456"){
            Log.e("Test sendClick response", it)
            text2.text = it
        }
    }
}