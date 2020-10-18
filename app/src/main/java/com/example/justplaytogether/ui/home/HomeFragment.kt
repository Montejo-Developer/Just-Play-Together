package com.example.justplaytogether.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.justplaytogether.ChatActivity
import com.example.justplaytogether.FriendListActivity
import com.example.justplaytogether.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var listView : ListView

    private lateinit var requestQueue : RequestQueue

    val steamKey = ""//Replace with your own steam web api key

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        var friendListActivity = activity as FriendListActivity
        listView = root.findViewById(R.id.listView)
        requestQueue = Volley.newRequestQueue(requireActivity().baseContext)

        getSteamFriendsID(friendListActivity.steamID)

        return root
    }

    private fun getSteamFriendsID(steamID : String) {

        var idList : ArrayList<String> = ArrayList()
        val postParams = JSONObject()

        val objectRequest = JsonObjectRequest(
            Request.Method.GET,  //IP for the server localhost in this case
            "http://api.steampowered.com/ISteamUser/GetFriendList/v0001/?key=$steamKey&steamid=$steamID&relationship=friend",
            postParams,
            Response.Listener { response ->
                println("RESPONSE FRIENDSID: $response")

                try {

                    var friends: JSONObject = response.getJSONObject("friendslist")
                    var friendList : JSONArray = friends.getJSONArray("friends")

                    var y = friendList.length() -1

                    //GET ALL OF THE USER FRIENDS ID
                    do {
                        val obj : JSONObject = friendList.getJSONObject(y)
                        idList.add(obj.getString("steamid"))

                        y--
                    } while (y >= 0)

                    getSteamFriends(idList)


                } catch (e: JSONException) {
                    e.printStackTrace()
                    println("Error Generating JSONObject With Post Params")
                }
            },
            Response.ErrorListener { volleyError -> println("Error: " + volleyError.message) }
        )
        requestQueue.add(objectRequest)
    }

    private fun getSteamFriends( idList : ArrayList<String>){

        var list = mutableListOf<Model>()
        var y = idList.size-1

        do {

            var id : String = idList[y]
            val postParams = JSONObject()

            val objectRequest = JsonObjectRequest(
                Request.Method.GET,  //IP for the server localhost in this case
                "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=$steamKey&steamids=$id",
                postParams,
                Response.Listener { response ->
                    println("RESPONSE FRIEND: $response")

                    try {

                        var friend: JSONObject = response.getJSONObject("response")
                        var player : JSONArray = friend.getJSONArray("players")

                        var steamPlayer : JSONObject = player.getJSONObject(0)

                        var steamState : String = steamPlayer.getString("personastate")

                        if (steamState.equals("0"))
                            steamState = "Offline"
                        else
                            steamState = "Online"

                        val steamFriend = SteamFriend(steamPlayer.getString("personaname"),
                            steamState, steamPlayer.getString("avatarfull"))

                        list.add(Model(steamFriend.name,steamFriend.state,steamFriend.Image))

                        if (list.size == idList.size-1) {

                            var adapter = MyListAdapter(requireActivity().baseContext,R.layout.row,list)
                            adapter.notifyDataSetChanged()
                            listView.requestLayout()
                            listView.adapter = adapter
                            listView.setOnItemClickListener { parent, view, position, id ->

                                val i = Intent(
                                    requireActivity().baseContext,
                                    ChatActivity::class.java
                                )
                                i.putExtra("pos" ,position)
                                startActivity(i)

                            }
                            adapter.notifyDataSetChanged()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        println("Error Generating JSONObject With Post Params")
                    }
                },
                Response.ErrorListener { volleyError -> println("Error: " + volleyError.message) }
            )

            requestQueue.add(objectRequest)

            y--

        } while (y >= 0)
    }
}
