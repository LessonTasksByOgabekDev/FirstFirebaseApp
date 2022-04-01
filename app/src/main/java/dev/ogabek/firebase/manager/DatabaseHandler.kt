package dev.ogabek.firebase.manager

import dev.ogabek.firebase.model.Post

interface DatabaseHandler {
    fun onSuccess(post: Post? = null, posts: ArrayList<Post> = ArrayList())
    fun onError()
}