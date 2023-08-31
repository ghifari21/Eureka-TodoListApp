package com.gosty.todolistapp.data.repositories

import androidx.lifecycle.LiveData
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.utils.Result

interface BookRepository {
    fun getAllBooks(): LiveData<Result<List<Book>>>
    fun getBookById(id: String): LiveData<Result<Book>>
    fun postBook(book: Book): LiveData<Result<String>>
    fun updateBook(book: Book): LiveData<Result<String>>
    fun deleteBook(id: String): LiveData<Result<String>>
}