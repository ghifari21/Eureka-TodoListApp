package com.gosty.todolistapp.data.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Book(
    val id: String? = null,
    val cover: String? = null,
    val title: String? = null,
    val author: String? = null,
    val year: Int? = null,
    val category: String? = null
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> =
        mapOf(
            "id" to id,
            "cover" to cover,
            "title" to title,
            "author" to author,
            "year" to year,
            "category" to category
        )
}
