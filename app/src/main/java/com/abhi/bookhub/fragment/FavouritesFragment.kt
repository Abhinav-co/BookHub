package com.abhi.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.abhi.bookhub.R
import com.abhi.bookhub.adapter.AdapterFavourites
import com.abhi.bookhub.database.BookDatabase
import com.abhi.bookhub.database.BookEntity

class Favourites : Fragment() {

    lateinit var recyclarFavourites:RecyclerView
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManger:RecyclerView.LayoutManager
    lateinit var recyclarAdapter:AdapterFavourites
    var dbBookList= listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourites, container, false)

        progressBar=view.findViewById(R.id.ProgressBar)
        progressLayout=view.findViewById(R.id.progressLayout)
        recyclarFavourites=view.findViewById(R.id.recyclarFavourites)

        layoutManger=GridLayoutManager(activity as Context,2)

        dbBookList=RetrieveFavourites(activity as Context).execute().get()
        if (activity!=null){
            progressLayout.visibility=View.VISIBLE
            recyclarAdapter= AdapterFavourites(activity as Context,dbBookList)
            recyclarFavourites.adapter=recyclarAdapter
            recyclarFavourites.layoutManager=layoutManger
        }
        return view
    }

    class RetrieveFavourites(val context: Context):AsyncTask<Void,Void,List<BookEntity>>(){

        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()

            return db.bookDao().getAllBook()
        }
    }
}