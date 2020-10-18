package com.example.justplaytogether

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject


class FriendListActivity : AppCompatActivity() {

    var steamID = ""

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        var bundle = intent.extras

        steamID = bundle.getString("message")

        if (steamID != "")
            apiCall(steamID)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
                println("RESPONSE UPDATE: $response")
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.friend_list, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
