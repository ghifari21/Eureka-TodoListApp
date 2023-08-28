package com.gosty.todolistapp.data.repositories

import androidx.lifecycle.LiveData
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.utils.Result

interface BookRepository {
    val resultPostBook: LiveData<Result<String>>
    val resultUpdateBook: LiveData<Result<String>>
    val resultDeleteBook: LiveData<Result<String>>
    fun getAllBooks(): LiveData<Result<List<Book>>>
    fun getBookById(id: String): LiveData<Result<Book>>
    fun postBook(book: Book)
    fun updateBook(book: Book)
    fun deleteBook(id: String)
}