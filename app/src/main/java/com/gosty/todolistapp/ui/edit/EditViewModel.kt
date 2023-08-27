package com.gosty.todolistapp.ui.edit

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.repositories.BookRepository

class EditViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    val result = bookRepository.resultUpdateBook

    fun updateBook(book: Book) = bookRepository.updateBook(book)
}