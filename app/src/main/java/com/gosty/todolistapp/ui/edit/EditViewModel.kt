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
    /***
     * This method to update a book in realtime database.
     * @param book variable that contain book model
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see Book
     */
    fun updateBook(book: Book) = bookRepository.updateBook(book)
}