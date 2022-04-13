package com.abhi.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abhi.bookhub.R
import com.abhi.bookhub.activity.DescriptionActivity
import com.abhi.bookhub.book.Book
import com.squareup.picasso.Picasso


//12fed3f7359e20

class AdapterDashboardRecyclarViewList(val context: Context, val itemList: ArrayList<Book>) :RecyclerView.Adapter<AdapterDashboardRecyclarViewList.DashboardViewHolder>(){

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtBookName:TextView=view.findViewById(R.id.txtBookName)
        val txtAuthor:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtRating:TextView=view.findViewById(R.id.txtBookRating)
        val txtBookCost:TextView=view.findViewById(R.id.txtBookPrice)
        val imgBook:ImageView=view.findViewById(R.id.imgBookImage)
        val bookLayout:LinearLayout=view.findViewById(R.id.BookLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.recyclar_dashbord_list,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemList[position]
        holder.txtBookName.text=book.bookName
        holder.txtAuthor.text=book.bookAuthor
        holder.txtBookCost.text=book.bookPrice
        holder.txtRating.text=book.bookRating
        //holder.imgBook.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBook)

        holder.bookLayout.setOnClickListener {
            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }
    }

}