package com.abhi.bookhub.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.abhi.bookhub.R
import com.abhi.bookhub.database.BookDatabase
import com.abhi.bookhub.database.BookEntity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookRating: TextView
    lateinit var txtBookDiscription: TextView
    lateinit var imgBookImage: ImageView
    lateinit var btAddToFavourites: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar

    var bookId: String? = "100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookAuthor = findViewById(R.id.txtAuthorName)
        txtBookName = findViewById(R.id.txtDBookName)
        txtBookRating = findViewById(R.id.txtDBookRating)
        txtBookPrice = findViewById(R.id.txtPriceName)
        txtBookDiscription = findViewById(R.id.txtBookDiscip)
        imgBookImage = findViewById(R.id.ivBookImage)
        btAddToFavourites = findViewById(R.id.btaddToFavourites)
        progressBar = findViewById(R.id.ProgressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

        } else {
            finish()
            Toast.makeText(this, "some Unexpected error occour", Toast.LENGTH_SHORT).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this, "some Unexpected error occour", Toast.LENGTH_SHORT).show()

        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,
            Response.Listener {

                try {
                    val success = it.getBoolean("success")
                    if (success) {
                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE


                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.default_book_cover).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDiscription.text = bookJsonObject.getString("description")

                        val bookEntity = BookEntity(
                            bookId?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDiscription.text.toString(),
                            bookImageUrl
                        )

                        val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                        val isFav = checkFav.get()

                        if (isFav) {
                            btAddToFavourites.text = "Remove From Favourites"
                        } else {
                            btAddToFavourites.text = "Add To Favourites"
                        }
                        btAddToFavourites.setOnClickListener {
                            if (!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()) {
                                val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(this, "added to Favourites", Toast.LENGTH_SHORT)
                                        .show()
                                    btAddToFavourites.text = "Remove To Favourites"

                                } else {
                                    Toast.makeText(this, "Some Error Occoured", Toast.LENGTH_SHORT)
                                        .show()

                                }
                            } else {
                                val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                val result = async.get()
                                if (result) {
                                    Toast.makeText(
                                        this,
                                        "removed from Favourites",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    btAddToFavourites.text = "Add from Favourites"
                                } else {
                                    Toast.makeText(this, "Some Error Occoured", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "some Unexpected Error !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "some Unexpected Error !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(
                    this@DescriptionActivity,
                    "some Volley Unexpected Error $it!",
                    Toast.LENGTH_SHORT
                ).show()

            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "12fed3f7359e20"
                return headers
            }

        }


    }

    private fun BookEntity(
        bookId: Int,
        bookName: String,
        bookAuthor: String,
        bookRating: String,
        bookPrice: String,
        bookImage: String,
        bookImageUrl: String
    ): BookEntity {
        TODO("Not yet implemented")
    }


    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*Mode1-check the dataBase if the books is fav or not
        Mode2-add book to fav
        mode3-remove book from fav
         */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    val book: BookEntity? = db.bookDao().getBookId(bookEntity.bookId.toString())
                    db.close()
                    return book != null
                }
                2 -> {
                    db.bookDao().inserBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false
        }

    }
}