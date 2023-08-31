package com.gosty.todolistapp.ui.detail

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    fun deleteBook(id: String) = bookRepository.deleteBook(id)

    fun getBookById(id: String) = bookRepository.getBookById(id)
}