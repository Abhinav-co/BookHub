package com.abhi.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {

    @Insert
    fun inserBook(bookEntity: BookEntity)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

    @Query("SELECT *From books")
    fun getAllBook(): List<BookEntity>

    @Query("SELECT *From books WHERE bookId")
    fun getBookId(bookId:String):BookEntity
}