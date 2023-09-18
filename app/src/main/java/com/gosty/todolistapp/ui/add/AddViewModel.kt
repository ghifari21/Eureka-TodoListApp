package com.gosty.todolistapp.ui.add

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.data.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    /***
     * This method to send book data to Firebase Realtime Database.
     * @param book variable that contain book model
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    fun postBook(book: Book) = bookRepository.postBook(book)
}