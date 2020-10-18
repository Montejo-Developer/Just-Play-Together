package com.example.justplaytogether

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val postParams = JSONObject()
        postParams.put("id", 8)
        postParams.put("steamID", "cosas")


        val requestQueue = Volley.newRequestQueue(this)
        val objectRequest = JsonObjectRequest(
            Request.Method.POST,
            "http://192.168.1.43/android_login_api/steam.php",
            postParams,
            Response.Listener { response ->
                println("RESPONSE REGISTER: $response")},

            Response.ErrorListener { volleyError -> println("ERROR: volleyError$volleyError") }
        )
        requestQueue.add(objectRequest)
    }
}
