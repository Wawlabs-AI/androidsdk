package com.wawlabs.aisearch

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class Search {
    companion object {
        @JvmStatic
        var search: Search? = null
        val instance: Search?
            get() {
                if (search != null)
                    return search
                else {
                    search = Search()
                    return search
                }
            }
    }


    var analyticId: String? = null
    var q: String? = null
    var companyID: String? = null
    var url: String? = null
    val errorMessage = "{errorMessage = 'You are not init!!' }"

    fun init(url: String, analyticId: String, companyID: String) {
        search?.url = url+"?"
        search?.analyticId = analyticId
        search?.companyID = companyID
    }


    fun searchIo(
        context: Context,
        params: HashMap<String, String>? = null,
        paramsWithUrl: String? = null,
        listener: ((String) -> (Unit))
    ) {
        if (url.isNullOrBlank()) listener.invoke(errorMessage)
        else {
            var apiUrl = paramsWithUrl
            if (!params.isNullOrEmpty()) {
                apiUrl = createUrWithParams(url!!, params)
            }

            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, apiUrl,
                { response ->
                    val params2 = HashMap<String, String>()
                    params2.put("waw_keyword", q ?: "")
                    params2.put("uid", companyID ?: "")
                    params2.put("ga", analyticId ?: "")
                    params2.put("device_os", "android")
                    sendAnalytic(context,params2)
                    listener.invoke(response)
                },
                {
                    listener.invoke(it.message ?: "")

                }
            )
            queue.add(stringRequest)
        }
    }


    fun sendClick(
        context: Context,
        productId: String?,
        listener: ((String) -> (Unit))?
    ) {
        val params2 = HashMap<String, String>()
        params2.put("waw_keyword", q ?: "")
        params2.put("uid", companyID ?: "")
        params2.put("product_id", productId ?: "")
        params2.put("device_os", "Android")
        sendAnalytic(context,params2,listener)
    }

    fun sendAnalytic(
        context: Context,
        params: HashMap<String, String>,
        listener: ((String) -> (Unit))? = null
    ) {

        val queue = Volley.newRequestQueue(context)
        val url = "https://wa.wawlabs.com"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                listener?.invoke(response)
            },
            Response.ErrorListener {
                listener?.invoke(it.message ?: "")
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return JSONObject(params as Map<*, *>).toString().toByteArray()
            }
        }
        queue.add(stringRequest)
    }

    fun createUrWithParams(url: String, params: HashMap<String, String>): String {
        var cacheUrl = url
        params.forEach { (key, value) ->
            val currentKey = key.toLowerCase(Locale.ROOT)
            if (currentKey.equals("q")) q = value
            cacheUrl = cacheUrl + "$currentKey=$value&"
        }
        return cacheUrl
    }
}

