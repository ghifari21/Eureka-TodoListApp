package com.gosty.todolistapp.ui.main

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    fun getAllBooks() = bookRepository.getAllBooks()
}