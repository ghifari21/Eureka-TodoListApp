package com.gosty.todolistapp.utils

object Utility {
    /***
     * This method to generate random string for ID with 16 chars.
     * @author Ghifari Octaverin
     * @since Sept 15th, 2023
     */
    fun getRandomString(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..16)
            .map { allowedChars.random() }
            .joinToString("")
    }
}