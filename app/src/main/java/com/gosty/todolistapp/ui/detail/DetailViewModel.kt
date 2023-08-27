package com.gosty.todolistapp.ui.detail

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.repositories.BookRepository

class DetailViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    val result = bookRepository.resultDeleteBook

    fun deleteBook(id: String) = bookRepository.deleteBook(id)

    fun getBookById(id: String) = bookRepository.getBookById(id)
}