package com.gosty.todolistapp.data.repositories

import androidx.lifecycle.LiveData
import com.gosty.todolistapp.data.models.Book
import com.gosty.todolistapp.utils.Result

interface BookRepository {
    /***
     * This method is a contract that need to be implement in BookRepositoryImpl to get all the book collection from realtime database.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see BookRepositoryImpl
     */
    fun getAllBooks(): LiveData<Result<List<Book>>>

    /***
     * This method is a contract that need to be implement in BookRepositoryImpl to get book based on the id from realtime database.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see BookRepositoryImpl
     */
    fun getBookById(id: String): LiveData<Result<Book>>

    /***
     * This method is a contract that need to be implement in BookRepositoryImpl to add the book data to realtime database.
     * @param book variable that contain book model
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see BookRepositoryImpl
     * @see Book
     */
    fun postBook(book: Book): LiveData<Result<String>>

    /***
     * This method is a contract that need to be implement in BookRepositoryImpl to update the book data in realtime database.
     * @param book variable that contain book model
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see BookRepositoryImpl
     * @see Book
     */
    fun updateBook(book: Book): LiveData<Result<String>>

    /***
     * This method is a contract that need to be implement in BookRepositoryImpl to delete book that match the ID from realtime database.
     * @param id variable that indicate book ID
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     * @see BookRepositoryImpl
     */
    fun deleteBook(id: String): LiveData<Result<String>>
}