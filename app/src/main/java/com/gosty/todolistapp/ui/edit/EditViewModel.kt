package com.gosty.todolistapp.ui.edit

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    fun updateBook(book: Book) = bookRepository.updateBook(book)
}