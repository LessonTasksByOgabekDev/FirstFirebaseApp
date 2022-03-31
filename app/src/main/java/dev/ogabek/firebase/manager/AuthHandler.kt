package dev.ogabek.firebase.manager

import java.lang.Exception

interface AuthHandler {

    fun onSuccess()
    fun onError(exception: Exception?)

}