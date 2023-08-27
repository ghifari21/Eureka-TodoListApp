package com.gosty.todolistapp.ui.main

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.repositories.BookRepository

class MainViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    fun getAllBooks() = bookRepository.getAllBooks()
}