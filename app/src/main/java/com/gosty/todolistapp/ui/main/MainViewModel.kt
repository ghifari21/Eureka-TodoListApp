package com.gosty.todolistapp.ui.main

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    /***
     * This method to get all books from realtime database.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    fun getAllBooks() = bookRepository.getAllBooks()
}