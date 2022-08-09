package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface Repository {
    val data : LiveData<List<Post>>
    fun like(post: Post)
    fun share(post: Post)
    fun delete(postId: Long)
    fun save(post: Post)

    companion object {
        const val NEW_POST_ID = 0L
    }
}
