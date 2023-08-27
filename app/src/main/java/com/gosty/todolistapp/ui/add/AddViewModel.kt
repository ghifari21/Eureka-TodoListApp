package com.gosty.todolistapp.ui.add

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.repositories.BookRepository

class AddViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    val result = bookRepository.resultPostBook

    fun postBook(book: Book) = bookRepository.postBook(book)
}