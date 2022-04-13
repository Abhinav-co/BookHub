package com.abhi.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.provider.Settings
import android.view.*

import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.bookhub.R
import com.abhi.bookhub.adapter.AdapterDashboardRecyclarViewList
import com.abhi.bookhub.book.Book
import com.abhi.bookhub.util.ConnectionManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.HashMap


class Dashboard : Fragment() {

    lateinit var dashboardRecycleList: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager


    lateinit var adapterRecycler: AdapterDashboardRecyclarViewList

    lateinit var progressBar: ProgressBar

    lateinit var progressLayout: RelativeLayout

    var bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book> { book1, book2 ->

        if(book1.bookRating.compareTo(book2.bookRating, true) == 0){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_dashboard,
            container,
            false
        ) //we dont want to attach fragment permanently to its core.

        dashboardRecycleList = view.findViewById(R.id.dashboardRecycleList)



        progressBar = view.findViewById(R.id.ProgressBar)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE



        layoutManager = LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().connectivityCheck(activity as Context)) {

            val jasonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")

                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")

                                )
                                bookInfoList.add(bookObject)
                                adapterRecycler = AdapterDashboardRecyclarViewList(
                                    activity as Context,
                                    bookInfoList
                                )

                                dashboardRecycleList.adapter = adapterRecycler

                                dashboardRecycleList.layoutManager = layoutManager


                            }
                        } else {
                            Toast.makeText(activity as Context, "Some Error", Toast.LENGTH_SHORT)
                                .show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "some unexpected Error ocoured!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    //handle
                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occoured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "appliction/json"
                        headers["token"] = "12fed3f7359e20"
                        return headers
                    }
                }
            queue.add(jasonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("internet connection  not found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId
        if (id == R.id.action_sort) {
            Collections.sort(bookInfoList, ratingComparator)
            bookInfoList.reverse()
        }
        adapterRecycler.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }


}