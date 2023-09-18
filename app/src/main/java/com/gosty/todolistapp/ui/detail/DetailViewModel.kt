package com.gosty.todolistapp.ui.detail

import androidx.lifecycle.ViewModel
import com.gosty.todolistapp.data.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    /***
     * This method to delete a book from realtime database.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    fun deleteBook(id: String) = bookRepository.deleteBook(id)

    /***
     * This method to get book based on the id from realtime database.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    fun getBookById(id: String) = bookRepository.getBookById(id)
}