package com.example.justplaytogether

import android.app.ProgressDialog
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

class RegisterActivity : AppCompatActivity() {

    private var btnRegister: Button? = null
    private var btnLinkToLogin: Button? = null
    private var inputFullName: EditText? = null
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var pDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        inputFullName = findViewById<View>(R.id.name) as EditText
        inputEmail = findViewById<View>(R.id.email) as EditText
        inputPassword = findViewById<View>(R.id.password) as EditText
        btnRegister = findViewById<View>(R.id.btnRegister) as Button
        btnRegister!!.setOnClickListener { apiCall() }
        btnLinkToLogin =
            findViewById<View>(R.id.btnLinkToLoginScreen) as Button

        // Progress dialog
        pDialog = ProgressDialog(this)
        pDialog!!.setCancelable(false)

        // Link to Login Screen
        btnLinkToLogin!!.setOnClickListener {
            val i = Intent(
                applicationContext,
                LoginActivity::class.java
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
            val name = inputFullName!!.text.toString()
            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.text.toString()
            postParams.put("name", name)
            postParams.put("email", email)
            postParams.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
            println("Error Generating JSONObject With Post Params")
        }
        val requestQueue = Volley.newRequestQueue(this)
        val objectRequest = JsonObjectRequest(
            Request.Method.POST,
            "http://192.168.1.43/android_login_api/register.php",
            postParams,
            Response.Listener { response ->
                println("RESPONSE REGISTER: $response")
                val status = response.optString("error")
            if (status === "true") {
                val toast = Toast.makeText(applicationContext,
                    "Something went wrong. \n Please check your credentials",
                    Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                val toast = Toast.makeText(applicationContext,
                    "User created successfully!",
                    Toast.LENGTH_SHORT)
                toast.show()
            }},
            Response.ErrorListener { volleyError -> println("ERROR: volleyError$volleyError") }
        )
        requestQueue.add(objectRequest)
    }

}
