package com.example.justplaytogether

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

// The string will appear to the user in the login screen
// you can put your app's name
const val REALM_PARAM = "Just Play Together"

class SteamLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_steam_login)

        val webView = WebView(this)

        webView.getSettings().setJavaScriptEnabled(true)
        val activity = this
        setContentView(webView)
        // Constructing openid url request
        val url = "https://steamcommunity.com/openid/login?" +
                "openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.identity=http://specs.openid.net/auth/2.0/identifier_select&" +
                "openid.mode=checkid_setup&" +
                "openid.ns=http://specs.openid.net/auth/2.0&" +
                "openid.realm=https://" + REALM_PARAM + "&" +
                "openid.return_to=https://" + REALM_PARAM + "/signin/"

        webView.loadUrl(url)

        webView.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {

                //checks the url being loaded
                setTitle(url);
                var Url = Uri.parse(url);


                if(Url.getAuthority().equals(REALM_PARAM.toLowerCase())){
                    // That means that authentication is finished and the url contains user's id.
                    webView.stopLoading();

                    // Extracts user id.
                    var userAccountUrl = Uri.parse(Url.getQueryParameter("openid.identity"));
                    var userId = userAccountUrl.getLastPathSegment();

                    // Do whatever you want with the user's steam id
                    println(userId)
                    val i = Intent(
                        applicationContext,
                        FriendListActivity::class.java
                    )

                    apiCall(userId)
                    i.putExtra("message", userId);
                    startActivity(i)
                    finish()

                }
                setContentView(webView)

            }
        }

    }
    private fun apiCall(id : String) {
        /**
         * UPDATE USER
         */
        val postParams = JSONObject()
        try {
            postParams.put("id", 8)
            postParams.put("steamID", id)
        } catch (e: JSONException) {
            e.printStackTrace()
            println("Error Generating JSONObject With Post Params")
        }
        val requestQueue = Volley.newRequestQueue(this)

        val objectRequest = JsonObjectRequest(
            Request.Method.POST,  //IP for the server localhost in this case
            "http://192.168.1.43/android_login_api/steam.php",
            postParams,
            Response.Listener { response ->
                println("RESPONSE LOGIN: $response")
                val status = response.optString("error")
                if (status === "false") {

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
