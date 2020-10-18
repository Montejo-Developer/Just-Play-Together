package com.example.justplaytogether

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private val TAG = RegisterActivity::class.java.simpleName
    private var btnLogin: Button? = null
    private var btnLinkToRegister: Button? = null
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        btnLogin = findViewById<View>(R.id.btnLogin) as Button
        btnLogin!!.setOnClickListener { apiCall() }
        btnLinkToRegister =
            findViewById<View>(R.id.btnLinkToRegisterScreen) as Button
        // Link to register Screen
        btnLinkToRegister!!.setOnClickListener {
            val i = Intent(
                applicationContext,
                RegisterActivity::class.java
            )
            startActivity(i)
            finish()
        }
    }

    private fun apiCall() {
        /**
         * REGISTER USER
         */
        val postParams = JSONObject()
        try {
            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()
            postParams.put("email", email)
            postParams.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
            println("Error Generating JSONObject With Post Params")
        }
        val requestQueue = Volley.newRequestQueue(this)

        val objectRequest = JsonObjectRequest(
            Request.Method.POST,  //IP for the server localhost in this case
            "http://192.168.1.43/android_login_api/login.php",
            postParams,
            Response.Listener { response ->
                println("RESPONSE LOGIN: $response")
                val status = response.optString("error")
                if (status === "false") {
                    val i = Intent(
                        applicationContext,
                        FriendListActivity::class.java
                    )
                    i.putExtra("message", "");
                    startActivity(i)
                    finish()
                }
                else {

                    val toast = Toast.makeText(applicationContext,
                        "Something went wrong. \n Please check your credentials",
                    Toast.LENGTH_SHORT)
                    toast.show()
                }

            },
            Response.ErrorListener { volleyError -> println("Error: " + volleyError.message) }
        )
        requestQueue.add(objectRequest)
    }
}